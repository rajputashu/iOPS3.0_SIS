package com.sisindia.ai.android.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import com.droidcommons.dagger.worker.AndroidWorkerInjection;
import com.sisindia.ai.android.base.BaseWorker;
import com.sisindia.ai.android.models.TableSyncResponse;
import com.sisindia.ai.android.room.dao.DailyTimeLineDao;
import com.sisindia.ai.android.room.dao.DutyStatusDao;
import com.sisindia.ai.android.room.entities.DutyStatusEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class DutyStatusWorker extends BaseWorker {

    @Inject
    DutyStatusDao dutyStatusDao;

    @Inject
    DailyTimeLineDao dailyTimeLineDao;

    @Inject
    public DutyStatusWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        AndroidWorkerInjection.inject(this);
    }

    @NonNull
    @Override
    public Result doWork() {
        addDisposable(dutyStatusDao.fetchAllNotSyncDutyStatus(false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onItemsFetched, Timber::e));
        return Result.success();
    }

    /*private void insertOrUpdateDutyOnOffToDB() {
        boolean isDutyOn = getInputData().getBoolean(DutyStatusWorker.class.getSimpleName(), true);
        if (isDutyOn) {
            DutyStatusEntity item = new DutyStatusEntity(true);
            item.dutyOnLocation = Prefs.getDouble(PrefConstants.LATITUDE, 0.0) + "," +
                    Prefs.getDouble(PrefConstants.LONGITUDE, 0.0);
            addDisposable(dutyStatusDao.insertDutyOnRecord(item)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((rowId, throwable) -> {
                    }));
            insertDailyTimeLine(new DailyTimeLineEntity("DutyOn", ""));
        } else {
            String dutyOffDateTime = LocalDateTime.now().toString();
            String offLocation = Prefs.getDouble(PrefConstants.LATITUDE, 0.0) + "," +
                    Prefs.getDouble(PrefConstants.LONGITUDE, 0.0);
            addDisposable(dutyStatusDao.updateDutyOffToLastRecord(dutyOffDateTime, offLocation, false)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe((rowId, throwable) -> uploadDutyStatusToServer()));
                    .subscribe((rowId, throwable) -> {
                    }));

            insertDailyTimeLine(new DailyTimeLineEntity("DutyOff", ""));
        }
    }*/

    /*private void insertDailyTimeLine(DailyTimeLineEntity dailyTimeline) {
        addDisposable(dailyTimeLineDao.insert(dailyTimeline)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::fetchAllNonSyncedItems, Timber::e));
                .subscribe(l -> {
                }, Timber::e));
    }*/

    /*
     * Don't delete the code just
     * COMMENTING BELOW CODE AS PER REQUIREMENT RAISED BY MANISH SIR
     */
    /*private void fetchAllNonSyncedItems(Long aLong) {
        addDisposable(dailyTimeLineDao.fetchAllNotSyncedItems(false)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::uploadDailyTimeLineToServer, Timber::e));
    }
    private void uploadDailyTimeLineToServer(List<DailyTimeLineEntity> items) {
        for (DailyTimeLineEntity item : items) {
            addDisposable(coreApi.dailyTimeLine(item)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> updateDailyTimeLine(response.data, item), this::onApiError));
        }
    }
    private void updateDailyTimeLine(TableSyncResponse.TableSyncData data, DailyTimeLineEntity item) {
        if (data != null && data.serverId != 0) {
            addDisposable(dailyTimeLineDao.updateItemOnServerSync(data.serverId, item.localId, true)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rowId -> Timber.d("Duty status row id %s", rowId), Timber::e));
        }
    }*/

   /* private void uploadDutyStatusToServer() {
        addDisposable(dutyStatusDao.fetchAllNotSyncDutyStatus(false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onItemsFetched, Timber::e));
    }*/

    private void onItemsFetched(List<DutyStatusEntity> items) {
        for (DutyStatusEntity item : items) {
            addDisposable(coreApi.isDutyOnOff(item)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> onTableSynced(response.data, item), this::onApiError));
        }
    }

    private void onTableSynced(TableSyncResponse.TableSyncData data, DutyStatusEntity item) {
        if (data != null && data.serverId != 0) {
            addDisposable(dutyStatusDao.updateOnSyncToServer(item.localId, data.serverId, true)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rowId -> Timber.d("Duty status row id %s", rowId), Timber::e));
        }
    }
}
