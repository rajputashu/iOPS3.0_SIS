package com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.available;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.common.internal.Objects;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;
import com.sisindia.ai.android.constants.IntentConstants;
import com.sisindia.ai.android.constants.IntentRequestCodes;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.databinding.FragmentGuardAvailableBinding;
import com.sisindia.ai.android.features.imagecapture.CaptureImageActivityV2;
import com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.GuardConfirmDialogFragment;
import com.sisindia.ai.android.mlcore.camera.QRCameraSource;
import com.sisindia.ai.android.mlcore.qrdetection.BarcodeProcessorQR;
import com.sisindia.ai.android.mlcore.qrdetection.WorkflowState;
import com.sisindia.ai.android.room.entities.AttachmentEntity;

import org.parceler.Parcels;

import java.io.IOException;

public class GuardAvailableFragment extends IopsBaseFragment {

    private GuardAvailableViewModel viewModel;
    private FragmentGuardAvailableBinding binding;
    private QRCameraSource cameraSource;
    private AnimatorSet promptChipAnimator;
    private WorkflowState currentWorkflowState;

    public static GuardAvailableFragment newInstance(Boolean isPractoTask) {
        GuardAvailableFragment fragment = new GuardAvailableFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IntentConstants.IS_PRACTO_TASK, isPractoTask);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void extractBundle() {
        if (getArguments() != null && getArguments().containsKey(IntentConstants.IS_PRACTO_TASK)) {
            boolean isPRACTOTask = getArguments().getBoolean(IntentConstants.IS_PRACTO_TASK, false);
            if (isPRACTOTask) {
                viewModel.guardStatus.set(GuardAvailableViewModel.GuardAvailableStatus.GUARD_SLEEPING);
                viewModel.isComingFromPractoTask = true;
            }
        }
    }

    @Override
    protected void initViewModel() {
        viewModel = (GuardAvailableViewModel) getAndroidViewModel(GuardAvailableViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        binding = (FragmentGuardAvailableBinding) bindFragmentView(getLayoutResource(), inflater, container);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {
        viewModel.workflowState.observe(this,
                workflowState -> {
                    if (workflowState == null || Objects.equal(currentWorkflowState, workflowState)) {
                        return;
                    }

                    currentWorkflowState = workflowState;
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
            if (barcode != null) {
                currentWorkflowState = WorkflowState.NOT_STARTED;
                viewModel.setWorkflowState(WorkflowState.DETECTED);
                stopCameraPreview();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    viewModel.onGuardQrScanned(barcode.getRawValue());
            }
        });

        liveData.observe(this, message -> {
            switch (message.what) {
                case NavigationConstants.ON_OPEN_GUARD_SLEEPING_IMAGE_CAPTURE:
                    AttachmentEntity item = (AttachmentEntity) message.obj;
                    openGuardSleepingImageCaptureScreen(item);
                    break;

                case NavigationConstants.ON_GUARD_DETAIL_FOUND:
                    openGuardReadyToCheckInScreen();
                    break;
            }
        });
    }

    private void openGuardReadyToCheckInScreen() {
        if (getChildFragmentManager().findFragmentByTag(GuardConfirmDialogFragment.class.getSimpleName()) == null) {
            GuardConfirmDialogFragment fragment = GuardConfirmDialogFragment.newInstance();
            fragment.setCancelable(false);
            fragment.show(getChildFragmentManager(), GuardConfirmDialogFragment.class.getSimpleName());
        }
    }

    private void openGuardSleepingImageCaptureScreen(AttachmentEntity item) {
        startActivityForResult(CaptureImageActivityV2.newIntent(requireActivity(), item), IntentRequestCodes.REQUEST_CODE_CAPTURE_SLEEPING_GUARD);
    }

    @Override
    protected void onCreated() {
        viewModel.initViewModel();

        cameraSource = new QRCameraSource(binding.cameraPreviewGraphicOverlay);
        promptChipAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(requireActivity(),
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

        binding.ivRetake.setOnClickListener(v -> onResume());
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
                e.printStackTrace();
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
        if (cameraSource != null) {
            cameraSource.release();
            cameraSource = null;
        }
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentRequestCodes.REQUEST_CODE_CAPTURE_SLEEPING_GUARD) {
            if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null && data.getExtras().containsKey(AttachmentEntity.class.getSimpleName())) {
                viewModel.guardStatus.set(GuardAvailableViewModel.GuardAvailableStatus.GUARD_SLEEPING);
                viewModel.attachment = Parcels.unwrap(data.getExtras().getParcelable(AttachmentEntity.class.getSimpleName()));
            } else {
                Toast.makeText(requireActivity(), "Unable to Capture Image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_guard_available;
    }
}
