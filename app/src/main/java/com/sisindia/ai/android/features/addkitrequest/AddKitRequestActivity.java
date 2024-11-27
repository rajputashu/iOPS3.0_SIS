package com.sisindia.ai.android.features.addkitrequest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseActivity;
import com.sisindia.ai.android.commons.CaptureSignatureBottomSheetFragment;
import com.sisindia.ai.android.constants.IntentRequestCodes;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.databinding.ActivityAddKitRequestBinding;
import com.sisindia.ai.android.features.imagecapture.CaptureImageActivityV2;
import com.sisindia.ai.android.room.entities.AttachmentEntity;
import com.sisindia.ai.android.room.entities.EmployeeSiteEntity;

import org.parceler.Parcels;

import static com.sisindia.ai.android.constants.NavigationConstants.ON_KIT_REQUEST_COMPLETED;

public class AddKitRequestActivity extends IopsBaseActivity {

    private ActivityAddKitRequestBinding binding;

    private AddKitRequestViewModel viewModel;

    public static Intent newIntent(Activity activity, int employeeId) {
        Intent intent = new Intent(activity, AddKitRequestActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(EmployeeSiteEntity.class.getSimpleName(), employeeId);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_kit_request;
    }

    @Override
    protected void extractBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(EmployeeSiteEntity.class.getSimpleName())) {
            int employeeId = bundle.getInt(EmployeeSiteEntity.class.getSimpleName());
            viewModel.initViewModel(employeeId);
        } else {
            showToast("Invalid site");
        }
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            switch (message.what) {

                case NavigationConstants.ON_SHOW__KIT_ITEM_SIZES:
                    openKitItemSelectSizeBottomSheet();
                    break;

                case NavigationConstants.ON_KIT_REQUEST_CAPTURE_SIGNATURE:
                    AttachmentEntity signAttachment = (AttachmentEntity) message.obj;
                    openSignatureCaptureForKitRequest(signAttachment);
                    break;

                case NavigationConstants.ON_KIT_REQUEST_CAPTURE_IMAGE:
                    AttachmentEntity kitPhotoAttachment = (AttachmentEntity) message.obj;
                    openImageCaptureForKitRequest(kitPhotoAttachment);
                    break;

                case NavigationConstants.ON_KIT_REQUEST_DONE:
                    onKitRequestDone(message.arg1);
                    break;

                case ON_KIT_REQUEST_COMPLETED:
                    onKitRequestCompleted();
                    break;

            }
        });
    }

    private void onKitRequestCompleted() {
        setResult(RESULT_OK);
        this.finish();
    }

    private void onKitRequestDone(int kitRequestRowId) {

        if (getSupportFragmentManager().findFragmentByTag(AddedKitRequestDialogFragment.class.getSimpleName()) == null) {
            AddedKitRequestDialogFragment fragment = AddedKitRequestDialogFragment.newInstance(kitRequestRowId);
            fragment.setCancelable(false);
            fragment.show(getSupportFragmentManager(), AddedKitRequestDialogFragment.class.getSimpleName());
        }
    }

    private void openImageCaptureForKitRequest(AttachmentEntity item) {
        startActivityForResult(CaptureImageActivityV2.newIntent(this, item), IntentRequestCodes.REQUEST_CODE_CAPTURE_KIT_REQUEST);
    }

    private void openSignatureCaptureForKitRequest(AttachmentEntity signAttachment) {

        if (getSupportFragmentManager().findFragmentByTag(CaptureSignatureBottomSheetFragment.class.getSimpleName()) == null) {
            CaptureSignatureBottomSheetFragment fragment = CaptureSignatureBottomSheetFragment.newInstance(signAttachment);
            fragment.setCancelable(false);
            fragment.setOnSignDoneListeners(item -> {
                viewModel.signAttachmentObs.set(item);
                viewModel.signAttachmentObs.notifyChange();
            });
            fragment.show(getSupportFragmentManager(), CaptureSignatureBottomSheetFragment.class.getSimpleName());
        }
    }

    private void openKitItemSelectSizeBottomSheet() {
        if (getSupportFragmentManager().findFragmentByTag(SelectKitItemSizeBottomSheetFragment.class.getSimpleName()) == null) {
            SelectKitItemSizeBottomSheetFragment fragment = SelectKitItemSizeBottomSheetFragment.newInstance();
            fragment.setCancelable(false);
            fragment.show(getSupportFragmentManager(), SelectKitItemSizeBottomSheetFragment.class.getSimpleName());
        }
    }

    @Override
    protected void onCreated() {
        setupToolBarForBackArrow(binding.tbAddKitRequest);
    }

    @Override
    protected void initViewBinding() {
        binding = (ActivityAddKitRequestBinding) bindActivityView(this, getLayoutResource());
        binding.setVm(viewModel);
        binding.executePendingBindings();
    }

    @Override
    protected void initViewModel() {
        viewModel = (AddKitRequestViewModel) getAndroidViewModel(AddKitRequestViewModel.class);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentRequestCodes.REQUEST_CODE_CAPTURE_KIT_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null && data.getExtras().containsKey(AttachmentEntity.class.getSimpleName())) {
                if (Parcels.unwrap(data.getExtras().getParcelable(AttachmentEntity.class.getSimpleName())) != null)
                    viewModel.photoAttachmentObs.set(Parcels.unwrap(data.getExtras().getParcelable(AttachmentEntity.class.getSimpleName())));
            } else {
                showToast("Unable to Capture Image");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.stopTimer();
    }
}
