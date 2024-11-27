package com.sisindia.ai.android.features.login.otp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;
import com.sisindia.ai.android.databinding.FragmentEnterOtpBinding;

import timber.log.Timber;

import static com.sisindia.ai.android.constants.NavigationConstants.ON_UPDATING_LOADING_TIME;
import static com.sisindia.ai.android.receivers.OtpSmsReceiver.BIND_OTP_TO_VIEW;

public class EnterOtpFragment extends IopsBaseFragment {

    private EnterOtpViewModel viewModel;
    private FragmentEnterOtpBinding binding;

    private BroadcastReceiver smsBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (binding != null && intent != null && intent.getExtras() != null) {
                binding.otpView.setText(intent.getStringExtra(BIND_OTP_TO_VIEW));
            }
        }
    };

    public static EnterOtpFragment newInstance() {
        return new EnterOtpFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null)
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(smsBroadcastReceiver, new IntentFilter(BIND_OTP_TO_VIEW));
    }


    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() != null)
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(smsBroadcastReceiver);
    }

    @Override
    protected void extractBundle() {

    }

    @Override
    protected void initViewModel() {
        viewModel = (EnterOtpViewModel) getAndroidViewModel(EnterOtpViewModel.class);
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
            if (message.what == ON_UPDATING_LOADING_TIME) {
//                binding.loadingPercentageView.setPercentage(message.arg1);
                binding.loadingPercentageView.setProgress(message.arg1);
                binding.loadingPercentageView.setFinishedStrokeColor(R.color.colorLightRed);
                binding.loadingPercentageView.setUnfinishedStrokeColor(R.color.colorRed_30opct);
                binding.loadingPercentageView.setBottomText("SYNCING");
                binding.loadingPercentageView.setBottomTextSize(30f);
            }
        });
    }

    @Override
    protected void onCreated() {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_enter_otp;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.cancelTimer();
    }
}
