package com.sisindia.ai.android.features.timline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;
import com.sisindia.ai.android.databinding.FragmentTodayTimelineBinding;

public class TodayTimeLineFragment extends IopsBaseFragment {

    private TodayTimeLineViewModel viewModel;

    public static TodayTimeLineFragment newInstance() {
        return new TodayTimeLineFragment();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_today_timeline;
    }

    @Override
    protected void extractBundle() {

    }

    @Override
    protected void initViewModel() {
        viewModel = (TodayTimeLineViewModel) getAndroidViewModel(TodayTimeLineViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentTodayTimelineBinding binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
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
}
