package com.sisindia.ai.android.features.taskcheck.postcheck.postguardscan;

import static com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_GUARD_PHOTO_EVALUATION;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_ADD_SIGNATURE_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_GUARD_TURN_OUT_EDIT;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_PHOTO_EVALUATION_DONE;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_TAKE_GUARD_PHOTO_FOR_EVALUATION;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_GUARD_SUMMARY;
import static com.sisindia.ai.android.constants.NavigationConstants.TASK_TIMER_TIK;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseActivity;
import com.sisindia.ai.android.constants.IntentRequestCodes;
import com.sisindia.ai.android.databinding.ActivityPostGuardScanBinding;
import com.sisindia.ai.android.features.imagecapture.CaptureImageActivityV2;
import com.sisindia.ai.android.features.taskcheck.postcheck.photoevaluation.GuardPhotoEvaluationResultFragment;
import com.sisindia.ai.android.features.taskcheck.postcheck.photoevaluation.GuardTurnOutEditBottomSheetFragment;
import com.sisindia.ai.android.features.taskcheck.postcheck.rewardfine.GuardAddRewardFineFragment;
import com.sisindia.ai.android.features.taskcheck.postcheck.signature.AddSignatureFragment;
import com.sisindia.ai.android.features.taskcheck.postcheck.summary.GuardSummaryActivity;
import com.sisindia.ai.android.room.entities.AttachmentEntity;
import com.sisindia.ai.android.uimodels.GuardTurnOutResult;
import com.sisindia.ai.android.utils.TimeUtils;

import org.parceler.Parcels;

import java.util.List;

public class PostGuardScanActivity extends IopsBaseActivity {

    private ActivityPostGuardScanBinding binding;
    private PostGuardScanViewModel viewModel;

    public static Intent newIntent(Activity activity) {
        return new Intent(activity, PostGuardScanActivity.class);
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

                case ON_TAKE_GUARD_PHOTO_FOR_EVALUATION:
                    openGuardPhotoEvaluation();
                    break;

                case ON_GUARD_TURN_OUT_EDIT:
//                    List<GuardTurnOutResult.GuardTurnoutModel> list= (List<GuardTurnOutResult.GuardTurnoutModel>) message.obj;
                    GuardTurnOutResult turnOutResult = (GuardTurnOutResult) message.obj;
//                    openGuardTurnOutScreen(list);
                    openGuardTurnOutScreen(turnOutResult);
                    break;

                case ON_PHOTO_EVALUATION_DONE:
                    openGuardAddRewardFineScreen();
                    break;

                case ON_ADD_SIGNATURE_CLICK:
                    openAddSignatureScreen();
                    break;

                case OPEN_GUARD_SUMMARY:
                    openGuardSummary();
                    break;
            }
        });
    }

    private void openGuardSummary() {
        startActivityForResult(GuardSummaryActivity.newIntent(this), OPEN_GUARD_SUMMARY);
        finish();
    }

   /* private void openGuardTurnOutScreen(List<GuardTurnOutResult.GuardTurnoutModel> list) {
        if (getSupportFragmentManager().findFragmentByTag(GuardTurnOutEditBottomSheetFragment.class.getSimpleName()) == null) {
            GuardTurnOutEditBottomSheetFragment fragment =
                    GuardTurnOutEditBottomSheetFragment.newInstance(list);
            fragment.show(getSupportFragmentManager(), GuardTurnOutEditBottomSheetFragment.class.getSimpleName());
        }
    }*/

    private void openGuardTurnOutScreen(GuardTurnOutResult turnOutResult) {
        if (getSupportFragmentManager().findFragmentByTag(GuardTurnOutEditBottomSheetFragment.class.getSimpleName()) == null) {
            GuardTurnOutEditBottomSheetFragment fragment =
                    GuardTurnOutEditBottomSheetFragment.newInstance(turnOutResult);
            fragment.show(getSupportFragmentManager(), GuardTurnOutEditBottomSheetFragment.class.getSimpleName());
        }
    }

    private void openGuardPhotoEvaluation() {
        AttachmentEntity item = new AttachmentEntity(AttachmentEntity.AttachmentSourceType.GUARD_FULL_PHOTO);
        startActivityForResult(CaptureImageActivityV2.newIntent(this, item), IntentRequestCodes.REQUEST_CODE_GUARD_PHOTO_EVALUATION);
    }

    private void bindReviewInformationTime(int seconds) {
        binding.includeTimeSpent.tvTimeSpent.setText(TimeUtils.convertIntSecondsToHHMMSS(seconds));
    }

    @Override
    protected void onCreated() {
        setupToolBarForBackArrow(binding.tbPostGuardScan);
        openGuardCheckScanResultFragment();
        viewModel.initViewModel();
    }

    private void openGuardCheckScanResultFragment() {
        loadFragment(R.id.flPostGuardScan, GuardScanSuccessFragment.newInstance(), FRAGMENT_REPLACE, false);
        /*if (Prefs.getInt(PrefConstants.COUNTRY_ID) == 1)
            loadFragment(R.id.flPostGuardScan, GuardScanSuccessFragment.newInstance(), FRAGMENT_REPLACE, false);
        else {
            binding.flPostGuardScan.setVisibility(View.GONE);
            binding.photoMsgAndTakePhotoButton.setVisibility(View.VISIBLE);
        }*/
    }

    private void openGuardAddRewardFineScreen() {
        viewModel.onPhotoEvaluationDone();
        loadFragment(R.id.flPostGuardScan, GuardAddRewardFineFragment.newInstance(), FRAGMENT_REPLACE, false);
    }

    private void openAddSignatureScreen() {
        loadFragment(R.id.flPostGuardScan, AddSignatureFragment.newInstance(), FRAGMENT_REPLACE, false);
    }

    private void onGuardPhotoTakenForEvaluation(AttachmentEntity data) {
        loadFragment(R.id.flPostGuardScan, GuardPhotoEvaluationResultFragment.newInstance(data), FRAGMENT_REPLACE, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GUARD_PHOTO_EVALUATION) {
            if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null && data.getExtras().containsKey(AttachmentEntity.class.getSimpleName())) {
                /*if (Prefs.getInt(PrefConstants.COUNTRY_ID) != 1) {
                    binding.flPostGuardScan.setVisibility(View.VISIBLE);
                    binding.photoMsgAndTakePhotoButton.setVisibility(View.GONE);
                }*/
                AttachmentEntity attachment = Parcels.unwrap(data.getExtras().getParcelable(AttachmentEntity.class.getSimpleName()));
                onGuardPhotoTakenForEvaluation(attachment);
            } else {
                showToast("Unable to Capture Image");
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_post_guard_scan, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    protected void initViewBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutResource());
        binding.setVm(viewModel);
        binding.executePendingBindings();
    }

    @Override
    protected void initViewModel() {
        viewModel = (PostGuardScanViewModel) getAndroidViewModel(PostGuardScanViewModel.class);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_post_guard_scan;
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
