package com.sisindia.ai.android.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import com.droidcommons.dagger.worker.AndroidWorkerInjection;
import com.sisindia.ai.android.base.BaseWorker;
import com.sisindia.ai.android.models.TableSyncResponse;
import com.sisindia.ai.android.room.dao.GuardKitRequestDao;
import com.sisindia.ai.android.uimodels.akr.GuardKitRequestMO;
import com.sisindia.ai.android.uimodels.akr.KitRequestItemsMO;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class KitRequestWorker extends BaseWorker {

    @Inject
    GuardKitRequestDao kitRequestDao;

    @Inject
    public KitRequestWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
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
        addDisposable(kitRequestDao.fetchAllNotSyncedItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::uploadToServer, Timber::e));
    }

    /*private void uploadToServer(List<GuardKitRequestEntity> items) {
        for (GuardKitRequestEntity item : items) {
            addDisposable(kitRequestDao.fetchAllKitRequestItems(item.id)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(kitItems -> {
                        for (KitRequestItemEntity kitItem : kitItems) {
                            kitItem.id = 0;//to create entry in server
                        }
                        item.id = 0;//to create entry in server
                        item.kitItems = kitItems;
                        addDisposable(coreApi.addKitRequest(item)
                                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(response -> onUploadToServer(response.data, item), this::onApiError));
                    }, Timber::e));
        }
    }*/

    private void uploadToServer(List<GuardKitRequestMO> items) {
        for (GuardKitRequestMO item : items) {
            item.setSignatureId(0);
            item.setPhotoId(0);
            addDisposable(kitRequestDao.fetchAllKitRequestItems(item.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(kitItems -> {
                        for (KitRequestItemsMO kitItem : kitItems) {
                            kitItem.setId(0);//to create entry in server
                        }
                        item.setId(0);//to create entry in server
                        item.setKitRequestItems(kitItems);
                        addDisposable(coreApi.addKitRequest(item)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(response -> onUploadToServer(response.data, item), this::onApiError));
                    }, Timber::e));
        }
    }

    /*private void onUploadToServer(TableSyncResponse.TableSyncData data, GuardKitRequestEntity item) {
        if (data != null && data.serverId != 0)
            addDisposable(kitRequestDao.updateOnSyncedToServer(data.serverId, item.localId, true)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(integer -> Timber.d("rowId Updated for grievance"), Timber::e));
    }*/

    private void onUploadToServer(TableSyncResponse.TableSyncData data, GuardKitRequestMO item) {
        if (data != null && data.serverId != 0)
            addDisposable(kitRequestDao.updateOnSyncedToServer(data.serverId, item.getKitRequestGuid(), true)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(integer -> Timber.d("rowId Updated for grievance"), Timber::e));
    }
}