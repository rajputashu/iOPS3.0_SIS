package com.sisindia.ai.android.features.addkitrequest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment;
import com.sisindia.ai.android.databinding.BottomSheetKitItemSelectSizeBinding;

public class SelectKitItemSizeBottomSheetFragment extends IopsBaseBottomSheetDialogFragment {

    private AddKitRequestViewModel viewModel;
    private BottomSheetKitItemSelectSizeBinding binding;

    public static SelectKitItemSizeBottomSheetFragment newInstance() {
        SelectKitItemSizeBottomSheetFragment fragment = new SelectKitItemSizeBottomSheetFragment();
        return fragment;
    }

    @Override
    protected void extractBundle() {

    }

    @Override
    protected void applyStyle() {
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }

    @Override
    protected void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(AddKitRequestViewModel.class);
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

    }

    @Override
    protected void onCreated() {

        binding.ivCancelKitItemSize.setOnClickListener(v -> {
            viewModel.onKitItemSizeCancelled(v);
            dismissAllowingStateLoss();
        });

        binding.btnKitItemSizeSelected.setOnClickListener(v -> {
            viewModel.onKitItemSizeSelected(v);
            dismissAllowingStateLoss();
        });

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.bottom_sheet_kit_item_select_size;
    }
}
