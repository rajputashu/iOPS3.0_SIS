package com.sisindia.ai.android.features.addtask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.databinding.BottomSheetSelectBarrackBinding;
import com.sisindia.ai.android.room.entities.SiteEntity;

import org.parceler.Parcels;

public class SelectBarrackBottomSheetFragment extends IopsBaseBottomSheetDialogFragment {

    private SelectBarrackViewModel viewModel;

    public static SelectBarrackBottomSheetFragment newInstance() {
        SelectBarrackBottomSheetFragment fragment = new SelectBarrackBottomSheetFragment();
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(SiteEntity.class.getSimpleName(), Parcels.wrap(site));
//        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void extractBundle() {
//        Bundle bundle = getArguments();
//        if (bundle != null && bundle.containsKey(SiteEntity.class.getSimpleName())) {
//            SiteEntity site = Parcels.unwrap(bundle.getParcelable(SiteEntity.class.getSimpleName()));
//            viewModel.selectedSite.set(site);
//        } else {
//            requireActivity().onBackPressed();
//        }
    }

    @Override
    protected void applyStyle() {
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogThemeWithKeyboard);
    }

    @Override
    protected void initViewModel() {
        viewModel = (SelectBarrackViewModel) getAndroidViewModel(SelectBarrackViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        BottomSheetSelectBarrackBinding binding = (BottomSheetSelectBarrackBinding) bindFragmentView(getLayoutResource(), inflater, container);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {

            switch (message.what) {
                case NavigationConstants.ON_CREATE_TASK_BARRACK_SELECTED:
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
        return R.layout.bottom_sheet_select_barrack;
    }
}
