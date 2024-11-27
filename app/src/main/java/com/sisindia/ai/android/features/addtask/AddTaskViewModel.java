package com.sisindia.ai.android.features.addtask;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.uimodels.TaskTypeModel;

import javax.inject.Inject;

public class AddTaskViewModel extends IopsBaseViewModel {


    public ObservableField<TaskTypeModel> selectedTaskTypeObs = new ObservableField<>();

    @Inject
    public AddTaskViewModel(@NonNull Application application) {
        super(application);
    }
}
