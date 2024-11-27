package com.sisindia.ai.android.features.reviewinformation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;
import com.sisindia.ai.android.databinding.FragmentReviewInformationComplaintsBinding;

public class ReviewInformationComplaintFragment extends IopsBaseFragment {

    public static final int FRAGMENT_TYPE_OPEN = 1, FRAGMENT_TYPE_CLOSED = 2;

    private static final String FRAGMENT_TYPE = "fragment_type_tag";

    private int viewType;

    private FragmentReviewInformationComplaintsBinding binding;
    private ReviewInformationDetailViewModel viewModel;

    public static ReviewInformationComplaintFragment openInstance() {
        ReviewInformationComplaintFragment fragment = new ReviewInformationComplaintFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_TYPE, FRAGMENT_TYPE_OPEN);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ReviewInformationComplaintFragment closedInstance() {
        ReviewInformationComplaintFragment fragment = new ReviewInformationComplaintFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_TYPE, FRAGMENT_TYPE_CLOSED);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void extractBundle() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(FRAGMENT_TYPE)) {
            viewType = bundle.getInt(FRAGMENT_TYPE);
        }
        viewModel.initViewModelForComplaints(viewType);
    }

    @Override
    protected void initViewModel() {
        viewModel = (ReviewInformationDetailViewModel) getAndroidViewModel(ReviewInformationDetailViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        binding = (FragmentReviewInformationComplaintsBinding) bindFragmentView(getLayoutResource(), inflater, container);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {

    }

    @Override
    protected void onCreated() {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_review_information_complaints;
    }
}
