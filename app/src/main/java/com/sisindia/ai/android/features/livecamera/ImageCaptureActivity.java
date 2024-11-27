package com.sisindia.ai.android.features.livecamera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseActivity;
import com.sisindia.ai.android.commons.CaptureImageType;
import com.sisindia.ai.android.constants.IntentConstants;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.databinding.ActivityImageCaptureBinding;
import com.sisindia.ai.android.features.addkitrequest.AddKitRequestActivity;
import com.sisindia.ai.android.features.securityrisk.AddSecurityRiskActivity;
import com.sisindia.ai.android.features.taskcheck.postcheck.postguardscan.PostGuardScanActivity;
import com.sisindia.ai.android.room.entities.AttachmentEntity;
import com.sisindia.ai.android.utils.FileUtils;

import org.parceler.Parcels;

import java.io.File;
import java.util.Objects;

public class ImageCaptureActivity extends IopsBaseActivity {

    private LiveImageCameraViewModel viewModel;
    private CaptureImageType imageType = CaptureImageType.UNKNOWN;
    private Boolean isFrontCameraRequested = false;

    @Deprecated
    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, ImageCaptureActivity.class);
        Bundle bundle = new Bundle();
        if (activity instanceof PostGuardScanActivity)
            bundle.putSerializable(CaptureImageType.class.getSimpleName(), CaptureImageType.GUARD_FULL_PHOTO);
        else if (activity instanceof AddSecurityRiskActivity)
            bundle.putSerializable(CaptureImageType.class.getSimpleName(), CaptureImageType.ADD_SECURITY);
        else if (activity instanceof AddKitRequestActivity)
            bundle.putSerializable(CaptureImageType.class.getSimpleName(), CaptureImageType.KIT_REQUEST_PHOTO);
        intent.putExtras(bundle);
        return intent;
    }

    //Added to test things against Bill Collection file upload
    public static Intent newIntentWithAttachment(Activity activity, AttachmentEntity entity) {
        Intent intent = new Intent(activity, ImageCaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(AttachmentEntity.class.getSimpleName(), Parcels.wrap(entity));
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newIntentWithType(Activity activity, CaptureImageType type) {
        Intent intent = new Intent(activity, ImageCaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CaptureImageType.class.getSimpleName(), type);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newIntentWithType(Fragment fragment, CaptureImageType type) {
        Intent intent = new Intent(fragment.getActivity(), ImageCaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CaptureImageType.class.getSimpleName(), type);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newIntentWithFrontCamera(Fragment fragment, CaptureImageType type) {
        Intent intent = new Intent(fragment.getActivity(), ImageCaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CaptureImageType.class.getSimpleName(), type);
        intent.putExtras(bundle);
        intent.putExtra("IsFrontCameraRequested", true);
        return intent;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_image_capture;
    }

    @Override
    protected void extractBundle() {
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle.containsKey(CaptureImageType.class.getSimpleName())) {
                imageType = (CaptureImageType) getIntent().getExtras().getSerializable(CaptureImageType.class.getSimpleName());
            }
            if (bundle.containsKey(AttachmentEntity.class.getSimpleName())) {
                viewModel.obsAttachmentEntity.set(Parcels.unwrap(getIntent().getParcelableExtra(AttachmentEntity.class.getSimpleName())));
            }
            if (bundle.containsKey("IsFrontCameraRequested")) {
                isFrontCameraRequested = bundle.getBoolean("IsFrontCameraRequested", false);
            }
        }
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, msg -> {
            switch (msg.what) {
                case NavigationConstants.ON_FILE_NAME_GENERATED:
                    openImageCaptureScreen();
                    break;

                case NavigationConstants.ON_IMAGE_RETAKE_CLICK:
                    generateAttachmentFileName();
//                    openImageCaptureScreen();
                    break;

                case NavigationConstants.ON_IMAGE_CAPTURED:
                    viewModel.compressedFileSize.set(msg.arg1);
                    viewModel.imageUri.set((Uri) msg.obj);
//                    openImageResultScreen((Uri) msg.obj);
                    openImageResultScreen();
                    break;

                case NavigationConstants.ON_IMAGE_CAPTURE_CONFIRM:
                    onImageConfirm();
                    break;

                case NavigationConstants.ON_TEMP_ATTACHMENT_METADATA_INSERTED:
                    setResult(Activity.RESULT_OK, new Intent().setData(viewModel.imageUri.get()));
                    finish();
                    break;

                case NavigationConstants.ON_ATTACHMENT_METADATA_INSERTED:
//                    setResult(Activity.RESULT_OK, new Intent().setData(currentImageUri));
                    Intent intent = new Intent();
                    intent.putExtra(IntentConstants.CAPTURED_FILE_URI, Objects.requireNonNull(viewModel.imageUri.get()).toString());
                    intent.putExtra(IntentConstants.FILE_METADATA, viewModel.updatedAttachmentMetaData.get());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    break;
            }
        });
    }

    @Override
    protected void initViewBinding() {
        ActivityImageCaptureBinding binding = DataBindingUtil.setContentView(this, getLayoutResource());
        binding.setVm(viewModel);
        binding.executePendingBindings();
    }

    @Override
    protected void initViewModel() {
        viewModel = (LiveImageCameraViewModel) getAndroidViewModel(LiveImageCameraViewModel.class);
    }

    @Override
    protected void onCreated() {
        generateAttachmentFileName();
    }

    private void generateAttachmentFileName() {
        if (Objects.requireNonNull(viewModel.obsAttachmentEntity.get()).localFileName != null
                && !Objects.requireNonNull(viewModel.obsAttachmentEntity.get()).localFileName.isEmpty()) {
            viewModel.generatedFileName.set(Objects.requireNonNull(viewModel.obsAttachmentEntity.get()).localFileName);
            openImageCaptureScreen();
        } else {
            viewModel.attachmentSourceId.set(imageType.getAttachmentSourceTypeId());
            viewModel.generateFileNaming(imageType);
        }
    }

    private void onImageConfirm() {

        if (Objects.requireNonNull(viewModel.obsAttachmentEntity.get()).localFileName != null
                && !Objects.requireNonNull(viewModel.obsAttachmentEntity.get()).localFileName.isEmpty()) {
            viewModel.updateAttachmentWithLatestData();
            Intent returnIntent = new Intent();
            returnIntent.putExtra(AttachmentEntity.class.getSimpleName(), Parcels.wrap(viewModel.obsAttachmentEntity.get()));
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else
            viewModel.getAndCreateEmployeeProfileMetaData();
    }

    void openImageCaptureScreen() {
//        File generatedFile = FileUtils.getFileByPath(FileUtils.createFileName(viewModel.generatedFileName.get()));
        File generatedFile;
        /*if (Build.VERSION.SDK_INT >= 30)
            generatedFile = FileUtils.getFileByPath(FileUtils.createFileNameV2(viewModel.generatedFileName.get(), this));
        else
            generatedFile = FileUtils.getFileByPath(FileUtils.createFileName(viewModel.generatedFileName.get()));*/

        generatedFile = FileUtils.getFileByPath(FileUtils.createFileNameV2(viewModel.generatedFileName.get(), this));

        if (isFrontCameraRequested)
            loadFragment(R.id.flLiveImage, CaptureImageFragment.Companion.newInstanceWithFront(Objects.requireNonNull(generatedFile)),
                    FRAGMENT_REPLACE, false);
        else
            loadFragment(R.id.flLiveImage, CaptureImageFragment.Companion.newInstance(Objects.requireNonNull(generatedFile)),
                    FRAGMENT_REPLACE, false);
    }

    void openImageResultScreen() {
//        this.currentImageUri = uri;
        loadFragment(R.id.flLiveImage, LiveImageRenderFragment.newInstance(viewModel.imageUri.get()),
                FRAGMENT_REPLACE, false);
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.flLiveImage);
        if (currentFragment instanceof CaptureImageFragment) super.onBackPressed();
        else openImageCaptureScreen();
    }
}
