package com.sisindia.ai.android.features.taskcheck.postcheck.rewardfine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment;
import com.sisindia.ai.android.constants.IntentConstants;
import com.sisindia.ai.android.databinding.BottomSheetRewardFineSelectBinding;

import static com.sisindia.ai.android.constants.NavigationConstants.ON_REWARD_FINE_SELECTED;

public class RewardFineSelectBottomSheetFragment extends IopsBaseBottomSheetDialogFragment {

    private GuardAddRewardFineViewModel viewModel;
    private boolean isRewardRequest = false;

    @Override
    protected int getLayoutResource() {
        return R.layout.bottom_sheet_reward_fine_select;
    }

    public static RewardFineSelectBottomSheetFragment newInstance(boolean isRewardRequest) {
        RewardFineSelectBottomSheetFragment rewardFineFrag = new RewardFineSelectBottomSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IntentConstants.REWARD_PUNISHMENT, isRewardRequest);
        rewardFineFrag.setArguments(bundle);
        return rewardFineFrag;
    }

    @Override
    protected void extractBundle() {
        isRewardRequest = requireArguments().getBoolean(IntentConstants.REWARD_PUNISHMENT);
    }

    @Override
    protected void applyStyle() {

    }

    @Override
    protected void initViewModel() {
        viewModel = new ViewModelProvider(requireParentFragment(), viewModelFactory).get(GuardAddRewardFineViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        BottomSheetRewardFineSelectBinding binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            if (message.what == ON_REWARD_FINE_SELECTED) {
                this.dismissAllowingStateLoss();
            }
        });
    }

    @Override
    protected void onCreated() {
        viewModel.fetchOptionValues();
        viewModel.fetchReasons(isRewardRequest);
    }
}
