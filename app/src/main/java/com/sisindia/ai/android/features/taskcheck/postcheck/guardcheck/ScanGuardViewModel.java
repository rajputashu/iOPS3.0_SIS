package com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck;

import android.app.Application;

import androidx.annotation.NonNull;

import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.room.dao.TaskDao;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ScanGuardViewModel extends IopsBaseViewModel {

    @Inject
    public TaskDao taskDao;

    private TaskTimer tik = new TaskTimer(0);


    @Inject
    public ScanGuardViewModel(@NonNull Application application) {
        super(application);
    }

    void startTimer() {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        addDisposable(taskDao.fetchTimeSpentViaTaskId(taskId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    tik = new TaskTimer(model.getTimeElapsed() - findTimeDifference(model.getLastElapsedTime()));
                    tik.start();
                }, Timber::e));
    }

    void stopTimer() {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        addDisposable(taskDao.updateTimeSpentByTaskId(taskId, tik.lastTik, getCurrentMilliOfTheDay())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rowId -> {
                    tik.cancel();
                }, Timber::e));
    }
}
