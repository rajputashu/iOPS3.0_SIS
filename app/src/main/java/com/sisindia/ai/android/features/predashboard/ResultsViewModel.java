package com.sisindia.ai.android.features.predashboard;

import android.app.Application;

import androidx.annotation.NonNull;

import com.sisindia.ai.android.base.IopsBaseViewModel;

import javax.inject.Inject;

public class ResultsViewModel extends IopsBaseViewModel {

    @Inject
    public ResultsViewModel(@NonNull Application application) {
        super(application);
    }

}
