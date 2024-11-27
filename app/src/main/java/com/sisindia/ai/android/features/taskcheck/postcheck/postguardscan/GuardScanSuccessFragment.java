package com.sisindia.ai.android.features.taskcheck.postcheck.postguardscan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;
import com.sisindia.ai.android.databinding.FragmentGuardScanSuccessBinding;

public class GuardScanSuccessFragment extends IopsBaseFragment {

    private GuardCheckScanSuccessViewModel viewModel;

    public static GuardScanSuccessFragment newInstance() {
        return new GuardScanSuccessFragment();
    }

    @Override
    protected void extractBundle() {

    }

    @Override
    protected void initViewModel() {
        viewModel = (GuardCheckScanSuccessViewModel) getAndroidViewModel(GuardCheckScanSuccessViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentGuardScanSuccessBinding binding = (FragmentGuardScanSuccessBinding) bindFragmentView(getLayoutResource(), inflater, container);
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
        return R.layout.fragment_guard_scan_success;
    }
}
