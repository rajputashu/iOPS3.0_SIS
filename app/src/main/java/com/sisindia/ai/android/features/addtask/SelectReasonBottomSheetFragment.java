package com.sisindia.ai.android.features.addtask;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.databinding.BottomSheetSelectReasonBinding;

public class SelectReasonBottomSheetFragment extends IopsBaseBottomSheetDialogFragment {

    private BottomSheetSelectReasonBinding binding;
    private SelectReasonViewModel viewModel;

    public static SelectReasonBottomSheetFragment newInstance() {
        return new SelectReasonBottomSheetFragment();
    }

    @Override
    protected void extractBundle() {

    }

    @Override
    protected void applyStyle() {
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogThemeWithKeyboard);
    }

    @Override
    protected void initViewModel() {
        viewModel = (SelectReasonViewModel) getAndroidViewModel(SelectReasonViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        binding = (BottomSheetSelectReasonBinding) bindFragmentView(getLayoutResource(), inflater, container);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            switch (message.what) {

                case NavigationConstants.ON_CREATE_TASK_REASON_SELECTED:
                    dismissAllowingStateLoss();
                    break;
            }
        });
    }

    @Override
    protected void onCreated() {
        viewModel.initViewModel();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.bottom_sheet_select_reason;
    }
}
