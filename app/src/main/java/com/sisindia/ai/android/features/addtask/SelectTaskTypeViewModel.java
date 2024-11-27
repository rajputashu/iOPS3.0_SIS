package com.sisindia.ai.android.features.addtask;

import android.app.Application;

import androidx.annotation.NonNull;

import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.room.dao.TaskTypeDao;
import com.sisindia.ai.android.uimodels.TaskTypeModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SelectTaskTypeViewModel extends IopsBaseViewModel {

    public SelectTaskRecyclerAdapter selectTaskRecyclerAdapter = new SelectTaskRecyclerAdapter();

    @Inject
    public TaskTypeDao taskTypeDao;

    public SelectTaskTypeViewListeners taskTypeViewListeners = this::onTaskTypeSelected;

    @Inject
    public SelectTaskTypeViewModel(@NonNull Application application) {
        super(application);
    }


    private void onTaskTypeSelected(TaskTypeModel model) {
        message.what = NavigationConstants.ON_CREATE_TASK_CLICK;
        message.obj = model;
        liveData.postValue(message);
    }

    /**
     * For Select Task
     */
    public void initViewModel() {
        addDisposable(taskTypeDao.fetchAllActiveTaskTypes(true)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> selectTaskRecyclerAdapter.clearAndSetItems(items), Timber::e));
    }

}
