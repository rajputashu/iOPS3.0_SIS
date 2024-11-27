package com.sisindia.ai.android.features.securityrisk;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseActivity;
import com.sisindia.ai.android.databinding.ActivityAddSecurityRiskBinding;
import com.sisindia.ai.android.features.imagecapture.CaptureImageActivityV2;
import com.sisindia.ai.android.room.entities.AttachmentEntity;
import com.sisindia.ai.android.utils.TimeUtils;

import org.parceler.Parcels;

import static com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_ADD_SECURITY_RISK_IMAGE_CAPTURE;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_ADD_SECURITY_DONE;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_ADD_SECURITY_IMAGE_CAPTURE;
import static com.sisindia.ai.android.constants.NavigationConstants.TASK_TIMER_TIK;

public class AddSecurityRiskActivity extends IopsBaseActivity {

    private ActivityAddSecurityRiskBinding binding;
    private AddSecurityRiskViewModel viewModel;

    public static Intent newIntent(Activity activity) {
        return new Intent(activity, AddSecurityRiskActivity.class);
    }

    @Override
    protected void extractBundle() {

    }

    @Override
    protected void initViewState() {

        liveData.observe(this, message -> {
            switch (message.what) {

                case TASK_TIMER_TIK:
                    bindReviewInformationTime(message.arg1);
                    break;


                case ON_ADD_SECURITY_IMAGE_CAPTURE:
                    AttachmentEntity item = (AttachmentEntity) message.obj;
                    openImageCaptureForAddSecurityRisk(item);
                    break;

                case ON_ADD_SECURITY_DONE:
                    onSavedSuccess();
                    break;
            }
        });
    }

    private void onSavedSuccess() {
        setResult(RESULT_OK);
        this.finish();
    }

    private void openImageCaptureForAddSecurityRisk(AttachmentEntity item) {
        startActivityForResult(CaptureImageActivityV2.newIntent(this, item), REQUEST_CODE_ADD_SECURITY_RISK_IMAGE_CAPTURE);
    }

    private void bindReviewInformationTime(int seconds) {
        binding.includeTimeSpent.tvTimeSpent.setText(TimeUtils.convertIntSecondsToHHMMSS(seconds));
    }

    @Override
    protected void onCreated() {
        setupToolBarForBackArrow(binding.tbAddSecurityRisk);
        viewModel.initViewModel();
    }

    @Override
    protected void initViewBinding() {
        binding = (ActivityAddSecurityRiskBinding) bindActivityView(this, getLayoutResource());
        binding.setVm(viewModel);
        binding.executePendingBindings();
    }

    @Override
    protected void initViewModel() {
        viewModel = (AddSecurityRiskViewModel) getAndroidViewModel(AddSecurityRiskViewModel.class);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case REQUEST_CODE_ADD_SECURITY_RISK_IMAGE_CAPTURE:
                if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null && data.getExtras().containsKey(AttachmentEntity.class.getSimpleName())) {
                    viewModel.photoState.set(AddSecurityRiskViewModel.AddSecurityPhotoState.CAPTURED);
                    viewModel.imageAttachment.set(Parcels.unwrap(data.getExtras().getParcelable(AttachmentEntity.class.getSimpleName())));
                    viewModel.imageAttachment.notifyChange();
                } else {
                    Toast.makeText(this, "Unable to Capture Image", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_security_risk;
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
