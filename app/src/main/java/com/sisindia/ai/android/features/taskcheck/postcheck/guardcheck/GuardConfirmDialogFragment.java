package com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseDialogFragment;
import com.sisindia.ai.android.databinding.DialogFragmentGuardConfirmBinding;
import com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.available.GuardAvailableViewModel;

import static com.sisindia.ai.android.constants.NavigationConstants.ON_CLOSE_DIALOG_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_GUARD_AVAILABLE;

public class GuardConfirmDialogFragment extends IopsBaseDialogFragment {

    private GuardAvailableViewModel viewModel;

    public static GuardConfirmDialogFragment newInstance() {
        return new GuardConfirmDialogFragment();
    }

    @Override
    protected void extractBundle() {

    }

    @Override
    protected void initViewModel() {
        viewModel = new ViewModelProvider(requireParentFragment(), viewModelFactory).get(GuardAvailableViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        DialogFragmentGuardConfirmBinding binding = (DialogFragmentGuardConfirmBinding) bindFragmentView(getLayoutResource(), inflater, container);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            switch (message.what) {
                case ON_GUARD_AVAILABLE:
                case ON_CLOSE_DIALOG_CLICK:
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
        return R.layout.dialog_fragment_guard_confirm;
    }
}
