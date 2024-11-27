package com.sisindia.ai.android.features.register;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseActivity;
import com.sisindia.ai.android.constants.IntentRequestCodes;
import com.sisindia.ai.android.databinding.ActivityRegisterCheckBinding;
import com.sisindia.ai.android.room.entities.CheckedRegisterEntity;
import com.sisindia.ai.android.utils.TimeUtils;

import static com.sisindia.ai.android.constants.NavigationConstants.ON_REGISTER_CHECK_DONE;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_REGISTER_UPLOAD_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.TASK_TIMER_TIK;

public class RegisterCheckActivity extends IopsBaseActivity {

    private ActivityRegisterCheckBinding binding;
    private RegisterCheckViewModel viewModel;

    public static Intent newIntent(Activity activity) {
        return new Intent(activity, RegisterCheckActivity.class);
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
                case ON_REGISTER_UPLOAD_CLICK:
                    CheckedRegisterEntity item = (CheckedRegisterEntity) message.obj;
                    openScanDocument(item);
                    break;
                case ON_REGISTER_CHECK_DONE:
                    onRegisterCheckDone();
                    break;
            }
        });
    }

    private void onRegisterCheckDone() {
        setResult(RESULT_OK);
        this.finish();
    }

    private void openScanDocument(CheckedRegisterEntity item) {
        startActivityForResult(DocumentCaptureActivity.newIntent(this, item), IntentRequestCodes.REQUEST_CODE_SCAN_DOCUMENT);
    }

    private void bindReviewInformationTime(int seconds) {
        binding.includeTimeSpent.tvTimeSpent.setText(TimeUtils.convertIntSecondsToHHMMSS(seconds));
    }

    @Override
    protected void onCreated() {
        setupToolBarForBackArrow(binding.tbRegisterCheck);
        viewModel.initViewModel();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void initViewBinding() {
        binding = (ActivityRegisterCheckBinding) bindActivityView(this, getLayoutResource());
        binding.setVm(viewModel);
        binding.executePendingBindings();
    }

    @Override
    protected void initViewModel() {
        viewModel = (RegisterCheckViewModel) getAndroidViewModel(RegisterCheckViewModel.class);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_register_check;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentRequestCodes.REQUEST_CODE_SCAN_DOCUMENT) {
            if (resultCode == Activity.RESULT_OK)
                viewModel.initViewModel();
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