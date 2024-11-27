package com.sisindia.ai.android.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import com.droidcommons.dagger.worker.AndroidWorkerInjection;
import com.sisindia.ai.android.base.BaseWorker;
import com.sisindia.ai.android.models.TableSyncResponse;
import com.sisindia.ai.android.room.dao.EmployeeFineRewardDao;
import com.sisindia.ai.android.room.entities.EmployeeFineRewardEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class AddRewardFineWorker extends BaseWorker {

    @Inject
    EmployeeFineRewardDao fineRewardDao;

    @Inject
    public AddRewardFineWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        AndroidWorkerInjection.inject(this);
    }

    @NonNull
    @Override
    public Result doWork() {
        fetchNotSyncedItems();
        return Result.success();
    }

    private void fetchNotSyncedItems() {
        addDisposable(fineRewardDao.fetchAllNotSyncedItems(false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::uploadToServer, Timber::e));
    }

    private void uploadToServer(List<EmployeeFineRewardEntity> items) {
        /*for (EmployeeFineRewardEntity item : items) {
            addDisposable(coreApi.addRewardFine(item)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(task -> onUploadToServer(task.data, item), this::onApiError));
        }*/
        if (items.size() > 0)
            recursiveUploadToServer(items, 0);
    }

    private void recursiveUploadToServer(List<EmployeeFineRewardEntity> items, int noOfCalls) {
        if (noOfCalls < items.size()) {
            addDisposable(coreApi.addRewardFine(items.get(noOfCalls))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(task -> {
                        if (task.statusCode == 200)
                            updateSyncStatus(task.data, items.get(noOfCalls).localId);
                        recursiveUploadToServer(items, noOfCalls + 1);
                    }, e -> recursiveUploadToServer(items, noOfCalls + 1)));
        }
    }

    /*private void onUploadToServer(TableSyncResponse.TableSyncData data, EmployeeFineRewardEntity item) {
        if (data != null && data.serverId != 0)
            addDisposable(fineRewardDao.updateOnSyncedToServer(data.serverId, item.localId, true)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(integer -> Timber.d("rowId Updated for reward fine"), Timber::e));
    }*/

    private void updateSyncStatus(TableSyncResponse.TableSyncData data, String localId) {
        if (data != null && data.serverId != 0)
            addDisposable(fineRewardDao.updateOnSyncedToServer(data.serverId, localId, true)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(integer -> Timber.d("rowId Updated for reward fine"), Timber::e));
    }
}
