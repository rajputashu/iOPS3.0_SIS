package com.sisindia.ai.android.mlcore;

import static com.sisindia.ai.android.constants.IntentConstants.IS_COMING_FROM_SITE_CHECK_IN;
import static java.util.Objects.requireNonNull;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.common.internal.Objects;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseActivity;
import com.sisindia.ai.android.constants.IntentConstants;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.databinding.ActivityScanQrBinding;
import com.sisindia.ai.android.features.taskcheck.SkipReasonBottomSheet;
import com.sisindia.ai.android.mlcore.camera.QRCameraSource;
import com.sisindia.ai.android.mlcore.qrdetection.BarcodeProcessorQR;
import com.sisindia.ai.android.mlcore.qrdetection.WorkflowState;

import java.io.IOException;

public class ScanQRActivity extends IopsBaseActivity {

    private ActivityScanQrBinding binding;
    private QRScannerViewModel viewModel;
    private QRCameraSource cameraSource;
    private AnimatorSet promptChipAnimator;
    private WorkflowState currentWorkflowState;
    private Boolean isPostQRScanRequest = false;
    private Boolean isComingFromCheckIn = true;

    public static Intent newIntent(Activity activity) {
        return new Intent(activity, ScanQRActivity.class);
    }

    public static Intent newIntent(Activity activity, Boolean isPostQRScanRequest) {
        return new Intent(activity, ScanQRActivity.class).putExtra(IntentConstants.POST_QR_REQUEST, isPostQRScanRequest);
    }

    public static Intent newIntentCheckInOut(Activity activity, Boolean isComingFromCheckIn) {
        return new Intent(activity, ScanQRActivity.class)
                .putExtra(IntentConstants.POST_QR_REQUEST, true)
                .putExtra(IntentConstants.POST_QR_CHECK_IN_OUT, true)
                .putExtra(IS_COMING_FROM_SITE_CHECK_IN, isComingFromCheckIn);
    }

