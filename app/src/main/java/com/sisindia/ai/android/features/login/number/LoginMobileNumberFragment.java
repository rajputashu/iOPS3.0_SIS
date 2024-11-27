package com.sisindia.ai.android.features.login.number;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;
import com.sisindia.ai.android.databinding.FragmentLoginMobileNumberBinding;

public class LoginMobileNumberFragment extends IopsBaseFragment {

    private LoginMobileNumberViewModel viewModel;
    private FragmentLoginMobileNumberBinding binding;

    public static LoginMobileNumberFragment newInstance() {
        return new LoginMobileNumberFragment();
    }

    @Override
    protected void extractBundle() {
        if (getActivity() != null && getActivity().getWindow() != null)
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void initViewModel() {
        viewModel = (LoginMobileNumberViewModel) getAndroidViewModel(LoginMobileNumberViewModel.class);
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
    }

    @Override
    protected void onCreated() {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_login_mobile_number;
    }


}
