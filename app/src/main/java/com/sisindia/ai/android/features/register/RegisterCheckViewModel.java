package com.sisindia.ai.android.features.register;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.room.dao.AttachmentDao;
import com.sisindia.ai.android.room.dao.RegistersDao;
import com.sisindia.ai.android.room.dao.TaskDao;
import com.sisindia.ai.android.room.entities.CheckedRegisterEntity;
import com.sisindia.ai.android.room.entities.RegisterAttachmentEntity;
import com.sisindia.ai.android.uimodels.BreadCumItemModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.sisindia.ai.android.constants.PrefConstants.CURRENT_POST;
import static com.sisindia.ai.android.constants.PrefConstants.CURRENT_SITE;
import static com.sisindia.ai.android.constants.PrefConstants.CURRENT_TASK;

public class RegisterCheckViewModel extends IopsBaseViewModel {

    public PostRegisterRecyclerAdapter recyclerAdapter = new PostRegisterRecyclerAdapter();
    public ObservableField<List<BreadCumItemModel>> breadCums = new ObservableField<>(new ArrayList<>());

    @Inject
    public AttachmentDao attachmentDao;

    @Inject
    public TaskDao taskDao;

    private TaskTimer tik = new TaskTimer(0);

    @Inject
    public RegistersDao registersDao;

    public RegisterCheckViewListeners viewListeners = new RegisterCheckViewListeners() {

        @Override
        public void onRegisterItemClick(CheckedRegisterEntity item) {
            onRegisterUploadClick(item);
        }

        @Override
        public void onRemoveRegisterAttachment(RegisterAttachmentEntity item) {

        }
    };


    @Inject
    public RegisterCheckViewModel(@NonNull Application application) {
        super(application);
    }

    private void onRegisterUploadClick(CheckedRegisterEntity item) {
        if (item.isMissing) {
            showToast("Cannot upload missing document.");
            return;
        }
        message.what = NavigationConstants.ON_REGISTER_UPLOAD_CLICK;
        message.obj = item;
        liveData.postValue(message);
    }

    public void initViewModel() {
        int siteId = Prefs.getInt(CURRENT_SITE);
        int postId = Prefs.getInt(CURRENT_POST);
        int taskId = Prefs.getInt(CURRENT_TASK);
        List<BreadCumItemModel> items = new ArrayList<>();
        addDisposable(registersDao.fetchAllRegistersBySitePostId(siteId, postId, taskId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(registers -> {
                    recyclerAdapter.clear();
                    for (CheckedRegisterEntity reg : registers) {
                        items.add(BreadCumItemModel.registerCheckItem(reg.registerType, reg.isMissing || reg.noOfPages > 0));
                        addDisposable(registersDao.fetchLastTwoRegisterDocuments(taskId, siteId, postId, reg.registerTypeId)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe((documents, throwable) -> {
                                    reg.documents = documents;
                                    recyclerAdapter.add(reg);
                                }));
                    }
                    breadCums.set(items);
                }, Timber::e));
    }

    public void onCompleteRegisterCheck(View view) {
        insertCheckedRegister();
        message.what = NavigationConstants.ON_REGISTER_CHECK_DONE;
        liveData.postValue(message);
    }

    private void insertCheckedRegister() {
        List<CheckedRegisterEntity> items = recyclerAdapter.getItems();
        for (CheckedRegisterEntity item : items) {
            item.noOfPages = item.documents.size();
        }
        addDisposable(registersDao.insertAllCheckedRegister(items)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(row -> Timber.e("item inserted"), Timber::e));
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