package com.sisindia.ai.android.features.taskcheck.postcheck;

import android.app.Application;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.databinding.ObservableField;

import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.models.CheckListOptionResponseType;
import com.sisindia.ai.android.room.dao.AttachmentDao;
import com.sisindia.ai.android.room.dao.DayCheckDao;
import com.sisindia.ai.android.room.entities.AttachmentEntity;
import com.sisindia.ai.android.room.entities.CheckedPostCheckListEntity;
import com.sisindia.ai.android.room.entities.PostCheckListOptionEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class PostCheckListViewModel extends IopsBaseViewModel {

    public ObservableField<CheckedPostCheckListEntity> itemObs = new ObservableField<>(new CheckedPostCheckListEntity());
    public ObservableField<List<PostCheckListOptionEntity>> options = new ObservableField<>(new ArrayList<>());
    public ObservableField<PostCheckListOptionEntity> selectedOptionObs = new ObservableField<>(new PostCheckListOptionEntity());
    public ObservableField<String> remarks = new ObservableField<>("");

    @Inject
    public DayCheckDao dayCheckDao;

    @Inject
    public AttachmentDao attachmentDao;

    public ObservableField<Uri> imageUri = new ObservableField<>();

    @Inject
    public PostCheckListViewModel(@NonNull Application application) {
        super(application);
    }

    public void initViewModel() {
        CheckedPostCheckListEntity item = itemObs.get();
        if (item != null && item.siteId != 0) {
            addDisposable(dayCheckDao.fetchAllPostCheckListOptionsFor(item.siteId, item.postChecklistId, item.postId)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list -> options.set(list), Timber::e));
        }
    }


    public void onCheckedChange(RadioGroup group, int check) {
        for (int i = 0; i < group.getChildCount(); i++) {
            AppCompatRadioButton rb = (AppCompatRadioButton) group.getChildAt(i);
            if (rb.isChecked()) {
                PostCheckListOptionEntity item = (PostCheckListOptionEntity) rb.getTag();
                selectedOptionObs.set(item);
                return;
            } else {
                selectedOptionObs.set(new PostCheckListOptionEntity());
            }
        }
    }

    public void onCaptureImageForPostCheckListClick(View view) {
        message.what = NavigationConstants.ON_ADD_IMAGE_FOR_CHECK_LIST;
        liveData.postValue(message);
    }

    public void onPostCheckListSelected(View view) {
        PostCheckListOptionEntity selectedOption = selectedOptionObs.get();
        CheckedPostCheckListEntity item = itemObs.get();

        Uri image = imageUri.get();
        if (selectedOption != null && selectedOption.postChecklistId != 0 && item != null) {
            if (selectedOption.optionResponseType.equalsIgnoreCase(CheckListOptionResponseType.IMAGE.getResponseType()) && image == null) {
                showToast("Please add Image...");
                return;
            }

            if (image != null) {
                AttachmentEntity imageAttachment = new AttachmentEntity(image.toString());
                addDisposable(attachmentDao.insert(imageAttachment)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(attachmentId -> onAttachmentAdded(attachmentId, selectedOption, item), Timber::e));
            } else {
                onAttachmentAdded(0L, selectedOption, item);
            }
        } else if (item != null && item.checklistQuestionType.equals(CheckListOptionResponseType.TEXT.getResponseType())) {
            String addedRemarks = remarks.get();
            if (TextUtils.isEmpty(addedRemarks)) {
                showToast("please add remarks");
                return;
            }
            item.isEdited = true;
            item.remarks = addedRemarks;
            updatePostCheckList(item);
        } else {
            showToast("Please select an option");
        }
    }

    private void onAttachmentAdded(Long attachmentId, PostCheckListOptionEntity selectedOption, CheckedPostCheckListEntity item) {
        item.isEdited = true;
        item.imageAttachmentId = attachmentId.intValue();
        item.optionLabel = selectedOption.optionLabel;
        item.postChecklistOptionId = selectedOption.id;
        item.optionResponseType = selectedOption.optionResponseType;
        updatePostCheckList(item);
    }

    private void updatePostCheckList(CheckedPostCheckListEntity item) {
        addDisposable(dayCheckDao.updatePostCheckedList(item)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(row -> {
                    message.what = NavigationConstants.ON_ADD_POST_CHECK_LIST_DONE;
                    liveData.postValue(message);
                }, Timber::e));
    }

}
