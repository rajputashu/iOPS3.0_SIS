package com.sisindia.ai.android.features.onboard;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.features.billsubmit.RadioCheckedListener;

import javax.inject.Inject;

public class OnBoardViewModel extends IopsBaseViewModel {

    @Inject
    public OnBoardViewModel(@NonNull Application application) {
        super(application);
    }


    //Utilizing OnBoardViewModel for check in and out functionality used in DC/NC tasks
    private String skipReason = "";
    public RadioCheckedListener onRadioCheckedListener = value -> skipReason = value;
    public ObservableBoolean isSkipAtCheckInTime = new ObservableBoolean(true);

    public void onViewClick(View view) {
        if (view.getId() == R.id.btnSelectSkipReason) {

            if (skipReason.isEmpty())
                showToast("Please select reason for skipping");
            else {
                message.what = NavigationConstants.ON_SKIP_QR_SCAN_WITH_REASON;
                message.obj = skipReason;
                liveData.postValue(message);
            }
        }
    }
}
