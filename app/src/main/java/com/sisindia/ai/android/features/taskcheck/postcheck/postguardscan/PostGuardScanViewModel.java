package com.sisindia.ai.android.features.taskcheck.postcheck.postguardscan;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.room.dao.DayCheckDao;
import com.sisindia.ai.android.room.dao.TaskDao;
import com.sisindia.ai.android.room.entities.CheckedGuardEntity;
import com.sisindia.ai.android.uimodels.BreadCumItemModel;
import com.sisindia.ai.android.uimodels.CheckedGuardItemModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.sisindia.ai.android.constants.NavigationConstants.ON_ADD_SIGNATURE_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_PHOTO_EVALUATION_DONE;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_GUARD_SUMMARY;
import static com.sisindia.ai.android.room.entities.CheckedGuardEntity.CurrentState.EVALUATION;
import static com.sisindia.ai.android.room.entities.CheckedGuardEntity.CurrentState.SIGNATURE;

public class PostGuardScanViewModel extends IopsBaseViewModel {

    public ObservableField<List<BreadCumItemModel>> breadCums = new ObservableField<>(new ArrayList<>());

    @Inject
    public DayCheckDao dayCheckDao;

    @Inject
    public TaskDao taskDao;

    private TaskTimer tik = new TaskTimer(0);

    @Inject
    public PostGuardScanViewModel(@NonNull Application application) {
        super(application);
    }

    public void initViewModel() {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        int postId = Prefs.getInt(PrefConstants.CURRENT_POST);
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);
        int employeeId = Prefs.getInt(PrefConstants.SELECTED_EMPLOYEE_ID);
        addDisposable(dayCheckDao.fetchGuardDetailAfterScan(taskId, siteId, postId, employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCheckedGuard, Timber::e));
    }

    private void onCheckedGuard(CheckedGuardItemModel model) {
        CheckedGuardEntity.GuardStatus guardStatus = CheckedGuardEntity.GuardStatus.of(model.guardStatus);
        List<BreadCumItemModel> items = new ArrayList<>();

        if (guardStatus == CheckedGuardEntity.GuardStatus.SLEEPING)
            items.add(BreadCumItemModel.guardSleepingItem());

        items.add(BreadCumItemModel.guardAvailableItem());

        CheckedGuardEntity.CurrentState currentState = CheckedGuardEntity.CurrentState.of(model.currentState);

        items.add(BreadCumItemModel.guardTurnOutItem(currentState == EVALUATION));
        items.add(BreadCumItemModel.guardSignatureItem(currentState == SIGNATURE));

        switch (currentState) {
            case EVALUATION:
                message.what = ON_PHOTO_EVALUATION_DONE;
                liveData.postValue(message);
                break;

            case REWARD_FINE:
                message.what = ON_ADD_SIGNATURE_CLICK;
                liveData.postValue(message);
                break;

            case SIGNATURE:
                message.what = OPEN_GUARD_SUMMARY;
                liveData.postValue(message);
                break;

            default:
                break;
        }
        breadCums.set(items);
    }

    public void onPhotoEvaluationDone() {
        List<BreadCumItemModel> items = breadCums.get();
        BreadCumItemModel i = new BreadCumItemModel(10);
        if (items != null) {
            if (items.contains(i)) {
                int pos = items.indexOf(i);
                i = BreadCumItemModel.guardTurnOutItem(true);
                items.set(pos, i);
            }
            breadCums.set(items);
            breadCums.notifyChange();
        }
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
                .subscribe(rowId -> tik.cancel(), Timber::e));
    }

    public void onTakePhotoClick() {
        message.what = NavigationConstants.ON_TAKE_GUARD_PHOTO_FOR_EVALUATION;
        liveData.postValue(message);
    }
}
