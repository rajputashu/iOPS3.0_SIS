package com.sisindia.ai.android.features.timline;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;

import com.sisindia.ai.android.base.IopsBaseViewModel;

import javax.inject.Inject;

import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_DASH_BOARD_DRAWER;

public class TimeLineViewModel extends IopsBaseViewModel {


    @Inject
    public TimeLineViewModel(@NonNull Application application) {
        super(application);
    }


    public void ivRotaDrawerClick(View view) {
        message.what = OPEN_DASH_BOARD_DRAWER;
        liveData.postValue(message);
    }

}
