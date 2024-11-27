package com.sisindia.ai.android.features.addkitrequest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseDialogFragment;
import com.sisindia.ai.android.databinding.DialogFragmentAddedKitRequestBinding;
import com.sisindia.ai.android.room.entities.GuardKitRequestEntity;

import static com.sisindia.ai.android.constants.NavigationConstants.ON_KIT_REQUEST_COMPLETED;

public class AddedKitRequestDialogFragment extends IopsBaseDialogFragment {

    private DialogFragmentAddedKitRequestBinding binding;
    private AddedKitRequestViewModel viewModel;

    public static AddedKitRequestDialogFragment newInstance(int rowId) {
        AddedKitRequestDialogFragment dialogFragment = new AddedKitRequestDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(GuardKitRequestEntity.class.getSimpleName(), rowId);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }


    @Override
    protected void extractBundle() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(GuardKitRequestEntity.class.getSimpleName())) {
            int rowId = bundle.getInt(GuardKitRequestEntity.class.getSimpleName());
            viewModel.initViewModel(rowId);
        } else {
            dismissAllowingStateLoss();
        }
    }

    @Override
    protected void initViewModel() {
        viewModel = (AddedKitRequestViewModel) getAndroidViewModel(AddedKitRequestViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        binding = (DialogFragmentAddedKitRequestBinding) bindFragmentView(getLayoutResource(), inflater, container);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            switch (message.what) {

                case ON_KIT_REQUEST_COMPLETED:
                    dismissAllowingStateLoss();
                    break;
            }
        });
    }

    @Override
    protected void onCreated() {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_fragment_added_kit_request;
    }
}
