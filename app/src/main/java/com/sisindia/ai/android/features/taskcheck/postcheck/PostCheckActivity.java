package com.sisindia.ai.android.features.taskcheck.postcheck;

import static com.sisindia.ai.android.constants.NavigationConstants.TASK_TIMER_TIK;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseActivity;
import com.sisindia.ai.android.constants.IntentRequestCodes;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.databinding.ActivityPostCheckBinding;
import com.sisindia.ai.android.features.addgrievances.AddGrievancesActivity;
import com.sisindia.ai.android.features.addkitrequest.AddKitRequestActivity;
import com.sisindia.ai.android.features.register.RegisterCheckActivity;
import com.sisindia.ai.android.features.securityrisk.AddSecurityRiskActivity;
import com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.ScanGuardActivity;
import com.sisindia.ai.android.features.taskcheck.postcheck.postguardscan.PostGuardScanActivity;
import com.sisindia.ai.android.room.entities.CheckedPostCheckListEntity;
import com.sisindia.ai.android.utils.TimeUtils;

public class PostCheckActivity extends IopsBaseActivity {

    private ActivityPostCheckBinding binding;
    private PostCheckViewModel viewModel;

    public static Intent newIntent(Activity activity) {
        return new Intent(activity, PostCheckActivity.class);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_post_check;
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

                case NavigationConstants.OPEN_CHECK_GUARD_SCREEN:
                    openScanGuardScreen();
                    break;

                case NavigationConstants.OPEN_CHECK_REGISTER_SCREEN:
                    openRegisterCheck();
                    break;

                case NavigationConstants.ON_ADD_GRIEVANCE_CLICK:
                    openAddGrievanceScreen();
                    break;

                case NavigationConstants.ON_PENDING_GUARD_CLICK:
                    openPostGuardScanScreen();
                    break;

                case NavigationConstants.OPEN_CHECK_SECURITY_RISK_SCREEN:
                    openAddSecurityRiskScreen();
                    break;

                case NavigationConstants.ON_POST_CHECK_DONE:
                    onPostCheckDone();
                    break;

                case NavigationConstants.OPEN_CHECK_KIT_REQUEST_SCREEN:
                    openKitRequestScreen();
                    break;

                case NavigationConstants.ON_POST_CHECK_LIST_ITEM_CLICK:
                    CheckedPostCheckListEntity item = (CheckedPostCheckListEntity) message.obj;
                    openPostCheckListEditBottomScreen(item);
                    break;

                case NavigationConstants.ON_ADD_POST_CHECK_LIST_DONE:
                    viewModel.initViewModel();
                    break;
            }
        });
    }

    private void openPostCheckListEditBottomScreen(CheckedPostCheckListEntity item) {
        if (getSupportFragmentManager().findFragmentByTag(PostCheckListBottomSheetFragment.class.getSimpleName()) == null) {
            PostCheckListBottomSheetFragment.newInstance(item).show(getSupportFragmentManager(), PostCheckListBottomSheetFragment.class.getSimpleName());
        }
    }

    private void openKitRequestScreen() {
        startActivityForResult(AddKitRequestActivity.newIntent(this, 0), IntentRequestCodes.REQUEST_CODE_ADD_KIT_REQUEST);
    }

    private void onPostCheckDone() {
        setResult(RESULT_OK);
        this.finish();
    }

    private void openAddSecurityRiskScreen() {
        startActivityForResult(AddSecurityRiskActivity.newIntent(this), IntentRequestCodes.REQUEST_CODE_ON_ADD_SECURITY_RISK);
    }

    private void openPostGuardScanScreen() {
        startActivityForResult(PostGuardScanActivity.newIntent(this), IntentRequestCodes.REQUEST_CODE_ON_PENDING_GUARD);
    }

    private void openAddGrievanceScreen() {
        startActivityForResult(AddGrievancesActivity.newGrievanceIntent(this), IntentRequestCodes.REQUEST_CODE_ADD_GRIEVANCES);
    }

    private void openRegisterCheck() {
        startActivityForResult(RegisterCheckActivity.newIntent(this), IntentRequestCodes.REQUEST_CODE_REGISTER_CHECK);
    }

    private void openScanGuardScreen() {
        startActivityForResult(ScanGuardActivity.newIntent(this, false), IntentRequestCodes.REQUEST_CODE_SCAN_GUARD);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        openRegisterCheck();
        /*if (requestCode == RunTimePermissions.RC_CAMERA_PERMISSION) {
            if (RunTimePermissions.checkCameraPermissions(this)) {
                openRegisterCheck();
            } else {
                showToast("Please enable permissions from app settings!");
            }
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentRequestCodes.REQUEST_CODE_SCAN_GUARD:
            case IntentRequestCodes.REQUEST_CODE_ON_ADD_SECURITY_RISK:
            case IntentRequestCodes.REQUEST_CODE_ON_PENDING_GUARD:
            case IntentRequestCodes.REQUEST_CODE_ADD_KIT_REQUEST:
            case IntentRequestCodes.REQUEST_CODE_REGISTER_CHECK:
                viewModel.initViewModel();
                break;
        }
    }

    private void bindReviewInformationTime(int seconds) {
        binding.includeTimeSpent.tvTimeSpent.setText(TimeUtils.convertIntSecondsToHHMMSS(seconds));
    }

    @Override
    protected void onCreated() {
        setupToolBarForBackArrow(binding.tbPostCheck);
        viewModel.initViewModel();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void initViewBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutResource());
        binding.setVm(viewModel);
        binding.executePendingBindings();
    }

    @Override
    protected void initViewModel() {
        viewModel = (PostCheckViewModel) getAndroidViewModel(PostCheckViewModel.class);
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
