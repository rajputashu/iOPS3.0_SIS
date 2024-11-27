package com.sisindia.ai.android.features.imagecapture;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.room.entities.AttachmentEntity;
import com.sisindia.ai.android.utils.FileUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.inject.Inject;

import timber.log.Timber;

public class CaptureImageViewModel extends IopsBaseViewModel {

    public ObservableField<AttachmentEntity> itemObs = new ObservableField<>(new AttachmentEntity());
    public ObservableInt selfieLoader = new ObservableInt(View.GONE);
    public ObservableBoolean isFakeGuard = new ObservableBoolean(false);

    @Inject
    public CaptureImageViewModel(@NonNull Application application) {
        super(application);
    }

    /*public File getTempFileForImage() {
        AttachmentEntity item = itemObs.get();
        StringBuilder builder = new StringBuilder();
        if (item != null) {
            builder.append(item.attachmentSourceType);
        }
        return FileUtils.getFileByPath(FileUtils.createTempFile(builder.toString()));
    }*/

    public File getTempFileForImageV2(Context context) {
        AttachmentEntity item = itemObs.get();
        StringBuilder builder = new StringBuilder();
        if (item != null) {
            builder.append(item.attachmentSourceType);
        }
        return FileUtils.getFileByPath(FileUtils.createTempFileV2(builder.toString(), context));
    }

    public void onImageCaptured(@NotNull File compressedImageFile) {
        setIsLoading(false);
        AttachmentEntity item = itemObs.get();
        if (item != null) {
            item.fileSize = compressedImageFile.length() / 1024;
            item.localFilePath = Uri.fromFile(compressedImageFile).toString();
        }
        message.what = NavigationConstants.ON_IMAGE_CAPTURED_V2;
        liveData.postValue(message);
    }

    public void onSelfieImageCaptured(@NotNull File compressedImageFile) {
        AttachmentEntity item = itemObs.get();
        if (item != null) {
            item.fileSize = compressedImageFile.length() / 1024;
            item.localFilePath = Uri.fromFile(compressedImageFile).toString();
        }
        message.what = NavigationConstants.ON_SELFIE_IMAGE_CAPTURED_V2;
        message.obj = item;
        liveData.postValue(message);
    }

    /*
     * VALIDATING WHETHER IMAGE CAPTURED IS OF GUARD OR OF ANY OTHER OBJECT
     */
    public void validateHumanBodyViaMLKit() {
        try {
            AttachmentEntity item = itemObs.get();
            if (Objects.requireNonNull(item).attachmentSourceType == AttachmentEntity.AttachmentSourceType.GUARD_FULL_PHOTO.getSourceType() ||
                    Objects.requireNonNull(item).attachmentSourceType == AttachmentEntity.AttachmentSourceType.SLEEPING_GUARD.getSourceType()) {

                Uri uri = Uri.parse(item.localFilePath);

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), uri);
                InputImage image = InputImage.fromBitmap(bitmap, 0);
                PoseDetectorOptions options = new PoseDetectorOptions.Builder()
                        .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
                        .build();
                PoseDetector poseDetector = PoseDetection.getClient(options);

                poseDetector.process(image)
                        .addOnSuccessListener(pose -> {
                            // Check if any human pose landmarks are detected
                            boolean hasHumanBody = !pose.getAllPoseLandmarks().isEmpty();
                            Timber.e("Has human body in pic %s", hasHumanBody);
                            isFakeGuard.set(!hasHumanBody);

                            // updating the status of isFakeGuardImage flag based on detection
                            Objects.requireNonNull(itemObs.get()).isFakeGuardImage = isFakeGuard.get();
                        })
                        .addOnFailureListener(Throwable::printStackTrace);
            }
        } catch (IOException ignored) {
        }
    }

    public void onConfirmClick(View view) {
        AttachmentEntity item = itemObs.get();
        if (item == null) {
            showToast("Error in image capture");
            return;
        }
        item.isFileSaved = true;
        message.obj = item;
        message.what = NavigationConstants.ON_IMAGE_CONFIRM_V2;
        liveData.postValue(message);
    }

    public void onRetakeClick(View view) {
        message.what = NavigationConstants.ON_IMAGE_RETAKE_V2;
        liveData.postValue(message);
    }
}
