package com.sisindia.ai.android.features.issues.complaints;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseDialogFragment;
import com.sisindia.ai.android.databinding.DialogFragmentAddedComplaintBinding;
import com.sisindia.ai.android.room.entities.ComplaintEntity;

import static com.sisindia.ai.android.constants.NavigationConstants.ON_COMPLAINT_ADDED_CONTINUE;

public class AddedComplaintDialogFragment extends IopsBaseDialogFragment {

    private DialogFragmentAddedComplaintBinding binding;
    private ComplaintStatusViewModel viewModel;

    public static AddedComplaintDialogFragment newInstance(int complaintId) {
        AddedComplaintDialogFragment dialogFragment = new AddedComplaintDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ComplaintEntity.class.getSimpleName(), complaintId);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }


    @Override
    protected void extractBundle() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(ComplaintEntity.class.getSimpleName())) {
            int complaintId = bundle.getInt(ComplaintEntity.class.getSimpleName());
            viewModel.fetchComplaintDetail(complaintId);
        } else {
            dismissAllowingStateLoss();
        }
    }

    @Override
    protected void initViewModel() {
        viewModel = (ComplaintStatusViewModel) getAndroidViewModel(ComplaintStatusViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        binding = (DialogFragmentAddedComplaintBinding) bindFragmentView(getLayoutResource(), inflater, container);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            switch (message.what) {
                case ON_COMPLAINT_ADDED_CONTINUE:
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
        return R.layout.dialog_fragment_added_complaint;
    }
}