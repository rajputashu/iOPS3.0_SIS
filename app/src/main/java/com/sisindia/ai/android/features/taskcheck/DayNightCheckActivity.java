package com.sisindia.ai.android.features.taskcheck;

import static com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_OPEN_CLIENT_HAND_SHAKE;
import static com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_OPEN_STRENGTH_CHECK;
import static com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_POST_CHECK;
import static com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_QR_SCANNER;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_SITE_POST_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_LIVE_QR_SCANNER_SCREEN;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_PRE_POST_CHECK_SCREEN;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_STRENGTH_CHECK_SCREEN;
import static com.sisindia.ai.android.constants.NavigationConstants.TASK_TIMER_TIK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseActivity;
import com.sisindia.ai.android.commons.YesNoDialogFragment;
import com.sisindia.ai.android.constants.IntentConstants;
import com.sisindia.ai.android.constants.IntentRequestCodes;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.databinding.ActivityDayNightCheckBinding;
import com.sisindia.ai.android.features.taskcheck.clienthandshake.ClientHandshakeActivity;
import com.sisindia.ai.android.features.taskcheck.postcheck.PostCheckActivity;
import com.sisindia.ai.android.features.taskcheck.postcheck.postlist.SitePostListBottomSheetFragment;
import com.sisindia.ai.android.features.taskcheck.strengthcheck.StrengthCheckActivity;
import com.sisindia.ai.android.features.uar.dialog.DialogListener;
import com.sisindia.ai.android.mlcore.ScanQRActivity;
import com.sisindia.ai.android.room.entities.CheckedSiteCheckListEntity;
import com.sisindia.ai.android.utils.TimeUtils;

import java.util.Objects;


public class DayNightCheckActivity extends IopsBaseActivity {

    private ActivityDayNightCheckBinding binding;
    private DayNightCheckViewModel viewModel;

