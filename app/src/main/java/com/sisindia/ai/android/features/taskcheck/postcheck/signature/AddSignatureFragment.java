package com.sisindia.ai.android.features.taskcheck.postcheck.signature;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;
import com.sisindia.ai.android.databinding.FragmentAddSignatureBinding;


public class AddSignatureFragment extends IopsBaseFragment {

    private AddSignatureViewModel viewModel;

    public static Fragment newInstance() {
        return new AddSignatureFragment();
    }

    @Override
    protected void extractBundle() {

    }

    @Override
    protected void initViewModel() {
        viewModel = (AddSignatureViewModel) getAndroidViewModel(AddSignatureViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentAddSignatureBinding binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {
        viewModel.initViewModel();
    }


    @Override
    protected void onCreated() {
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_add_signature;
    }
}
