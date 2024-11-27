package com.sisindia.ai.android.features.timline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;
import com.sisindia.ai.android.databinding.FragmentYesterdayTimelineBinding;

public class YesterDayTimeLineFragment extends IopsBaseFragment {

    private YesterDayTimeLineViewModel viewModel;

    public static YesterDayTimeLineFragment newInstance() {
        return new YesterDayTimeLineFragment();
    }

    @Override
    protected void extractBundle() {
    }

    @Override
    protected void initViewModel() {
        viewModel = (YesterDayTimeLineViewModel) getAndroidViewModel(YesterDayTimeLineViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentYesterdayTimelineBinding binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
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
        return R.layout.fragment_yesterday_timeline;
    }
}