    public static Intent newIntent(Activity activity) {
        return new Intent(activity, DayNightCheckActivity.class);
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

                case OPEN_LIVE_QR_SCANNER_SCREEN:
                    openLiveQRScreen();
                    break;

                case OPEN_PRE_POST_CHECK_SCREEN:
                    if (getSupportFragmentManager().findFragmentByTag(SitePostListBottomSheetFragment.class.getSimpleName()) == null) {
                        SitePostListBottomSheetFragment.newInstance().show(getSupportFragmentManager(),
                                SitePostListBottomSheetFragment.class.getSimpleName());
                    }
                    break;

                case ON_SITE_POST_CLICK:
                    startActivityForResult(PostCheckActivity.newIntent(this), IntentRequestCodes.REQUEST_CODE_POST_CHECK);
                    break;

                case OPEN_STRENGTH_CHECK_SCREEN:
                    startActivityForResult(StrengthCheckActivity.newIntent(this), REQUEST_CODE_OPEN_STRENGTH_CHECK);
                    break;

                case NavigationConstants.OPEN_CLIENT_HANDSHAKE_SCREEN:
                    startActivityForResult(ClientHandshakeActivity.newIntent(this), REQUEST_CODE_OPEN_CLIENT_HAND_SHAKE);
                    break;

                case NavigationConstants.ON_SITE_CHECK_LIST_ITEM_CLICK:
                    CheckedSiteCheckListEntity item = (CheckedSiteCheckListEntity) message.obj;
                    if (getSupportFragmentManager().findFragmentByTag(SiteCheckListBottomSheetFragment.class.getSimpleName()) == null) {
                        SiteCheckListBottomSheetFragment.newInstance(item).show(getSupportFragmentManager(),
                                SiteCheckListBottomSheetFragment.class.getSimpleName());
                    }
                    break;

                case NavigationConstants.ON_ADD_SITE_CHECK_LIST_DONE:
                    viewModel.initViewModel();
                    break;

                case NavigationConstants.OPEN_SCANNER_FOR_CHECKOUT:
                    startActivityForResult(ScanQRActivity.newIntentCheckInOut(this, false), IntentRequestCodes.REQUEST_CODE_QR_SCANNER);
                    break;

                case NavigationConstants.ON_DAY_NIGHT_CHECK_DONE:
                    setResult(RESULT_OK);
                    this.finish();
                    break;
            }
        });
    }

    private void openLiveQRScreen() {
        SitePostListBottomSheetFragment fragment = (SitePostListBottomSheetFragment) getSupportFragmentManager()
                .findFragmentByTag(SitePostListBottomSheetFragment.class.getSimpleName());
        if (fragment != null) {
            fragment.dismissAllowingStateLoss();
        }
        startActivityForResult(ScanQRActivity.newIntent(this, true), IntentRequestCodes.REQUEST_CODE_OPEN_POST_SCAN);
    }

    private void bindReviewInformationTime(int seconds) {
        binding.includeTimeSpent.tvTimeSpent.setText(TimeUtils.convertIntSecondsToHHMMSS(seconds));
    }

    @Override
    protected void onCreated() {
        setupToolBarForBackArrow(binding.tbDayNightCheck);
        viewModel.initViewModel();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag(YesNoDialogFragment.class.getSimpleName()) == null) {
            YesNoDialogFragment fragment = YesNoDialogFragment.newInstance("Do you want to exit?");
            fragment.setCancelable(false);
            fragment.initDialogListener(new DialogListener() {
                @Override
                public void onCrossButtonClick() {
                    fragment.dismissAllowingStateLoss();
                }

                @Override
                public void onViewAllPOAClick() {

                }

                @Override
                public void onYesButtonClicked() {
                    DayNightCheckActivity.super.onBackPressed();
                }

                @Override
                public void onNoButtonClicked() {
                    fragment.dismissAllowingStateLoss();
                }
            });
            fragment.show(getSupportFragmentManager(), YesNoDialogFragment.class.getSimpleName());
        }
    }

    @Override
    protected void initViewBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutResource());
        binding.setVm(viewModel);
        binding.executePendingBindings();
    }

    @Override
    protected void initViewModel() {
        viewModel = (DayNightCheckViewModel) getAndroidViewModel(DayNightCheckViewModel.class);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_day_night_check;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentRequestCodes.REQUEST_CODE_OPEN_POST_SCAN:
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null &&
                        data.getExtras().containsKey(IntentConstants.ON_QR_SCANNED)) {
                    String qrRawData = data.getExtras().getString(IntentConstants.ON_QR_SCANNED);
                    viewModel.onSitePostQrScanned(qrRawData);
                } else showToast("Error in Qr Scan.. Please try again");
                break;

            case REQUEST_CODE_OPEN_STRENGTH_CHECK:
            case REQUEST_CODE_OPEN_CLIENT_HAND_SHAKE:
            case REQUEST_CODE_POST_CHECK:
                viewModel.initViewModel();
                break;
            case REQUEST_CODE_QR_SCANNER:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null && bundle.containsKey(IntentConstants.ON_SKIP_QR_SCANNED)) {
                        if (bundle.containsKey(IntentConstants.ON_SKIP_QR_REASON))
                            viewModel.updateCheckOutSkipRecord("", bundle.getString(IntentConstants.ON_SKIP_QR_REASON));
                    } else if (bundle != null && bundle.containsKey(IntentConstants.ON_QR_SCANNED)) {
                        if (viewModel.qrCodesList.get() != null && !Objects.requireNonNull(viewModel.qrCodesList.get()).isEmpty()) {
                            boolean isQRFound = false;
                            String scannedQRCode = bundle.getString(IntentConstants.ON_QR_SCANNED, "");
                            for (String qrCode : Objects.requireNonNull(viewModel.qrCodesList.get())) {
                                if (qrCode.equals(scannedQRCode)) {
                                    isQRFound = true;
                                    break;
                                }
                            }

                            if (isQRFound)
                                viewModel.updateCheckOutSkipRecord(scannedQRCode, "");
                            else
                                showToast("Please scan valid QR code at this site");

                        } else {
                            showToast("QR Codes are not configured at this site");
                        }
                    }
                }
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
