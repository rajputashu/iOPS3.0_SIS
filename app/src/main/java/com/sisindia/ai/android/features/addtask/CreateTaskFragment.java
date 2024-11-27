package com.sisindia.ai.android.features.addtask;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseFragment;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.databinding.FragmentCreateTaskBinding;
import com.sisindia.ai.android.room.entities.BarrackEntity;
import com.sisindia.ai.android.room.entities.LookUpEntity;
import com.sisindia.ai.android.room.entities.SiteEntity;
import com.sisindia.ai.android.uimodels.TaskTypeModel;
import com.sisindia.ai.android.utils.CustomProgressDialog;

import org.parceler.Parcels;

import static com.sisindia.ai.android.constants.NavigationConstants.HIDE_LOADER_ON_ADD_TASK;
import static com.sisindia.ai.android.constants.NavigationConstants.SHOW_LOADER_ON_ADD_TASK;


public class CreateTaskFragment extends IopsBaseFragment {
    private CreateTaskViewModel viewModel;

    public static CreateTaskFragment newInstance(TaskTypeModel model) {
        CreateTaskFragment fragment = new CreateTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TaskTypeModel.class.getSimpleName(), Parcels.wrap(model));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void extractBundle() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(TaskTypeModel.class.getSimpleName())) {
            TaskTypeModel model = Parcels.unwrap(bundle.getParcelable(TaskTypeModel.class.getSimpleName()));
            viewModel.selectedTaskType.set(model);
        } else {
            Toast.makeText(requireActivity(), "Please Select Again", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        }
    }

    @Override
    protected void initViewModel() {
        viewModel = (CreateTaskViewModel) getAndroidViewModel(CreateTaskViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentCreateTaskBinding binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            switch (message.what) {

                case NavigationConstants.ON_CREATE_TASK_SITE_SELECTED:
                    SiteEntity site = (SiteEntity) message.obj;
                    viewModel.selectedBarrack.set(null);
                    viewModel.selectedSite.set(site);
                    break;

                case NavigationConstants.ON_CREATE_TASK_REASON_SELECTED:
                    LookUpEntity reason = (LookUpEntity) message.obj;
                    viewModel.selectedReason.set(reason);
                    break;

                case NavigationConstants.ON_CREATE_TASK_BARRACK_SELECTED:
                    BarrackEntity barrack = (BarrackEntity) message.obj;
                    viewModel.selectedBarrack.set(barrack);
                    break;

                case NavigationConstants.ON_CREATE_TASK_SUB_TASK_TYPE_SELECTED:
                    LookUpEntity selectedSubTaskType = (LookUpEntity) message.obj;
                    viewModel.selectedSubTaskType.set(selectedSubTaskType);
                    break;

                case SHOW_LOADER_ON_ADD_TASK:
                    showDialogProgress();
                    break;

                case HIDE_LOADER_ON_ADD_TASK:
                    hideDialogProgress();
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
        return R.layout.fragment_create_task;
    }

    private Dialog dialog = null;

    private void showDialogProgress() {
        dialog = CustomProgressDialog.Companion.buildDialog(requireActivity(), "Please wait... creating rota");
        dialog.show();
    }

    private void hideDialogProgress() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}