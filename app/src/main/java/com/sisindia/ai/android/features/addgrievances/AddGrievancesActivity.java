package com.sisindia.ai.android.features.addgrievances;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import com.droidcommons.permissions.RunTimePermissions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseActivity;
import com.sisindia.ai.android.commons.audiorecord.AudioRecordingBottomSheetFragment;
import com.sisindia.ai.android.databinding.ActivityAddGrievancesBinding;
import com.sisindia.ai.android.room.entities.CheckedGuardEntity;
import com.sisindia.ai.android.utils.TimeUtils;

import static com.sisindia.ai.android.constants.NavigationConstants.ON_ADD_AUDIO_CLIP_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_GRIEVANCE_ADDED_CONTINUE;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_GUARD_CHECK_GRIEVANCE_ADDED;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_OPEN_ADD_GRIEVANCE_DETAIL;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_OPEN_ADD_GRIEVANCE_SELECT_GUARD;
import static com.sisindia.ai.android.constants.NavigationConstants.TASK_TIMER_TIK;


public class AddGrievancesActivity extends IopsBaseActivity {

    private ActivityAddGrievancesBinding binding;
    private AddGrievancesViewModel viewModel;

    public static Intent newIntentForSelectedGuard(Activity activity, int employeeId) {
        Intent intent = new Intent(activity, AddGrievancesActivity.class);
        intent.putExtra(CheckedGuardEntity.class.getSimpleName(), employeeId);
        return intent;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_grievances;
    }

    public static Intent newGrievanceIntent(Activity activity) {
        return new Intent(activity, AddGrievancesActivity.class);
    }

    @Override
    protected void extractBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(CheckedGuardEntity.class.getSimpleName())) {
            int employeeId = bundle.getInt(CheckedGuardEntity.class.getSimpleName());
            viewModel.initViewModel(employeeId);
            viewModel.setBreadCum(employeeId);
        } else {
            viewModel.initViewModel(0);
            viewModel.setBreadCum(0);
        }
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            switch (message.what) {

                case TASK_TIMER_TIK:
                    bindReviewInformationTime(message.arg1);
                    break;

                case ON_ADD_AUDIO_CLIP_CLICK:
                    openAddAudioClipBottomSheet();
                    break;

                case ON_OPEN_ADD_GRIEVANCE_SELECT_GUARD:
                    openSelectGuardForGrievance();
                    break;

                case ON_OPEN_ADD_GRIEVANCE_DETAIL:
                    openCreateGrievanceForGuard(message.arg1);
                    break;

                case ON_GUARD_CHECK_GRIEVANCE_ADDED:
                    openGrievanceAddedDialog(message.arg1);
                    break;

                case ON_GRIEVANCE_ADDED_CONTINUE:
                    onAddedGrievanceContinue();
                    break;
            }
        });
    }

    private void onAddedGrievanceContinue() {
        setResult(Activity.RESULT_OK);
        this.finish();
    }

    private void openGrievanceAddedDialog(int grievanceId) {
        if (getSupportFragmentManager().findFragmentByTag(AddedGrievanceDialogFragment.class.getSimpleName()) == null) {
            AddedGrievanceDialogFragment fragment = AddedGrievanceDialogFragment.newInstance(grievanceId);
            fragment.setCancelable(false);
            fragment.show(getSupportFragmentManager(), AddedGrievanceDialogFragment.class.getSimpleName());
        }
    }

    private void openSelectGuardForGrievance() {
        viewModel.setBreadCum(0);
        loadFragment(R.id.flAddGrievance, SelectGuardForAddGrievanceFragment.newInstance(), FRAGMENT_REPLACE, false);
    }

    private void openCreateGrievanceForGuard(int employeeId) {
        viewModel.setBreadCum(employeeId);
        loadFragment(R.id.flAddGrievance, GuardGrievanceDetailFragment.newInstance(employeeId), FRAGMENT_REPLACE, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void openAddAudioClipBottomSheet() {
        if (!RunTimePermissions.checkAudioRecordPermissions(this)) {
            requestPermissions(RunTimePermissions.getAudioRecordPermissions(), RunTimePermissions.RC_AUDIO_RECORD);
            return;
        }

        if (getSupportFragmentManager().findFragmentByTag(AudioRecordingBottomSheetFragment.class.getSimpleName()) == null) {
            BottomSheetDialogFragment fragment = AudioRecordingBottomSheetFragment.newInstance();
            fragment.setCancelable(false);
            fragment.show(getSupportFragmentManager(), AudioRecordingBottomSheetFragment.class.getSimpleName());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RunTimePermissions.RC_AUDIO_RECORD) {
            if (RunTimePermissions.checkAudioRecordPermissions(this))
                openAddAudioClipBottomSheet();
            else
                showToast("Please enable permissions from app settings..");
        }
    }

    @Override
    protected void onCreated() {
        setupToolBarForBackArrow(binding.tbAddGrievances);
    }

    private void bindReviewInformationTime(int seconds) {
        binding.includeTimeSpent.tvTimeSpent.setText(TimeUtils.convertIntSecondsToHHMMSS(seconds));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_add_grievances, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    protected void initViewBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutResource());
        binding.setVm(viewModel);
        binding.executePendingBindings();

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag(AudioRecordingBottomSheetFragment.class.getSimpleName()) == null) {
            super.onBackPressed();
        }
    }

    @Override
    protected void initViewModel() {
        viewModel = (AddGrievancesViewModel) getAndroidViewModel(AddGrievancesViewModel.class);
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
