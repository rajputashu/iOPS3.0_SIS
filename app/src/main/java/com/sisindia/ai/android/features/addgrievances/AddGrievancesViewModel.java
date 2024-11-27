package com.sisindia.ai.android.features.addgrievances;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.room.dao.TaskDao;
import com.sisindia.ai.android.uimodels.BreadCumItemModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class AddGrievancesViewModel extends IopsBaseViewModel {


    public ObservableField<List<BreadCumItemModel>> breadCums = new ObservableField<>(new ArrayList<>());

    @Inject
    public TaskDao taskDao;

    private TaskTimer tik = new TaskTimer(0);

    @Inject
    public AddGrievancesViewModel(@NonNull Application application) {
        super(application);
    }

    public void initViewModel(int employeeId) {
        if (employeeId != 0) {
            message.arg1 = employeeId;
            message.what = NavigationConstants.ON_OPEN_ADD_GRIEVANCE_DETAIL;
            liveData.postValue(message);
        } else {
            message.what = NavigationConstants.ON_OPEN_ADD_GRIEVANCE_SELECT_GUARD;
            liveData.postValue(message);

        }
    }

    public void setBreadCum(int employeeId) {
        List<BreadCumItemModel> items = new ArrayList<>();
        breadCums.set(items);
        if (employeeId == 0) {
            items.add(BreadCumItemModel.guardDetailItem(false));
            items.add(BreadCumItemModel.grievanceItem(false));
        } else {
            items.add(BreadCumItemModel.guardDetailItem(true));
            items.add(BreadCumItemModel.grievanceItem(false));
        }
        breadCums.set(items);
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
