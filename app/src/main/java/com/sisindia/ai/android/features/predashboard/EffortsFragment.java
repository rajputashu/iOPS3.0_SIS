package com.sisindia.ai.android.features.predashboard;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;
import com.sisindia.ai.android.databinding.ViewPagerFragmentEffortsBinding;
import com.sisindia.ai.android.features.billcollection.BillCollectionDetailsOnSites;
import com.sisindia.ai.android.features.billsubmit.BillSubmissionCardsActivity;
import com.sisindia.ai.android.features.moninput.MonInputCardsActivity;

import static com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_ON_REFRESHING_PRE_DASHBOARD;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_MONINPUT_TASK;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_PRE_DASHBOARD_BILL_SUBMISSION_TASK;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_PRE_DASH_BOARD_BILL_COLLECTION;

public class EffortsFragment extends IopsBaseFragment {

    private PreDashboardViewModel parentViewModel;

    public static EffortsFragment newInstance() {
        return new EffortsFragment();
    }

    @Override
    protected void extractBundle() {

    }

    @Override
    protected void initViewModel() {
        parentViewModel = (PreDashboardViewModel) getAndroidViewModel(PreDashboardViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        ViewPagerFragmentEffortsBinding binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        binding.setVm(parentViewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            switch (message.what) {

                case OPEN_PRE_DASHBOARD_BILL_SUBMISSION_TASK:
                    startActivityForResult(new Intent(requireActivity(), BillSubmissionCardsActivity.class),
                            REQUEST_CODE_ON_REFRESHING_PRE_DASHBOARD);
                    break;

                case OPEN_MONINPUT_TASK:
                    startActivityForResult(new Intent(requireActivity(), MonInputCardsActivity.class),
                            REQUEST_CODE_ON_REFRESHING_PRE_DASHBOARD);
                    break;

                case OPEN_PRE_DASH_BOARD_BILL_COLLECTION:
                    startActivityForResult(new Intent(requireActivity(), BillCollectionDetailsOnSites.class),
                            REQUEST_CODE_ON_REFRESHING_PRE_DASHBOARD);
                    break;
            }
        });
    }

    @Override
    protected void onCreated() {
        parentViewModel.initEfforts();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.view_pager_fragment_efforts;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_ON_REFRESHING_PRE_DASHBOARD && resultCode == Activity.RESULT_OK)
            parentViewModel.initEfforts();
    }
}
