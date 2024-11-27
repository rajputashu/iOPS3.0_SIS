package com.sisindia.ai.android.features.taskcheck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.databinding.BottomSheetSiteCheckListBinding;
import com.sisindia.ai.android.features.imagecapture.CaptureImageActivityV2;
import com.sisindia.ai.android.room.entities.AttachmentEntity;
import com.sisindia.ai.android.room.entities.CheckedSiteCheckListEntity;

import org.parceler.Parcels;

import static com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_CAPTURE_IMAGE_FOR_SITE_CHECK;


public class SiteCheckListBottomSheetFragment extends IopsBaseBottomSheetDialogFragment {

    private SiteCheckListViewModel viewModel;

    public static SiteCheckListBottomSheetFragment newInstance(CheckedSiteCheckListEntity item) {
        SiteCheckListBottomSheetFragment fragment = new SiteCheckListBottomSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(CheckedSiteCheckListEntity.class.getSimpleName(), Parcels.wrap(item));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.bottom_sheet_site_check_list;
    }

    @Override
    protected void extractBundle() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(CheckedSiteCheckListEntity.class.getSimpleName())) {
            CheckedSiteCheckListEntity item = Parcels.unwrap(bundle.getParcelable(CheckedSiteCheckListEntity.class.getSimpleName()));
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
        viewModel = (SiteCheckListViewModel) getAndroidViewModel(SiteCheckListViewModel.class);
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        BottomSheetSiteCheckListBinding binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        binding.setVm(viewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            switch (message.what) {

                case NavigationConstants.ON_ADD_IMAGE_FOR_CHECK_LIST:
                    AttachmentEntity item = (AttachmentEntity) message.obj;
                    openImageCaptureForSiteCheck(item);
                    break;

                case NavigationConstants.ON_ADD_SITE_CHECK_LIST_DONE:
                    dismissAllowingStateLoss();
                    break;
            }
        });
    }

    private void openImageCaptureForSiteCheck(AttachmentEntity item) {
        startActivityForResult(CaptureImageActivityV2.newIntent(requireActivity(), item), REQUEST_CODE_CAPTURE_IMAGE_FOR_SITE_CHECK);
    }

    @Override
    protected void onCreated() {
        viewModel.initViewModel();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAPTURE_IMAGE_FOR_SITE_CHECK) {
            if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null && data.getExtras().containsKey(AttachmentEntity.class.getSimpleName())) {
                if (Parcels.unwrap(data.getExtras().getParcelable(AttachmentEntity.class.getSimpleName())) != null)
                    viewModel.siteCheckAttachment.set(Parcels.unwrap(data.getExtras().getParcelable(AttachmentEntity.class.getSimpleName())));
            } else {
                Toast.makeText(requireActivity(), "Unable to Capture Image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
