package com.sisindia.ai.android.features.predashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;
import com.sisindia.ai.android.databinding.ViewPagerFragmentResultsBinding;

import timber.log.Timber;

public class ResultsFragment extends IopsBaseFragment {

    private PreDashboardViewModel parentViewModel;

    public static ResultsFragment newInstance() {
        return new ResultsFragment();
    }

    @Override
    protected void extractBundle() {

    }

    @Override
    protected void initViewModel() {
        if (getParentFragment() != null) {
            parentViewModel = new ViewModelProvider(getParentFragment(), viewModelFactory).get(PreDashboardViewModel.class);
        }
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        ViewPagerFragmentResultsBinding binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        binding.setVm(parentViewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {

    }

    @Override
    protected void onCreated() {
        Timber.e("");
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.view_pager_fragment_results;
    }
}
