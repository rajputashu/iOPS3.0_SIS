package com.sisindia.ai.android.features.kpi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;

public class MyKpiFragment extends IopsBaseFragment {

    private MyKpiViewModel viewModel;

    public static MyKpiFragment newInstance() {
        return new MyKpiFragment();
    }

    @Override
    protected void extractBundle() {

    }

    @Override
    protected void initViewModel() {
        viewModel = (MyKpiViewModel) getAndroidViewModel(MyKpiViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        com.sisindia.ai.android.databinding.FragmentMyKpiBinding binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {

    }

    @Override
    protected void onCreated() {
        viewModel.updateKPIAdapter();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_my_kpi;
    }
}
