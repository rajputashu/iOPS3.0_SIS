package com.sisindia.ai.android.features.taskcheck.postcheck.rewardfine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;
import com.sisindia.ai.android.databinding.FragmentGuardAddRewardFineBinding;

import static com.sisindia.ai.android.constants.NavigationConstants.ON_ADD_FINE_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_ADD_REWARD_CLICK;

public class GuardAddRewardFineFragment extends IopsBaseFragment {

    private GuardAddRewardFineViewModel viewModel;
    private FragmentGuardAddRewardFineBinding binding;

    public static GuardAddRewardFineFragment newInstance() {
        return new GuardAddRewardFineFragment();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_guard_add_reward_fine;
    }

    @Override
    protected void extractBundle() {

    }

    @Override
    protected void initViewModel() {
        viewModel = (GuardAddRewardFineViewModel) getAndroidViewModel(GuardAddRewardFineViewModel.class);
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
        liveData.observe(this, message -> {
            switch (message.what) {
                case ON_ADD_REWARD_CLICK:
                    openAddRewardFineBottomSheet(true);
                    break;
                case ON_ADD_FINE_CLICK:
                    openAddRewardFineBottomSheet(false);
                    break;
            }
        });
    }

    private void openAddRewardFineBottomSheet(boolean isReward) {
        if (getChildFragmentManager().findFragmentByTag(RewardFineSelectBottomSheetFragment.class.getSimpleName()) == null) {
            RewardFineSelectBottomSheetFragment fragment = RewardFineSelectBottomSheetFragment.newInstance(isReward);
            fragment.show(getChildFragmentManager(), RewardFineSelectBottomSheetFragment.class.getSimpleName());
//            fragment.setCancelable(false);
        }
    }

    @Override
    protected void onCreated() {
        viewModel.initViewModel();
        /*if (Prefs.getInt(PrefConstants.COUNTRY_ID) != 1) {
            binding.addRewardButton.setVisibility(View.GONE);
            binding.addFineButton.setVisibility(View.GONE);
            binding.rewardFineMessage.setVisibility(View.GONE);
        }*/
    }
}
