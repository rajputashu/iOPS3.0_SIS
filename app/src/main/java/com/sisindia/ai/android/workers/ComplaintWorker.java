package com.sisindia.ai.android.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import com.droidcommons.dagger.worker.AndroidWorkerInjection;
import com.sisindia.ai.android.base.BaseWorker;
import com.sisindia.ai.android.models.TableSyncResponse;
import com.sisindia.ai.android.room.dao.ComplaintDao;
import com.sisindia.ai.android.room.entities.ComplaintEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ComplaintWorker extends BaseWorker {


    @Inject
    ComplaintDao complaintDao;

    @Inject
    public ComplaintWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        AndroidWorkerInjection.inject(this);
    }

    @NonNull
    @Override
    public Result doWork() {

        int workType = getInputData().getInt(ComplaintWorker.class.getSimpleName(), ComplaintWorker.ComplaintWorkerType.SYNC_FROM_SERVER.workerType);

        if (workType == ComplaintWorker.ComplaintWorkerType.SYNC_FROM_SERVER.workerType) {

            syncFromServer();

        } else {

            syncToServer();

        }

        return Result.success();
    }

    private void syncFromServer() {

        addDisposable(
                Single.zip(
                        coreApi.getComplaints(),
                        complaintDao.fetchAll(), (serverList, localList) -> {
                            if (serverList != null && serverList.complaints != null) {
                                for (ComplaintEntity serverItem : serverList.complaints) {
                                    serverItem.serverId = serverItem.id;
                                    serverItem.isSync = true;
                                    if (localList.contains(serverItem)) {
                                        serverItem.id = localList.get(localList.indexOf(serverItem)).id;
                                    } else {
                                        serverItem.id = 0;
                                    }
                                    insertComplaint(serverItem);
                                }
                            }
                            return true;
                        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isSync -> {
                            Timber.d("Complaint Sync Success ");
                        }, this::onApiError)
        );
    }

    private void insertComplaint(ComplaintEntity item) {
        addDisposable(complaintDao.insert(item)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(row -> {
                    Timber.d("Complaint Added");
                }, Timber::e));
    }


    private void syncToServer() {
        addDisposable(complaintDao.fetchAllNotSyncedItems(false)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::uploadToServer, Timber::e));
    }

    private void uploadToServer(List<ComplaintEntity> items) {
        for (ComplaintEntity item : items) {
            if (item.serverId == 0) {
                addDisposable(coreApi.addComplaint(item)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(task -> onUploadToServer(task.data, item), Timber::e));
            } else {
                addDisposable(coreApi.updateComplaint(item)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(task -> onUploadToServer(task.data, item), Timber::e));
            }


        }
    }

    private void onUploadToServer(TableSyncResponse.TableSyncData data, ComplaintEntity item) {
        if (data != null && data.serverId != 0) {
            item.isSync = true;
            item.serverId = data.serverId;
            addDisposable(complaintDao.insert(item).subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(row -> Timber.d("rowId Updated for grievance"), Timber::e));
        }
    }


    public enum ComplaintWorkerType {

        SYNC_FROM_SERVER(1),

        SYNC_TO_SERVER(2);

        private final int workerType;

        ComplaintWorkerType(int workerType) {
            this.workerType = workerType;
        }

        public int getWorkerType() {
            return workerType;
        }
    }
}
