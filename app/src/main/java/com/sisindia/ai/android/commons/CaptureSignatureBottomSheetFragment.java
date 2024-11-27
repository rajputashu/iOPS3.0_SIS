package com.sisindia.ai.android.commons;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.droidcommons.views.ink.InkView;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment;
import com.sisindia.ai.android.databinding.BottomSheetCaptureSignatureBinding;
import com.sisindia.ai.android.room.entities.AttachmentEntity;
import com.sisindia.ai.android.utils.FileUtils;
import com.sisindia.ai.android.utils.ImageUtils;

import org.parceler.Parcels;

import java.io.File;

public class CaptureSignatureBottomSheetFragment extends IopsBaseBottomSheetDialogFragment {

    private BottomSheetCaptureSignatureBinding binding;

    private AttachmentEntity signAttachment;
    private OnSignDoneListener onSignDoneListeners;

    @Deprecated
    public static CaptureSignatureBottomSheetFragment newInstance() {
        CaptureSignatureBottomSheetFragment fragment = new CaptureSignatureBottomSheetFragment();
        return fragment;
    }

    public static CaptureSignatureBottomSheetFragment newInstance(AttachmentEntity signAttachment) {
        CaptureSignatureBottomSheetFragment fragment = new CaptureSignatureBottomSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(AttachmentEntity.class.getSimpleName(), Parcels.wrap(signAttachment));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void extractBundle() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(AttachmentEntity.class.getSimpleName())) {
            signAttachment = Parcels.unwrap(bundle.getParcelable(AttachmentEntity.class.getSimpleName()));
        } else {
            Toast.makeText(requireActivity(), "Unable to capture... signature", Toast.LENGTH_SHORT).show();
            dismissAllowingStateLoss();
        }

    }

    @Override
    protected void applyStyle() {
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }

    @Override
    protected void initViewModel() {
    }

    @Override
    protected View initViewBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    protected void initViewState() {

    }

    @Override
    protected void onCreated() {

        binding.ivOnSignatureClear.setOnClickListener(v -> {
            binding.inkSignature.clear();
        });

        binding.btnSignatureDone.setOnClickListener(v -> {
            if (binding.inkSignature.isViewEmpty()) {
                Toast.makeText(requireActivity(), "Please add Signature to continue..", Toast.LENGTH_SHORT).show();
                return;
            }

//            if (FileUtils.createOrExistsDir(FileUtils.DIR_ROOT)) {
            String imagePath = FileUtils.createTempFileV2(String.valueOf(signAttachment.attachmentSourceType), requireActivity());
            Bitmap bitmap = binding.inkSignature.getBitmap(R.color.colorWhite);
            if (ImageUtils.save(bitmap, imagePath, Bitmap.CompressFormat.JPEG)) {
                File signFile = new File(imagePath);
                signAttachment.isFileSaved = true;
                signAttachment.fileSize = signFile.length() / 1024;
                signAttachment.localFilePath = Uri.fromFile(signFile).toString();
                onSignatureFileSaved(signAttachment);
            } else {
                Toast.makeText(requireActivity(), "Unable to add Signature..", Toast.LENGTH_SHORT).show();
            }
//            }
        });

        binding.inkSignature.addListener(new InkView.InkListener() {

            @Override
            public void onInkClear() {
                binding.ivOnSignatureClear.setVisibility(View.GONE);
            }

            @Override
            public void onInkDraw() {
                binding.ivOnSignatureClear.setVisibility(View.VISIBLE);
            }
        });

    }

    private void onSignatureFileSaved(AttachmentEntity attachment) {
        if (onSignDoneListeners != null) {
            onSignDoneListeners.onSignatureCaptured(attachment);
            dismissAllowingStateLoss();
        }

//        Message message = new Message();
//        message.what = NavigationConstants.ON_SIGNATURE_CAPTURED;
//        message.obj = attachment;
//        liveData.postValue(message);
//
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.bottom_sheet_capture_signature;
    }

    public void setOnSignDoneListeners(OnSignDoneListener onSignDoneListeners) {
        this.onSignDoneListeners = onSignDoneListeners;
    }

    public interface OnSignDoneListener {
        void onSignatureCaptured(AttachmentEntity item);
    }
}
