package com.sisindia.ai.android.features.conveyance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;
import com.sisindia.ai.android.constants.IntentConstants;
import com.sisindia.ai.android.databinding.FragmentConveyanceBinding;

public class ConveyanceFragment extends IopsBaseFragment {

    private ConveyanceViewModel viewModel;

    public static Fragment newInstance() {
        return new ConveyanceFragment();
    }

    public static Fragment newInstance(String selectedDate) {
        Fragment fragment = new ConveyanceFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IntentConstants.DATE, selectedDate);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void extractBundle() {
        Bundle receivedBundle = getArguments();
        if (receivedBundle != null && receivedBundle.containsKey(IntentConstants.DATE)) {
//            Timber.e("Date Selected %s", receivedBundle.getString(IntentConstants.DATE));
//            viewModel.conveyanceDate.set(receivedBundle.getString(IntentConstants.DATE));
            viewModel.isSingleDateConveyanceRequest.set(true);
            viewModel.receivedDate.set(receivedBundle.getString(IntentConstants.DATE));
        }
    }

    @Override
    protected void initViewModel() {
        viewModel = (ConveyanceViewModel) getAndroidViewModel(ConveyanceViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentConveyanceBinding binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {

    }

    @Override
    protected void onCreated() {
        viewModel.initViewModel();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_conveyance;
    }
}
