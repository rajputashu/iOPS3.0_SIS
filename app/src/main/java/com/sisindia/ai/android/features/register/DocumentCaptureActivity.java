package com.sisindia.ai.android.features.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseActivity;
import com.sisindia.ai.android.databinding.ActivityCaptureDocumentBinding;
import com.sisindia.ai.android.features.imagecapture.CaptureImageActivityV2;
import com.sisindia.ai.android.room.entities.AttachmentEntity;
import com.sisindia.ai.android.room.entities.CheckedRegisterEntity;

import org.parceler.Parcels;

import timber.log.Timber;

import static com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_CAPTURE_DOCUMENT;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_CAPTURE_DOCUMENT;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_CAPTURE_DOCUMENT_DONE;

public class DocumentCaptureActivity extends IopsBaseActivity {

    private DocumentCaptureViewModel viewModel;
    private ActivityCaptureDocumentBinding binding;

    public static Intent newIntent(Activity activity, CheckedRegisterEntity item) {
        Intent intent = new Intent(activity, DocumentCaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CheckedRegisterEntity.class.getSimpleName(), Parcels.wrap(item));
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void extractBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(CheckedRegisterEntity.class.getSimpleName())) {
            CheckedRegisterEntity item = Parcels.unwrap(bundle.getParcelable(CheckedRegisterEntity.class.getSimpleName()));
            viewModel.selectedItem.set(item);
        }
    }

    @Override
    protected void initViewState() {
        liveData.observe(this, message -> {
            switch (message.what) {

                case ON_CAPTURE_DOCUMENT:
                    AttachmentEntity item = (AttachmentEntity) message.obj;
                    openCaptureDocumentScreen(item);
                    break;

                case ON_CAPTURE_DOCUMENT_DONE:
                    onCaptureDone();
                    break;
            }
        });
    }

    private void onCaptureDone() {
        setResult(RESULT_OK);
        this.finish();
    }

    private void openCaptureDocumentScreen(AttachmentEntity item) {
        startActivityForResult(CaptureImageActivityV2.newIntent(this, item), REQUEST_CODE_CAPTURE_DOCUMENT);
    }

    @Override
    protected void onCreated() {
        setupToolBarForBackArrow(binding.tbScanDocument);
        viewModel.initViewModel();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void initViewBinding() {
        binding = (ActivityCaptureDocumentBinding) bindActivityView(this, getLayoutResource());
        binding.setVm(viewModel);
        binding.executePendingBindings();
    }

    @Override
    protected void initViewModel() {
        viewModel = (DocumentCaptureViewModel) getAndroidViewModel(DocumentCaptureViewModel.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CAPTURE_DOCUMENT) {
            if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
                AttachmentEntity attachment = Parcels.unwrap(data.getExtras().getParcelable(AttachmentEntity.class.getSimpleName()));
                viewModel.onDocumentCaptured(attachment);
            } else {
                Timber.e("Image capture cancelled.");
            }
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_capture_document;
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.stopTimer();
    }

}
