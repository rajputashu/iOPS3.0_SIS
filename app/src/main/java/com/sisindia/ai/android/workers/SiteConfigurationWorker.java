package com.sisindia.ai.android.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import com.droidcommons.dagger.worker.AndroidWorkerInjection;
import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.base.BaseWorker;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.room.dao.DayCheckDao;
import com.sisindia.ai.android.room.dao.SiteStrengthDao;
import com.sisindia.ai.android.room.entities.CacheStrengthEntity;
import com.sisindia.ai.android.room.entities.CheckedPostCheckListEntity;
import com.sisindia.ai.android.room.entities.CheckedRegisterEntity;
import com.sisindia.ai.android.room.entities.CheckedSiteCheckListEntity;
import com.sisindia.ai.android.room.entities.PostCheckListEntity;
import com.sisindia.ai.android.room.entities.PostRegisterEntity;
import com.sisindia.ai.android.room.entities.SiteCheckListEntity;
import com.sisindia.ai.android.room.entities.SiteStrengthEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SiteConfigurationWorker extends BaseWorker {

    @Inject
    DayCheckDao dayCheckDao;

    @Inject
    SiteStrengthDao strengthDao;


    @Inject
    public SiteConfigurationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        AndroidWorkerInjection.inject(this);
    }

    @NonNull
    @Override
    public Result doWork() {
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);

        addDisposable(dayCheckDao.fetchAllSiteCheckList(siteId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSiteCheckListFetched, Timber::e));


        addDisposable(dayCheckDao.fetchAllPostCheckList(siteId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPostCheckListFetched, Timber::e));

        addDisposable(dayCheckDao.fetchAllPostRegisterList(siteId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPostRegisterFetched, Timber::e));

        addDisposable(strengthDao.fetchAllStrengthBySiteId(siteId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSiteStrengthFetched, Timber::e));

        return Result.success();
    }

    private void onSiteStrengthFetched(List<SiteStrengthEntity> items) {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);

        addDisposable(strengthDao.isCacheStrengthPresent(taskId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(row -> {
                    if (row == 0) {
                        for (SiteStrengthEntity item : items) {
                            CacheStrengthEntity entity = new CacheStrengthEntity(item.id, siteId, taskId, item.postId, item.grade, item.authorizedStrength, item.shiftId, item.rankId);
                            addDisposable(strengthDao.insertCacheStrengthEntity(entity)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(id -> Timber.e("inserted"), Timber::e));
                        }
                    }
                }, Timber::e));
    }

    private void onPostRegisterFetched(List<PostRegisterEntity> items) {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);

        addDisposable(dayCheckDao.isCheckedRegisterPresent(taskId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(row -> {
                    if (row == 0) {
                        for (PostRegisterEntity item : items) {
                            CheckedRegisterEntity entity = new CheckedRegisterEntity(item.attachmentSourceTypeId, item.registerTypeId, item.postId, item.siteId, item.registerType, item.isMandatory, taskId);
                            addDisposable(dayCheckDao.insertCheckedRegister(entity)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(id -> Timber.e("inserted"), Timber::e));
                        }
                    }
                }, Timber::e));
    }

    private void onPostCheckListFetched(List<PostCheckListEntity> items) {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        addDisposable(dayCheckDao.isCheckedPostListPresent(taskId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(row -> {
                    if (row == 0) {
                        for (PostCheckListEntity item : items) {
                            CheckedPostCheckListEntity entity = new CheckedPostCheckListEntity(item.postChecklistId, item.checklistLabel, item.checklistQuestionType, item.postId, item.siteId, taskId);
                            addDisposable(dayCheckDao.insertCheckedPostCheckListItem(entity)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(id -> Timber.e("inserted"), Timber::e));
                        }
                    }
                }, Timber::e));
    }

    private void onSiteCheckListFetched(List<SiteCheckListEntity> items) {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        addDisposable(dayCheckDao.isCheckedSiteListPresent(taskId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(row -> {
                    if (row == 0) {
                        for (SiteCheckListEntity item : items) {
                            CheckedSiteCheckListEntity entity = new CheckedSiteCheckListEntity(item.siteChecklistId, item.checklistLabel, item.checklistQuestionType, item.siteId, taskId);
                            addDisposable(dayCheckDao.insertSiteCheckedList(entity)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(id -> Timber.e("inserted"), Timber::e));
                        }
                    }
                }, Timber::e));
    }
}
