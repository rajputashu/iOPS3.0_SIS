package com.sisindia.ai.android.features.taskcheck.postcheck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment;
import com.sisindia.ai.android.commons.CaptureImageType;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.databinding.BottomSheetPostCheckListBinding;
import com.sisindia.ai.android.room.entities.CheckedPostCheckListEntity;

import org.parceler.Parcels;

import timber.log.Timber;

import static com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_CAPTURE_IMAGE_FOR_POST_CHECK;
import static com.sisindia.ai.android.features.livecamera.ImageCaptureActivity.newIntentWithType;

public class PostCheckListBottomSheetFragment extends IopsBaseBottomSheetDialogFragment {

    private PostCheckListViewModel viewModel;

    public static PostCheckListBottomSheetFragment newInstance(CheckedPostCheckListEntity item) {
        PostCheckListBottomSheetFragment fragment = new PostCheckListBottomSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(CheckedPostCheckListEntity.class.getSimpleName(), Parcels.wrap(item));
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void extractBundle() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(CheckedPostCheckListEntity.class.getSimpleName())) {
            CheckedPostCheckListEntity item = Parcels.unwrap(bundle.getParcelable(CheckedPostCheckListEntity.class.getSimpleName()));
            viewModel.itemObs.set(item);
        } else {
            dismissAllowingStateLoss();
        }
    }


    @Override
    protected void applyStyle() {
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }

    @Override
    protected void initViewModel() {
        viewModel = (PostCheckListViewModel) getAndroidViewModel(PostCheckListViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        BottomSheetPostCheckListBinding binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            switch (message.what) {

                case NavigationConstants.ON_ADD_IMAGE_FOR_CHECK_LIST:
                    openImageCaptureForSiteCheck();
                    break;

                case NavigationConstants.ON_ADD_POST_CHECK_LIST_DONE:
                    dismissAllowingStateLoss();
                    break;
            }
        });
    }

    private void openImageCaptureForSiteCheck() {
        startActivityForResult(newIntentWithType(requireActivity(), CaptureImageType.SITE_CHECK), REQUEST_CODE_CAPTURE_IMAGE_FOR_POST_CHECK);
    }

    @Override
    protected void onCreated() {
        viewModel.initViewModel();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAPTURE_IMAGE_FOR_POST_CHECK) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                viewModel.imageUri.set(data.getData());
            } else {
                Timber.e("Image capture cancelled.");
            }
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.bottom_sheet_post_check_list;
    }
}
