package com.sisindia.ai.android.features.taskcheck;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.databinding.ObservableField;

import com.droidcommons.preference.Prefs;
import com.google.gson.Gson;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.models.CheckListOptionResponseType;
import com.sisindia.ai.android.room.dao.AttachmentDao;
import com.sisindia.ai.android.room.dao.DayCheckDao;
import com.sisindia.ai.android.room.entities.AttachmentEntity;
import com.sisindia.ai.android.room.entities.CheckedSiteCheckListEntity;
import com.sisindia.ai.android.room.entities.SiteCheckListOptionEntity;
import com.sisindia.ai.android.uimodels.attachments.SiteCheckListAttachmentMetaData;
import com.sisindia.ai.android.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SiteCheckListViewModel extends IopsBaseViewModel {

    public ObservableField<CheckedSiteCheckListEntity> itemObs = new ObservableField<>(new CheckedSiteCheckListEntity());
    public ObservableField<List<SiteCheckListOptionEntity>> options = new ObservableField<>(new ArrayList<>());
    public ObservableField<SiteCheckListOptionEntity> selectedOptionObs = new ObservableField<>(new SiteCheckListOptionEntity());

    @Inject
    public DayCheckDao dayCheckDao;

    @Inject
    public AttachmentDao attachmentDao;

    public ObservableField<AttachmentEntity> siteCheckAttachment = new ObservableField<>(new AttachmentEntity(AttachmentEntity.AttachmentSourceType.SITE_CHECKLIST));

    @Inject
    public SiteCheckListViewModel(@NonNull Application application) {
        super(application);
    }

    public void initViewModel() {
        CheckedSiteCheckListEntity item = itemObs.get();
        if (item != null && item.siteId != 0) {
            addDisposable(dayCheckDao.fetchAllSiteCheckListOptionsFor(item.siteId, item.siteChecklistId)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list -> options.set(list), Timber::e));
        }
    }

    public void onCheckedChange(RadioGroup group, int check) {
        for (int i = 0; i < group.getChildCount(); i++) {
            AppCompatRadioButton rb = (AppCompatRadioButton) group.getChildAt(i);
            if (rb.isChecked()) {
                SiteCheckListOptionEntity item = (SiteCheckListOptionEntity) rb.getTag();
                selectedOptionObs.set(item);
                return;
            } else {
                selectedOptionObs.set(new SiteCheckListOptionEntity());
            }
        }
    }

    public void onCaptureImageForSiteCheckListClick(View view) {
        message.what = NavigationConstants.ON_ADD_IMAGE_FOR_CHECK_LIST;
        message.obj = siteCheckAttachment.get();
        liveData.postValue(message);
    }

    public void onSiteCheckListSelected(View view) {
        SiteCheckListOptionEntity selectedOption = selectedOptionObs.get();
        CheckedSiteCheckListEntity item = itemObs.get();

        AttachmentEntity image = siteCheckAttachment.get();
        if (selectedOption != null && selectedOption.siteChecklistId != 0 && item != null) {
            if (selectedOption.optionResponseType.equalsIgnoreCase(CheckListOptionResponseType.IMAGE.getResponseType()) &&
                    TextUtils.isEmpty(image != null ? image.localFilePath : null)) {
                showToast("Please add image...");
                return;
            }

            if (image != null && image.localFilePath != null) {
                item.imageAttachmentId = image.attachmentGuid;
                uploadAttachment(image);
            }

            item.isEdited = true;
            item.optionLabel = selectedOption.optionLabel;
            item.siteChecklistOptionId = selectedOption.id;
            item.optionResponseType = selectedOption.optionResponseType;

            addDisposable(dayCheckDao.updateSiteCheckedList(item)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(row -> {
                        message.what = NavigationConstants.ON_ADD_SITE_CHECK_LIST_DONE;
                        liveData.postValue(message);
                    }, Timber::e));
        } else {
            showToast("Please select an option");
        }
    }

    private void onAttachmentAdded(Long attachmentId, SiteCheckListOptionEntity selectedOption, CheckedSiteCheckListEntity item) {
        item.isEdited = true;
        item.optionLabel = selectedOption.optionLabel;
        item.siteChecklistOptionId = selectedOption.id;
        item.optionResponseType = selectedOption.optionResponseType;
        addDisposable(dayCheckDao.updateSiteCheckedList(item)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(row -> {
                    message.what = NavigationConstants.ON_ADD_SITE_CHECK_LIST_DONE;
                    liveData.postValue(message);
                }, Timber::e));
    }

    private void uploadAttachment(AttachmentEntity attachmentEntity) {
        addDisposable(dayCheckDao.getAttachmentForSiteCheckList(attachmentEntity.attachmentSourceType,
                attachmentEntity.attachmentGuid, Prefs.getInt(PrefConstants.CURRENT_TASK))
                .compose(transformSingle())
                .subscribe(attachmentModel -> {
                    attachmentEntity.storagePath = Objects.requireNonNull(attachmentModel.getStoragePath()).concat(FileUtils.EXT_JPG);
                    SiteCheckListAttachmentMetaData metaData = new SiteCheckListAttachmentMetaData();
                    metaData.attachmentTypeId = 1;
                    metaData.attachmentSourceTypeId = attachmentEntity.attachmentSourceType;
                    metaData.uuid = attachmentEntity.attachmentGuid;
                    metaData.fileName = Objects.requireNonNull(attachmentModel.getFileName()).concat(FileUtils.EXT_JPG);
                    metaData.fileSize = String.valueOf(attachmentEntity.fileSize);
                    metaData.storagePath = attachmentEntity.storagePath;
                    metaData.setSiteId(Prefs.getInt(PrefConstants.CURRENT_SITE));
                    metaData.setTaskId(attachmentModel.getServerId());

                    attachmentEntity.attachmentMetaData = new Gson().toJson(metaData);
                    attachmentEntity.isDone = true;
                    addDisposable(attachmentDao.insert(attachmentEntity)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(row -> Timber.d("SiteCheckListAttachment Added"), Timber::e));

                }, Timber::e));
    }
}
