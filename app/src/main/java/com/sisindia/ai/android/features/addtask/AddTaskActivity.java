package com.sisindia.ai.android.features.addtask;

import android.app.Activity;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseActivity;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.databinding.ActivityAddTaskBinding;
import com.sisindia.ai.android.uimodels.TaskTypeModel;

public class AddTaskActivity extends IopsBaseActivity {

    private ActivityAddTaskBinding binding;
    private AddTaskViewModel viewModel;

    public static Intent newIntent(Activity activity) {
        return new Intent(activity, AddTaskActivity.class);
    }

    @Override
    protected void extractBundle() {

    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            switch (message.what) {

                case NavigationConstants.ON_CREATE_TASK_CLICK:
                    openCreateTaskScreen((TaskTypeModel) message.obj);
                    break;

                case NavigationConstants.ON_SELECT_SITE_FOR_CREATE_TASK_CLICK:
                    openSelectSiteBottomSheet((TaskTypeModel) message.obj);
                    break;

                case NavigationConstants.ON_SELECT_BARRACK_FOR_CREATE_TASK_CLICK:
                    openSelectBarrackBottomSheet();
//                    openSelectBarrackBottomSheet((SiteEntity) message.obj);
                    break;

                case NavigationConstants.ON_SELECT_REASON_FOR_CREATE_TASK_CLICK:
                    openSelectReasonBottomSheet();
                    break;

                case NavigationConstants.ON_SELECT_SUB_TASK_TYPE_FOR_CREATE_TASK_CLICK:
                    openSubTaskTypeBottomSheet();
                    break;

                case NavigationConstants.ON_TASK_CREATED_SUCCESS:
                    onTaskCreatedSuccess();
                    break;
            }
        });
    }

    private void openSubTaskTypeBottomSheet() {
        if (getSupportFragmentManager().findFragmentByTag(SelectSubTaskTypeBottomSheetFragment.class.getSimpleName()) == null) {
            SelectSubTaskTypeBottomSheetFragment.newInstance().show(getSupportFragmentManager(), SelectSubTaskTypeBottomSheetFragment.class.getSimpleName());
        }
    }

    private void openSelectBarrackBottomSheet() {
        if (getSupportFragmentManager().findFragmentByTag(SelectBarrackBottomSheetFragment.class.getSimpleName()) == null) {
            SelectBarrackBottomSheetFragment.newInstance().show(getSupportFragmentManager(),
                    SelectBarrackBottomSheetFragment.class.getSimpleName());
        }
    }

    private void onTaskCreatedSuccess() {
        setResult(Activity.RESULT_OK);
        this.finish();
    }

    private void openSelectSiteBottomSheet(TaskTypeModel model) {
        if (getSupportFragmentManager().findFragmentByTag(SelectSiteBottomSheetFragment.class.getSimpleName()) == null) {
            SelectSiteBottomSheetFragment.newInstance(model).show(getSupportFragmentManager(),
                    SelectSiteBottomSheetFragment.class.getSimpleName());
        }
    }

    private void openSelectReasonBottomSheet() {
        if (getSupportFragmentManager().findFragmentByTag(SelectReasonBottomSheetFragment.class.getSimpleName()) == null) {
            SelectReasonBottomSheetFragment.newInstance().show(getSupportFragmentManager(), SelectReasonBottomSheetFragment.class.getSimpleName());
        }
    }

    @Override
    protected void onCreated() {
        toolbarWithCustomTitle(binding.tbAddTask);
        openSelectTaskFragment();
    }

    private void openSelectTaskFragment() {
        viewModel.selectedTaskTypeObs.set(null);
        loadFragment(R.id.flAddTask, SelectTaskFragment.newInstance(), FRAGMENT_REPLACE, false);
    }

    private void openCreateTaskScreen(TaskTypeModel model) {
        viewModel.selectedTaskTypeObs.set(model);
        loadFragment(R.id.flAddTask, CreateTaskFragment.newInstance(model), FRAGMENT_REPLACE, false);
    }

    @Override
    protected void initViewBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutResource());
        binding.setVm(viewModel);
        binding.executePendingBindings();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void initViewModel() {
        viewModel = (AddTaskViewModel) getAndroidViewModel(AddTaskViewModel.class);
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.flAddTask);
        if (currentFragment instanceof SelectTaskFragment) {
            super.onBackPressed();
        } else {
            openSelectTaskFragment();
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_task;
    }
}
