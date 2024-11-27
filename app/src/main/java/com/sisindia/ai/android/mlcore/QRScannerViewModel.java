package com.sisindia.ai.android.mlcore;

import android.app.Application;
import android.view.View;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.mlcore.camera.DetectedObject;
import com.sisindia.ai.android.mlcore.qrdetection.WorkflowState;

import javax.inject.Inject;

public class QRScannerViewModel extends IopsBaseViewModel {

    public MutableLiveData<FirebaseVisionBarcode> detectedBarCode = new MutableLiveData<>();
    public MutableLiveData<WorkflowState> workflowState = new MutableLiveData<>();
    private boolean isCameraLive = false;
    public ObservableBoolean isCheckInOutRequest = new ObservableBoolean(false);

    @Nullable
    private DetectedObject confirmedObject;

    @Inject
    public QRScannerViewModel(@NonNull Application application) {
        super(application);
    }


    @MainThread
    public void setWorkflowState(WorkflowState workflowState) {
        if (!workflowState.equals(WorkflowState.CONFIRMED)
                && !workflowState.equals(WorkflowState.SEARCHING)
                && !workflowState.equals(WorkflowState.SEARCHED)) {
            confirmedObject = null;
        }
        this.workflowState.setValue(workflowState);
    }

    public void markCameraLive() {
        isCameraLive = true;
    }

    public void markCameraFrozen() {
        isCameraLive = false;
    }

    public boolean isCameraLive() {
        return isCameraLive;
    }

    public String getQRCodeViaSplit(String rawData) {
        String qrCode = "NA";
        if (!rawData.isEmpty()) {
            if (rawData.contains("QRCODE"))
                qrCode = rawData.split("QRCODE")[1].split("Vendor")[0].replaceAll(":", "").trim();
        }
        return qrCode;
    }

    public void onSkipCheckInOutButton(View view) {
        message.what = NavigationConstants.ON_SKIP_CHECK_IN_OUT_QR_SCAN;
        liveData.postValue(message);
    }
}
