package com.sisindia.ai.android.features.taskcheck.postcheck.signature;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.droidcommons.preference.Prefs;
import com.droidcommons.views.ink.InkView;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.room.dao.AttachmentDao;
import com.sisindia.ai.android.room.dao.DayCheckDao;
import com.sisindia.ai.android.room.entities.AttachmentEntity;
import com.sisindia.ai.android.room.entities.CheckedGuardEntity;
import com.sisindia.ai.android.uimodels.CheckedGuardItemModel;
import com.sisindia.ai.android.utils.FileUtils;
import com.sisindia.ai.android.utils.ImageUtils;

import org.threeten.bp.LocalDateTime;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class AddSignatureViewModel extends IopsBaseViewModel {

    public ObservableInt isSigned = new ObservableInt(GONE);

    public ObservableField<CheckedGuardItemModel> selectedGuardObs = new ObservableField<>(new CheckedGuardItemModel());

    public InkView.InkListener listener = new InkView.InkListener() {
        @Override
        public void onInkClear() {
            isSigned.set(GONE);
        }

        @Override
        public void onInkDraw() {
            isSigned.set(VISIBLE);
        }
    };
    @Inject
    public DayCheckDao dayCheckDao;

    @Inject
    public AttachmentDao attachmentDao;

    private CheckedGuardEntity checkedGuard;
    private AttachmentEntity attachment = new AttachmentEntity(AttachmentEntity.AttachmentSourceType.GUARD_SIGNATURE);

    @Inject
    public AddSignatureViewModel(@NonNull Application application) {
        super(application);
    }

    @MainThread
    public void onInkViewClear(InkView view) {
        view.clear();
    }

    @MainThread
    public void onAddSignatureContinue(InkView view) {

        if (view.isViewEmpty()) {
            showToast("Please add the signature to Continue..");
            return;
        }

        /*if (FileUtils.createOrExistsDir(FileUtils.DIR_ROOT)) {
            String imagePath = FileUtils.createTempFile(String.valueOf(attachment.attachmentSourceType));
            Bitmap bitmap = view.getBitmap(R.color.colorWhite);
            if (ImageUtils.save(bitmap, imagePath, Bitmap.CompressFormat.JPEG)) {
                File signFile = new File(imagePath);
                attachment.isFileSaved = true;
                attachment.fileSize = signFile.length() / 1024;
                attachment.localFilePath = Uri.fromFile(signFile).toString();
                onSignatureFileSaved(attachment);
            } else {
                showToast("Unable to add Signature..");
            }
        }*/

        String imagePath = FileUtils.createTempFileV2(String.valueOf(attachment.attachmentSourceType), view.getContext());
        Bitmap bitmap = view.getBitmap(R.color.colorWhite);
        if (ImageUtils.save(bitmap, imagePath, Bitmap.CompressFormat.JPEG)) {
            File signFile = new File(imagePath);
            attachment.isFileSaved = true;
            attachment.fileSize = signFile.length() / 1024;
            attachment.localFilePath = Uri.fromFile(signFile).toString();
            onSignatureFileSaved(attachment);
        } else {
            showToast("Unable to add Signature..");
        }
    }

    private void onSignatureFileSaved(AttachmentEntity attachment) {

        addDisposable(attachmentDao.insert(attachment)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onAttachmentDone, Timber::e));
    }

    private void onAttachmentDone(Long attachmentRow) {
        if (checkedGuard == null) {
            showToast("unable to save guard details");
            return;
        }
        checkedGuard.guardSignatureGuid = attachment.attachmentGuid;
        checkedGuard.currentState = CheckedGuardEntity.CurrentState.SIGNATURE.getGuardStatus();
        checkedGuard.updatedDateTime = LocalDateTime.now().toString();

        addDisposable(dayCheckDao.updateCheckedGuard(checkedGuard)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGuardUpdated, Timber::e));
    }

    private void onGuardUpdated(Integer integer) {
        message.what = NavigationConstants.OPEN_GUARD_SUMMARY;
        liveData.postValue(message);
    }


    void initViewModel() {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        int postId = Prefs.getInt(PrefConstants.CURRENT_POST);
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);
        int employeeId = Prefs.getInt(PrefConstants.SELECTED_EMPLOYEE_ID);

        addDisposable(dayCheckDao.fetchCheckedGuard(taskId, siteId, postId, employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> checkedGuard = item, Timber::e));

        addDisposable(dayCheckDao.fetchGuardDetailAfterScan(taskId, siteId, postId, employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> selectedGuardObs.set(item), Timber::e));
    }
}