    @Override
    protected void extractBundle() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isPostQRScanRequest = requireNonNull(bundle).containsKey(IntentConstants.POST_QR_REQUEST);
            boolean isCheckInOut = requireNonNull(bundle).containsKey(IntentConstants.POST_QR_CHECK_IN_OUT);
            if (isCheckInOut)
                viewModel.isCheckInOutRequest.set(true);
            if (bundle.containsKey(IS_COMING_FROM_SITE_CHECK_IN)) {
                isComingFromCheckIn = bundle.getBoolean(IS_COMING_FROM_SITE_CHECK_IN);
            }
        }
    }

    @Override
    protected void initViewModel() {
        viewModel = (QRScannerViewModel) getAndroidViewModel(QRScannerViewModel.class);
    }

    @Override
    protected void initViewState() {
        viewModel.workflowState.observe(this,
                workflowState -> {
                    if (workflowState == null || Objects.equal(currentWorkflowState, workflowState)) {
                        return;
                    }
                    currentWorkflowState = workflowState;
//                    Timber.d("Current workflow state: " + currentWorkflowState.name());

                    boolean wasPromptChipGone = (binding.bottomPromptChip.getVisibility() == View.GONE);

                    switch (workflowState) {
                        case DETECTING:
                            binding.bottomPromptChip.setVisibility(View.VISIBLE);
                            binding.bottomPromptChip.setText(R.string.prompt_point_at_a_barcode);
                            startCameraPreview();
                            break;

                        case CONFIRMING:
                            binding.bottomPromptChip.setVisibility(View.VISIBLE);
                            binding.bottomPromptChip.setText(R.string.prompt_move_camera_closer);
                            startCameraPreview();
                            break;

                        case SEARCHING:
                            binding.bottomPromptChip.setVisibility(View.VISIBLE);
                            binding.bottomPromptChip.setText(R.string.string_prompt_searching);
                            stopCameraPreview();
                            break;

                        case DETECTED:
                            break;

                        case SEARCHED:
                            binding.bottomPromptChip.setVisibility(View.GONE);
                            stopCameraPreview();
                            break;

                        default:
                            binding.bottomPromptChip.setVisibility(View.GONE);
                            break;
                    }

                    boolean shouldPlayPromptChipEnteringAnimation =
                            wasPromptChipGone && (binding.bottomPromptChip.getVisibility() == View.VISIBLE);
                    if (shouldPlayPromptChipEnteringAnimation && !promptChipAnimator.isRunning()) {
                        promptChipAnimator.start();
                    }
                });

        viewModel.detectedBarCode.observe(this, barcode -> {
            if (barcode != null)
                onQrScanSuccess(barcode);
        });

        liveData.observe(this, message -> {
            if (message.what == NavigationConstants.ON_SKIP_CHECK_IN_OUT_QR_SCAN) {
                openSkipReasonBottomSheet();
//                onSkipQRScan();
            } else if (message.what == NavigationConstants.ON_SKIP_QR_SCAN_WITH_REASON) {
                dismissSkipBottomSheet();
                onSkipQRScan((String) message.obj);
            }
        });
    }

    private void onQrScanSuccess(FirebaseVisionBarcode barcode) {
        if (barcode != null && !TextUtils.isEmpty(barcode.getRawValue())) {
            Intent returnIntent = new Intent();
            String scannedQRValue = barcode.getRawValue();
            if (isPostQRScanRequest)
                scannedQRValue = viewModel.getQRCodeViaSplit(scannedQRValue);
            returnIntent.putExtra(IntentConstants.ON_QR_SCANNED, scannedQRValue);
            setResult(RESULT_OK, returnIntent);
            this.finish();
        }
    }

    private void openSkipReasonBottomSheet() {
        if (getSupportFragmentManager().findFragmentByTag(SkipReasonBottomSheet.class.getSimpleName()) == null) {
            SkipReasonBottomSheet.Companion.newInstance(isComingFromCheckIn)
                    .show(getSupportFragmentManager(),
                            SkipReasonBottomSheet.class.getSimpleName());
        }
    }

    private void dismissSkipBottomSheet() {
        SkipReasonBottomSheet bottomSheet = (SkipReasonBottomSheet) getSupportFragmentManager()
                .findFragmentByTag(SkipReasonBottomSheet.class.getSimpleName());
        if (bottomSheet != null)
            bottomSheet.dismissAllowingStateLoss();
    }

    private void onSkipQRScan(String skipReason) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(IntentConstants.ON_SKIP_QR_SCANNED, true);
        returnIntent.putExtra(IntentConstants.ON_SKIP_QR_REASON, skipReason);
        setResult(RESULT_OK, returnIntent);
        this.finish();
    }

    @Override
    protected void onCreated() {

        cameraSource = new QRCameraSource(binding.cameraPreviewGraphicOverlay);
        promptChipAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.bottom_prompt_chip_enter);
        promptChipAnimator.setTarget(binding.bottomPromptChip);

        binding.ivFlash.setOnClickListener(v -> {
            if (binding.ivFlash.isSelected()) {
                binding.ivFlash.setSelected(false);
                cameraSource.updateFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            } else {
                binding.ivFlash.setSelected(true);
                cameraSource.updateFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            }
        });

        binding.ivRetake.setOnClickListener(v -> {
            viewModel.markCameraFrozen();
            currentWorkflowState = WorkflowState.NOT_STARTED;
            viewModel.setWorkflowState(WorkflowState.DETECTING);
        });
    }

    @Override
    protected void initViewBinding() {
        binding = (ActivityScanQrBinding) bindActivityView(this, getLayoutResource());
        binding.setVm(viewModel);
        binding.executePendingBindings();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.markCameraFrozen();
        currentWorkflowState = WorkflowState.NOT_STARTED;
        cameraSource.setFrameProcessor(new BarcodeProcessorQR(binding.cameraPreviewGraphicOverlay, viewModel));
        viewModel.setWorkflowState(WorkflowState.DETECTING);
    }

    @Override
    public void onPause() {
        super.onPause();
        currentWorkflowState = WorkflowState.NOT_STARTED;
        stopCameraPreview();
    }

    private void startCameraPreview() {
        if (!viewModel.isCameraLive() && cameraSource != null) {
            try {
                viewModel.markCameraLive();
                binding.cameraPreview.start(cameraSource);
            } catch (IOException e) {
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    private void stopCameraPreview() {
        if (viewModel.isCameraLive()) {
            viewModel.markCameraFrozen();
            binding.ivFlash.setSelected(false);
            binding.cameraPreview.stop();
        }
    }


    @Override
    public void onDestroy() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (cameraSource != null) {
            cameraSource.release();
            cameraSource = null;
        }
        super.onDestroy();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_scan_qr;
    }
}
