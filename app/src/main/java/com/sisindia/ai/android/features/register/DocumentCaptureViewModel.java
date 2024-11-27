package com.sisindia.ai.android.features.register;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.droidcommons.preference.Prefs;
import com.google.gson.Gson;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.room.dao.AttachmentDao;
import com.sisindia.ai.android.room.dao.RegistersDao;
import com.sisindia.ai.android.room.dao.TaskDao;
import com.sisindia.ai.android.room.entities.AttachmentEntity;
import com.sisindia.ai.android.room.entities.CheckedRegisterEntity;
import com.sisindia.ai.android.room.entities.RegisterAttachmentEntity;
import com.sisindia.ai.android.uimodels.attachments.RegisterAttachmentMetaData;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.sisindia.ai.android.utils.FileUtils.EXT_JPG;

public class DocumentCaptureViewModel extends IopsBaseViewModel {

    @Inject
    public RegistersDao registersDao;

    @Inject
    public AttachmentDao attachmentDao;

    @Inject
    public TaskDao taskDao;

    public CapturedDocumentRecyclerAdapter recyclerAdapter = new CapturedDocumentRecyclerAdapter();

    public ObservableField<CheckedRegisterEntity> selectedItem = new ObservableField<>();

    public RegisterCheckViewListeners viewListeners = new RegisterCheckViewListeners() {

        @Override
        public void onRegisterItemClick(CheckedRegisterEntity item) {

        }

        @Override
        public void onRemoveRegisterAttachment(RegisterAttachmentEntity item) {
            deleteRegisterAttachment(item);
        }
    };
    private TaskTimer tik = new TaskTimer(0);


    @Inject
    public DocumentCaptureViewModel(@NonNull Application application) {
        super(application);
    }

    private void deleteRegisterAttachment(RegisterAttachmentEntity item) {

        addDisposable(attachmentDao.deleteAttachmentRecord(item.registerAttachmentId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Timber.i("deleted"), Timber::e));
        addDisposable(registersDao.deleteRegisterAttachment(item)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(row -> Timber.i("deleted"), Timber::e));
    }

    public void onCaptureDocumentClick(View view) {
        CheckedRegisterEntity item = selectedItem.get();
        if (item == null) {
            showToast("unable to capture ... please try again");
            return;
        }
        AttachmentEntity attachment = new AttachmentEntity(AttachmentEntity.AttachmentSourceType.of(item.attachmentSourceTypeId));
        message.what = NavigationConstants.ON_CAPTURE_DOCUMENT;
        message.obj = attachment;
        liveData.postValue(message);
    }

    public void onSaveAllCapturedDocumentClick(View view) {
        addDisposable(registersDao.insertAllRegisterAttachments(recyclerAdapter.getItems())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(row -> Timber.d("inserted"), Timber::e));
        message.what = NavigationConstants.ON_CAPTURE_DOCUMENT_DONE;
        liveData.postValue(message);
    }

    public void initViewModel() {
        CheckedRegisterEntity item = selectedItem.get();
        if (item == null) {
            showToast("unable to capture ... please try again");
            return;
        }
        addDisposable(registersDao.fetAllRegistersByTaskSitePostRegisterId(item.taskId, item.siteId, item.postId, item.registerTypeId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> recyclerAdapter.clearAndSetItems(items), Timber::e));
    }

    public void onDocumentCaptured(AttachmentEntity attachment) {
        CheckedRegisterEntity item = selectedItem.get();
        if (item == null) {
            showToast("unable to capture ... please try again");
            return;
        }

        //getAttachmentTypeForRegister
        addDisposable(attachmentDao.getAttachmentTypeForRegister(attachment.attachmentSourceType, attachment.attachmentGuid, item.taskId, item.postId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(attachmentModel -> {
                    attachment.storagePath = attachmentModel.getStoragePath().concat(EXT_JPG);
                    RegisterAttachmentMetaData metaData = new RegisterAttachmentMetaData();
                    metaData.attachmentTypeId = 1;//for image
                    metaData.attachmentSourceTypeId = attachment.attachmentSourceType;
                    metaData.uuid = attachment.attachmentGuid;
                    metaData.fileName = attachmentModel.getFileName().concat(EXT_JPG);
                    metaData.fileSize = String.valueOf(attachment.fileSize);
                    metaData.storagePath = attachment.storagePath;
                    metaData.siteId = item.siteId;
                    metaData.postId = item.postId;
                    metaData.taskId = attachmentModel.getServerId();
                    attachment.attachmentMetaData = new Gson().toJson(metaData);
                    attachment.isDone = true;
                    attachment.imageText = TextUtils.concat(attachmentModel.getTaskType(), " ", attachmentModel.getSiteCode()).toString();
                    addDisposable(attachmentDao.insert(attachment)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(attachmentId -> {
                                RegisterAttachmentEntity entity = new RegisterAttachmentEntity(item.taskId, item.siteId, item.postId, item.registerTypeId, attachmentId.intValue(), attachment.localFilePath, attachment.attachmentGuid);
                                recyclerAdapter.add(entity);
                            }, Timber::e));
                }, Timber::e));
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
}
