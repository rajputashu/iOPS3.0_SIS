package com.sisindia.ai.android.features.addgrievances;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.databinding.FragmentGuardGrievanceDetailBinding;
import com.sisindia.ai.android.room.entities.EmployeeSiteEntity;

public class GuardGrievanceDetailFragment extends IopsBaseFragment {

    private GuardGrievanceDetailsViewModel viewModel;

    public static GuardGrievanceDetailFragment newInstance(int employeeId) {
        GuardGrievanceDetailFragment fragment = new GuardGrievanceDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EmployeeSiteEntity.class.getSimpleName(), employeeId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void extractBundle() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(EmployeeSiteEntity.class.getSimpleName())) {
            int employeeId = bundle.getInt(EmployeeSiteEntity.class.getSimpleName());
            viewModel.setSelectedGuardId(employeeId);
        }
    }

    @Override
    protected void initViewModel() {
        viewModel = (GuardGrievanceDetailsViewModel) getAndroidViewModel(GuardGrievanceDetailsViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentGuardGrievanceDetailBinding binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            if (message.what == NavigationConstants.ON_AUDIO_RECORDED_FOR_GRIEVANCE) {
                String audioFile = (String) message.obj;
                viewModel.onAudioRecorded(audioFile);
            }
        });
    }

    @Override
    protected void onCreated() {
        viewModel.initViewModel();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_guard_grievance_detail;
    }
}
