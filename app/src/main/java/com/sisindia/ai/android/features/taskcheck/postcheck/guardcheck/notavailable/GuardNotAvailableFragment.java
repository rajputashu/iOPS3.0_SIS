package com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.notavailable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;
import com.sisindia.ai.android.databinding.FragmentGuardNotAvailableBinding;

public class GuardNotAvailableFragment extends IopsBaseFragment {

    private GuardNotAvailableViewModel viewModel;
    private FragmentGuardNotAvailableBinding binding;

    public static Fragment newInstance() {
        return new GuardNotAvailableFragment();
    }

    @Override
    protected void extractBundle() {

    }

    @Override
    protected void initViewModel() {
        viewModel = (GuardNotAvailableViewModel) getAndroidViewModel(GuardNotAvailableViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        binding = (FragmentGuardNotAvailableBinding) bindFragmentView(getLayoutResource(), inflater, container);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {

    }

    @Override
    protected void onCreated() {
        viewModel.initViewModel();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_guard_not_available;
    }
}
