package com.sisindia.ai.android.features.securityrisk;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.droidcommons.preference.Prefs;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.models.LookUpType;
import com.sisindia.ai.android.room.dao.AddSecurityRiskDao;
import com.sisindia.ai.android.room.dao.AttachmentDao;
import com.sisindia.ai.android.room.dao.LookUpDao;
import com.sisindia.ai.android.room.dao.TaskDao;
import com.sisindia.ai.android.room.entities.AddSecurityRiskEntity;
import com.sisindia.ai.android.room.entities.AttachmentEntity;
import com.sisindia.ai.android.uimodels.AddSecurityCategory;
import com.sisindia.ai.android.uimodels.attachments.AddSecurityRiskAttachmentMetaData;
import com.sisindia.ai.android.workers.AttachmentsUploadWorker;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.sisindia.ai.android.utils.FileUtils.EXT_JPG;

public class AddSecurityRiskViewModel extends IopsBaseViewModel {

    public ObservableField<AddSecurityPhotoState> photoState = new ObservableField<>(AddSecurityPhotoState.IDLE);
    public ObservableField<String> addSecurityRiskRemarks = new ObservableField<>("");
    public ObservableField<AddSecurityCategory> selectedCategoryObs = new ObservableField<>(new AddSecurityCategory());
    public ObservableField<List<AddSecurityCategory>> categoryObs = new ObservableField<>(new ArrayList<>());


    @Inject
    public AttachmentDao attachmentDao;

    @Inject
    public AddSecurityRiskDao addSecurityRiskDao;

    @Inject
    public LookUpDao lookUpDao;

    @Inject
    public TaskDao taskDao;

    public ObservableField<AttachmentEntity> imageAttachment = new ObservableField<>(new AttachmentEntity(AttachmentEntity.AttachmentSourceType.ADD_SECURITY));

    private TaskTimer tik = new TaskTimer(0);


    @Inject
    public AddSecurityRiskViewModel(@NonNull Application application) {
        super(application);
    }

    public void onCheckedChange(ChipGroup chipGroup, int check) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked()) {
                AddSecurityCategory category = (AddSecurityCategory) chip.getTag();
                if (category != null)
                    selectedCategoryObs.set(category);
                return;
            } else {
                selectedCategoryObs.set(new AddSecurityCategory());
            }
        }
    }

    public void onAddSecurityRisk(View view) {

        AddSecurityCategory category = selectedCategoryObs.get();
        if (category == null || category.id == 0) {
            showToast("Please select category");
            return;
        }

        String remarks = addSecurityRiskRemarks.get();
        if (TextUtils.isEmpty(remarks)) {
            showToast("Please add remarks");
            return;
        }

        if (remarks.trim().length() < 30) {
            showToast("Remarks too Short");
            return;
        }

        onAddSecurityRiskSave(category.id, remarks);

    }

    private void onAddSecurityRiskSave(int categoryId, String remarks) {
        int postId = Prefs.getInt(PrefConstants.CURRENT_POST);
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);
        AddSecurityRiskEntity item = new AddSecurityRiskEntity(taskId, siteId, postId, categoryId, remarks);

        AttachmentEntity attachmentItem = imageAttachment.get();
        if (attachmentItem != null && !TextUtils.isEmpty(attachmentItem.localFilePath)) {
            item.addSecurityGuid = attachmentItem.attachmentGuid;
            uploadAttachment(attachmentItem);
        }

        addDisposable(addSecurityRiskDao.insert(item)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rowId -> {
                    message.what = NavigationConstants.ON_ADD_SECURITY_DONE;
                    liveData.postValue(message);
                }, Timber::e));
    }

    private void uploadAttachment(AttachmentEntity attachmentItem) {
        int postId = Prefs.getInt(PrefConstants.CURRENT_POST);
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);

        addDisposable(taskDao.getAttachmentTypeForAddSecurityRisk(attachmentItem.attachmentSourceType, attachmentItem.attachmentGuid, taskId, postId)
                .compose(transformSingle()).subscribe(attachmentModel -> {
                    attachmentItem.storagePath = attachmentModel.getStoragePath().concat(EXT_JPG);
                    AddSecurityRiskAttachmentMetaData metaData = new AddSecurityRiskAttachmentMetaData();
                    metaData.attachmentTypeId = 1;//for image
                    metaData.attachmentSourceTypeId = attachmentItem.attachmentSourceType;
                    metaData.uuid = attachmentItem.attachmentGuid;
                    metaData.fileName = attachmentModel.getFileName().concat(EXT_JPG);
                    metaData.fileSize = String.valueOf(attachmentItem.fileSize);
                    metaData.storagePath = attachmentItem.storagePath;
                    metaData.siteId = siteId;
                    metaData.postId = postId;
                    metaData.taskId = attachmentModel.getServerId();

                    attachmentItem.attachmentMetaData = new Gson().toJson(metaData);
                    attachmentItem.isDone = true;
                    addDisposable(attachmentDao.insert(attachmentItem)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(row -> {
                                oneTimeWorkerWithNetwork(AttachmentsUploadWorker.class);
                                Timber.d("Attachment Added");
                            }, Timber::e));
                }, Timber::e));
    }

    public void onAddSecurityImageClear(View view) {
        photoState.set(AddSecurityPhotoState.IDLE);
        imageAttachment.set(new AttachmentEntity(AttachmentEntity.AttachmentSourceType.ADD_SECURITY));
    }

    public void onAddSecurityImageCapture(View view) {
        message.what = NavigationConstants.ON_ADD_SECURITY_IMAGE_CAPTURE;
        message.obj = imageAttachment.get();
        liveData.postValue(message);
    }


    public void initViewModel() {
        //fetch all category from lookUp
        addDisposable(lookUpDao.fetchAllCategoryForAddSecurity(LookUpType.SECURITY_RISK_CATEGORY.getTypeId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> categoryObs.set(items), Timber::e));
    }

    void startTimer() {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        addDisposable(taskDao.fetchTimeSpentViaTaskId(taskId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    tik = new TaskTimer(model.getTimeElapsed() - findTimeDifference(model.getLastElapsedTime()));
                    tik.start();
                }, Timber::e));
    }

    void stopTimer() {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        addDisposable(taskDao.updateTimeSpentByTaskId(taskId, tik.lastTik, getCurrentMilliOfTheDay())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rowId -> {
                    tik.cancel();
                }, Timber::e));
    }

    public enum AddSecurityPhotoState {
        IDLE, CAPTURED
    }

}
