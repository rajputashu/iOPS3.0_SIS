package com.sisindia.ai.android.features.issues.grievance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseDialogFragment;
import com.sisindia.ai.android.databinding.DialogFragmentClosedGrievanceBinding;
import com.sisindia.ai.android.features.addgrievances.AddedGrievanceViewModel;
import com.sisindia.ai.android.room.entities.GrievanceEntity;

import static com.sisindia.ai.android.constants.NavigationConstants.ON_GRIEVANCE_ADDED_CONTINUE;

public class ClosedGrievanceDialogFragment extends IopsBaseDialogFragment {

    private DialogFragmentClosedGrievanceBinding binding;
    private AddedGrievanceViewModel viewModel;

    public static ClosedGrievanceDialogFragment newInstance(int grievanceId) {
        ClosedGrievanceDialogFragment dialogFragment = new ClosedGrievanceDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(GrievanceEntity.class.getSimpleName(), grievanceId);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }


    @Override
    protected void extractBundle() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(GrievanceEntity.class.getSimpleName())) {
            int grievanceId = bundle.getInt(GrievanceEntity.class.getSimpleName());
            viewModel.onClosedGrievance(grievanceId);
        } else {
            dismissAllowingStateLoss();
        }
    }

    @Override
    protected void initViewModel() {
        viewModel = (AddedGrievanceViewModel) getAndroidViewModel(AddedGrievanceViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        binding = (DialogFragmentClosedGrievanceBinding) bindFragmentView(getLayoutResource(), inflater, container);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            switch (message.what) {
                case ON_GRIEVANCE_ADDED_CONTINUE:
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
        return R.layout.dialog_fragment_closed_grievance;
    }
}
