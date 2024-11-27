package com.sisindia.ai.android.features.splash;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;
import com.sisindia.ai.android.commons.YesNoDialogFragment;
import com.sisindia.ai.android.databinding.FragmentSplashBinding;
import com.sisindia.ai.android.features.uar.dialog.DialogListener;
import com.sisindia.ai.android.utils.installapk.DownloadController;

import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_VERSION_INFO_DIALOG;

public class SplashFragment extends IopsBaseFragment {

    private SplashViewModel viewModel;

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    private DownloadController downloadController;

    @Override
    protected void extractBundle() {
        if (getActivity() != null && getActivity().getWindow() != null)
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void initViewModel() {
        viewModel = (SplashViewModel) getAndroidViewModel(SplashViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentSplashBinding binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            if (message.what == OPEN_VERSION_INFO_DIALOG) {
//                String apkUrl = "https://csat.sisindia.com:8443/Iops2.0_APk/iOPS2.0_Ver-U(5.7).apk";
                String apkUrl = (String) message.obj;
                if (!apkUrl.isEmpty()) {
                    viewModel.loadingMsg.set("Downloading update of iOPS2.0...");
                    downloadController = new DownloadController(requireActivity(), apkUrl);
                    openVersionDialog();
                } else {
                    viewModel.checkUserState();
                }
            }
        });
    }

    @Override
    protected void onCreated() {
//        viewModel.checkUserState();
        viewModel.checkAppVersion();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_splash;
    }

    private void openVersionDialog() {
        YesNoDialogFragment fragment = YesNoDialogFragment.newSingleButtonInstance("Warning!\n\nYou are using an old version of an iOPS2.0\nNew update is available.");
        fragment.setCancelable(false);
        fragment.initDialogListener(new DialogListener() {
            @Override
            public void onCrossButtonClick() {

            }

            @Override
            public void onViewAllPOAClick() {

            }

            @Override
            public void onYesButtonClicked() {
                fragment.dismissAllowingStateLoss();
                downloadController.enqueueDownload();
            }

            @Override
            public void onNoButtonClicked() {

            }
        });
        fragment.show(requireActivity().getSupportFragmentManager(), YesNoDialogFragment.class.getSimpleName());
    }
}
