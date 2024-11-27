package com.sisindia.ai.android.features.taskcheck.postcheck.photoevaluation;

import static com.sisindia.ai.android.constants.NavigationConstants.ON_GUARD_EVALUATION_EDITED;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment;
import com.sisindia.ai.android.constants.IntentConstants;
import com.sisindia.ai.android.databinding.BottomSheetGuardTurnOutBinding;
import com.sisindia.ai.android.uimodels.GuardTurnOutResult;

public class GuardTurnOutEditBottomSheetFragment extends IopsBaseBottomSheetDialogFragment {

    private GuardTurnOutViewModel viewModel;

    public static GuardTurnOutEditBottomSheetFragment newInstance() {
        return new GuardTurnOutEditBottomSheetFragment();
    }

    /*public static GuardTurnOutEditBottomSheetFragment newInstance(List<GuardTurnOutResult.GuardTurnoutModel> turnoutModelList) {
        GuardTurnOutEditBottomSheetFragment fragment = new GuardTurnOutEditBottomSheetFragment();

        Bundle args = new Bundle();
//        args.putParcelable("turnoutModelList", turnoutModelList); // Using ArrayList because it's Parcelable
        args.putSerializable(IntentConstants.TURN_OUT_LIST, (Serializable) turnoutModelList);
        fragment.setArguments(args);

        return fragment;
    }*/

    public static GuardTurnOutEditBottomSheetFragment newInstance(GuardTurnOutResult turnOutResult) {
        GuardTurnOutEditBottomSheetFragment fragment = new GuardTurnOutEditBottomSheetFragment();
        Bundle args = new Bundle();
        args.putSerializable(IntentConstants.TURN_OUT_RESULT, turnOutResult);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void extractBundle() {
        if (getArguments() != null) {
            /*List<GuardTurnOutResult.GuardTurnoutModel> turnoutModelList =
                    (List<GuardTurnOutResult.GuardTurnoutModel>) getArguments().getSerializable(IntentConstants.TURN_OUT_RESULT);

            if (turnoutModelList != null) {
                viewModel.receivedTurnOutList = turnoutModelList;
                Timber.e("LIstSize of Turnout %d",turnoutModelList.size());
            }*/

            GuardTurnOutResult result =
                    (GuardTurnOutResult) getArguments().getSerializable(IntentConstants.TURN_OUT_RESULT);

            if (result != null) {
                viewModel.receivedTurnOutList = result.turnOutResult;
                viewModel.receivedMLTurnOutList = result.mlTurnOutResult;
            }
        }
    }

    @Override
    protected void applyStyle() {
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }

    @Override
    protected void initViewModel() {
        viewModel = (GuardTurnOutViewModel) getAndroidViewModel(GuardTurnOutViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        BottomSheetGuardTurnOutBinding binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            if (message.what == ON_GUARD_EVALUATION_EDITED) {
                dismissAllowingStateLoss();
            }
        });
    }

    @Override
    protected void onCreated() {
        viewModel.initViewModel();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.bottom_sheet_guard_turn_out;
    }
}
