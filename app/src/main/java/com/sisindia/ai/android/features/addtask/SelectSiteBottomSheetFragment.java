package com.sisindia.ai.android.features.addtask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.databinding.BottomSheetSelectSiteBinding;
import com.sisindia.ai.android.uimodels.TaskTypeModel;

import org.parceler.Parcels;

public class SelectSiteBottomSheetFragment extends IopsBaseBottomSheetDialogFragment {

    private SelectSiteViewModel viewModel;

    public static SelectSiteBottomSheetFragment newInstance(TaskTypeModel model) {
        SelectSiteBottomSheetFragment fragment = new SelectSiteBottomSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TaskTypeModel.class.getSimpleName(), Parcels.wrap(model));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void extractBundle() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(TaskTypeModel.class.getSimpleName())) {
            TaskTypeModel model = Parcels.unwrap(bundle.getParcelable(TaskTypeModel.class.getSimpleName()));
            viewModel.selectedTaskType.set(model);
        }
    }

    @Override
    protected void applyStyle() {
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogThemeWithKeyboard);
    }

    @Override
    protected void initViewModel() {
        viewModel = (SelectSiteViewModel) getAndroidViewModel(SelectSiteViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        BottomSheetSelectSiteBinding binding = (BottomSheetSelectSiteBinding) bindFragmentView(getLayoutResource(), inflater, container);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            if (message.what == NavigationConstants.ON_CREATE_TASK_SITE_SELECTED) {
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
        return R.layout.bottom_sheet_select_site;
    }
}
