package com.sisindia.ai.android.features.taskcheck.postcheck.photoevaluation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;
import com.sisindia.ai.android.databinding.FragmentGuardPhotoEvaluationBinding;
import com.sisindia.ai.android.room.entities.AttachmentEntity;

import org.parceler.Parcels;

import static com.sisindia.ai.android.constants.NavigationConstants.ON_GUARD_EVALUATION_EDITED;

public class GuardPhotoEvaluationResultFragment extends IopsBaseFragment {

    private GuardPhotoEvaluationResultViewModel viewModel;


    public static Fragment newInstance(AttachmentEntity item) {
        GuardPhotoEvaluationResultFragment fragment = new GuardPhotoEvaluationResultFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(AttachmentEntity.class.getSimpleName(), Parcels.wrap(item));
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void extractBundle() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(AttachmentEntity.class.getSimpleName())) {
            viewModel.attachment = Parcels.unwrap(getArguments().getParcelable(AttachmentEntity.class.getSimpleName()));
        }
    }

    @Override
    protected void initViewModel() {
        viewModel = (GuardPhotoEvaluationResultViewModel) getAndroidViewModel(GuardPhotoEvaluationResultViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentGuardPhotoEvaluationBinding binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            if (message.what == ON_GUARD_EVALUATION_EDITED) {
                viewModel.fetchGuardTurnOutFromDB();
            }
        });
    }

    @Override
    protected void onCreated() {
        viewModel.initTurnOutAIDetector();
//        viewModel.initObjectDetectorDetector();
//        viewModel.fetchGuardTurnOutFromDB();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_guard_photo_evaluation;
    }
}
