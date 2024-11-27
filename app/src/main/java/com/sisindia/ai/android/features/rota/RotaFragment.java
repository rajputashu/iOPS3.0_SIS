package com.sisindia.ai.android.features.rota;

import static com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_OPEN_REVIEW_INFORMATION;
import static com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_START_DAY_CHECK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_CONVEYANCE_SCREEN;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_REVIEW_INFORMATION;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayoutMediator;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;
import com.sisindia.ai.android.commons.CheckInStatus;
import com.sisindia.ai.android.constants.IntentConstants;
import com.sisindia.ai.android.constants.IntentRequestCodes;
import com.sisindia.ai.android.databinding.FragmentRotaBinding;
import com.sisindia.ai.android.features.conveyance.ConveyanceActivity;
import com.sisindia.ai.android.features.reviewinformation.ReviewInformationActivity;
import com.sisindia.ai.android.mlcore.ScanQRActivity;

import java.util.Objects;

public class RotaFragment extends IopsBaseFragment {

    private FragmentRotaBinding binding;
    private RotaViewModel viewModel;
    private final Handler refreshHandler = new Handler();

    public static RotaFragment newInstance() {
        return new RotaFragment();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_rota;
    }

    @Override
    protected void extractBundle() {

    }

    @Override
    protected void initViewModel() {
        viewModel = (RotaViewModel) getAndroidViewModel(RotaViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            if (message.what == OPEN_REVIEW_INFORMATION) {
                if (Objects.requireNonNull(viewModel.checkInStatus.get()) == CheckInStatus.DEFAULT.getStatus())
                    openCheckInOutQRScanner();
                else
                    openReviewInformationOrDcNcScreen();
            } else if (message.what == ON_CONVEYANCE_SCREEN)
                startActivity(ConveyanceActivity.newIntent(requireActivity()));
        });
    }

    private void openCheckInOutQRScanner() {
        startActivityForResult(ScanQRActivity.newIntentCheckInOut(requireActivity(), true), IntentRequestCodes.REQUEST_CODE_QR_SCANNER);
    }

    private void openReviewInformationOrDcNcScreen() {
        startActivityForResult(ReviewInformationActivity.newIntent(requireActivity()), REQUEST_CODE_OPEN_REVIEW_INFORMATION);
        /*if (Prefs.getInt(PrefConstants.COUNTRY_ID) == 1)
            startActivityForResult(ReviewInformationActivity.newIntent(requireActivity()), REQUEST_CODE_OPEN_REVIEW_INFORMATION);
        else
            startActivityForResult(DayNightCheckActivity.newIntent(requireActivity()), REQUEST_CODE_START_DAY_CHECK);*/
    }

    @Override
    protected void onCreated() {
        initTabLayout();
        viewModel.fetchRotaFromLocal();
        binding.swipeLayout.setOnRefreshListener(() -> {
            binding.swipeLayout.setRefreshing(false);
            if (binding.clpRota.getVisibility() == View.GONE) {
                viewModel.fetchRotaFromServer();
            }
        });

        //calling below API to fetch Today's and Month's Conveyance
        viewModel.fetchAOConveyanceSummary();
        /*if (Prefs.getInt(PrefConstants.COUNTRY_ID) == 1)
            viewModel.fetchAOConveyanceSummary();
        else
            binding.conveyanceLayout.setVisibility(View.GONE);*/
    }

    @SuppressLint("InflateParams")
    private void initTabLayout() {
        new TabLayoutMediator(binding.tlWeeklyRota, binding.vpWeeklyRota, (tab, position) -> {
            if (isAdded()) {
                View view = getLayoutInflater().inflate(R.layout.layout_tab_header_indicator, null);
                tab.setCustomView(view);
            }
        }).attach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OPEN_REVIEW_INFORMATION) {
            viewModel.fetchRotaFromLocal();
        } else if (requestCode == REQUEST_CODE_START_DAY_CHECK) {
            viewModel.fetchRotaFromLocal();
        } else if (requestCode == IntentRequestCodes.REQUEST_CODE_QR_SCANNER) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null && bundle.containsKey(IntentConstants.ON_SKIP_QR_SCANNED)) {
//                    viewModel.updateCheckInOutStatus(CheckInStatus.SKIPPED.getStatus());
//                    openReviewInformationOrDcNcScreen();
                    if (viewModel.validateLocationDistance()) {
                        viewModel.updateCheckInOutStatus(CheckInStatus.SKIPPED.getStatus());
                        if (bundle.containsKey(IntentConstants.ON_SKIP_QR_REASON))
                            viewModel.insertCheckInSkipRecord("", bundle.getString(IntentConstants.ON_SKIP_QR_REASON));
                        openReviewInformationOrDcNcScreen();
                    } else {
                        Toast.makeText(getActivity(), "You are not at site location", Toast.LENGTH_LONG).show();
                    }
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

                        if (isQRFound) {
                            viewModel.updateCheckInOutStatus(CheckInStatus.CHECKED_IN.getStatus());
                            viewModel.insertCheckInSkipRecord(scannedQRCode, "");
                            viewModel.startCheckInTimer();
                            new Handler().postDelayed(() -> {
                                viewModel.stopCheckInTimer();
                                openReviewInformationOrDcNcScreen();
                            }, 600);
                        } else
                            viewModel.showMessage("Please scan valid QR code at this site");

                    } else {
                        viewModel.showMessage("No QR Code is configured at this site");
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        refreshHandler.removeCallbacksAndMessages(null);
    }
}