package com.sisindia.ai.android.features.taskcheck.postcheck.summary;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseActivity;
import com.sisindia.ai.android.constants.IntentRequestCodes;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.databinding.ActivityGuardSummaryBinding;
import com.sisindia.ai.android.features.addgrievances.AddGrievancesActivity;
import com.sisindia.ai.android.features.addkitrequest.AddKitRequestActivity;

public class GuardSummaryActivity extends IopsBaseActivity {

    private ActivityGuardSummaryBinding binding;
    private GuardSummaryViewModel viewModel;

    public static Intent newIntent(Activity activity) {
        return new Intent(activity, GuardSummaryActivity.class);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_guard_summary;
    }

    @Override
    protected void extractBundle() {

    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            switch (message.what) {

                case NavigationConstants.ON_ADD_GRIEVANCE_CLICK:
                    openAddGrievanceScreen(message);
                    break;

                case NavigationConstants.ON_GUARD_CHECK_SUCCESS:
                    onGuardCheckSuccess();
                    break;

                case NavigationConstants.ON_ADD_KIT_REQUEST_CLICK:
                    openAddKitRequestScreen(message.arg1);
                    break;
            }
        });
    }

    private void onGuardCheckSuccess() {
        setResult(Activity.RESULT_OK);
        this.finish();
    }

    private void openAddKitRequestScreen(int employeeId) {
        startActivityForResult(AddKitRequestActivity.newIntent(this, employeeId), IntentRequestCodes.REQUEST_CODE_ADD_KIT_REQUEST);
    }

    private void openAddGrievanceScreen(Message message) {
        startActivityForResult(AddGrievancesActivity.newIntentForSelectedGuard(this, message.arg1), IntentRequestCodes.REQUEST_CODE_ADD_GRIEVANCES);
    }

    @Override
    protected void onCreated() {
        setupToolBarForBackArrow(binding.tbGuardSummary);
        viewModel.initViewModel();
    }

    @Override
    protected void initViewBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutResource());
        binding.setVm(viewModel);
        binding.executePendingBindings();
    }

    @Override
    protected void initViewModel() {
        viewModel = (GuardSummaryViewModel) getAndroidViewModel(GuardSummaryViewModel.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case IntentRequestCodes.REQUEST_CODE_ADD_GRIEVANCES:
            case IntentRequestCodes.REQUEST_CODE_ADD_KIT_REQUEST:
                viewModel.initViewModel();
                break;
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