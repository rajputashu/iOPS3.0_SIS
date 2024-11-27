package com.sisindia.ai.android.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import com.droidcommons.dagger.worker.AndroidWorkerInjection;
import com.sisindia.ai.android.base.BaseWorker;
import com.sisindia.ai.android.models.TableSyncResponse;
import com.sisindia.ai.android.room.dao.GrievanceDao;
import com.sisindia.ai.android.room.entities.GrievanceEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class GrievanceWorker extends BaseWorker {

    @Inject
    GrievanceDao grievanceDao;

    @Inject
    public GrievanceWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        AndroidWorkerInjection.inject(this);
    }

    @NonNull
    @Override
    public Result doWork() {

        int workType = getInputData().getInt(GrievanceWorker.class.getSimpleName(), GrievanceWorkerType.SYNC_FROM_SERVER.workerType);

        if (workType == GrievanceWorkerType.SYNC_FROM_SERVER.workerType) {
            syncFromServer();
        } else {
            syncToServer();
        }

        return Result.success();
    }

    private void syncFromServer() {

        addDisposable(
                Single.zip(
                        coreApi.getGrievances(),
                        grievanceDao.fetchAll(), (serverList, localList) -> {
                            if (serverList != null && serverList.grievances != null) {
                                for (GrievanceEntity serverItem : serverList.grievances) {
                                    serverItem.serverId = serverItem.id;
                                    serverItem.isSync = true;
                                    if (localList.contains(serverItem)) {
                                        serverItem.id = localList.get(localList.indexOf(serverItem)).id;
                                    } else {
                                        serverItem.id = 0;
                                    }
                                    insertGrievance(serverItem);
                                }
                            }
                            return true;
                        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isSync -> {
                            Timber.d("Grievance Sync Success ");
                        }, Timber::e)
        );
    }

    private void insertGrievance(GrievanceEntity serverItem) {
        addDisposable(grievanceDao.insert(serverItem)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(row -> {
                    Timber.d("Grievance Added");
                }, Timber::e));
    }


    private void syncToServer() {
        addDisposable(grievanceDao.fetchAllNotSyncedItems(false)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::uploadToServer, Timber::e));
    }

    private void uploadToServer(List<GrievanceEntity> items) {
        for (GrievanceEntity item : items) {
            addDisposable(coreApi.addGrievance(item)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(task -> onUploadToServer(task.data, item), this::onApiError));
        }
    }

    private void onUploadToServer(TableSyncResponse.TableSyncData data, GrievanceEntity item) {
        if (data != null && data.serverId != 0)
            addDisposable(grievanceDao.updateOnSyncedToServer(data.serverId, item.localId, true)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(integer -> Timber.d("rowId Updated for grievance"), Timber::e));
    }

    public enum GrievanceWorkerType {

        SYNC_FROM_SERVER(1),
        SYNC_TO_SERVER(2);

        private final int workerType;

        GrievanceWorkerType(int workerType) {
            this.workerType = workerType;
        }

        public int getWorkerType() {
            return workerType;
        }
    }
}
