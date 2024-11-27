package com.sisindia.ai.android.features.taskcheck.postcheck.summary;

import static com.sisindia.ai.android.room.entities.AttachmentEntity.AttachmentSourceType.GUARD_FULL_PHOTO;
import static com.sisindia.ai.android.room.entities.AttachmentEntity.AttachmentSourceType.GUARD_SIGNATURE;
import static com.sisindia.ai.android.room.entities.AttachmentEntity.AttachmentSourceType.SLEEPING_GUARD;
import static com.sisindia.ai.android.utils.FileUtils.EXT_JPG;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.droidcommons.preference.Prefs;
import com.google.gson.Gson;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.features.addgrievances.GrievanceRecyclerAdapter;
import com.sisindia.ai.android.features.addkitrequest.KitRequestRecyclerAdapter;
import com.sisindia.ai.android.room.dao.AttachmentDao;
import com.sisindia.ai.android.room.dao.DayCheckDao;
import com.sisindia.ai.android.room.dao.GrievanceDao;
import com.sisindia.ai.android.room.dao.GuardKitRequestDao;
import com.sisindia.ai.android.room.dao.TaskDao;
import com.sisindia.ai.android.room.entities.AttachmentEntity;
import com.sisindia.ai.android.room.entities.CheckedGuardEntity;
import com.sisindia.ai.android.uimodels.AttachmentModel;
import com.sisindia.ai.android.uimodels.GuardSummaryModel;
import com.sisindia.ai.android.uimodels.attachments.GuardAttachmentMetaData;
import com.sisindia.ai.android.workers.AttachmentsUploadWorker;

import org.threeten.bp.LocalDateTime;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class GuardSummaryViewModel extends IopsBaseViewModel {

    public GrievanceRecyclerAdapter grievanceRecyclerAdapter = new GrievanceRecyclerAdapter();//grievanceAllCount
    public KitRequestRecyclerAdapter kitRequestRecyclerAdapter = new KitRequestRecyclerAdapter();
    public ObservableField<GuardSummaryModel> item = new ObservableField<>(new GuardSummaryModel());
    public ObservableInt grievanceAllCount = new ObservableInt(0);
    public ObservableInt kitRequestAllCount = new ObservableInt(0);
    //    public ObservableInt countryId = new ObservableInt(Prefs.getInt(PrefConstants.COUNTRY_ID, 1));
    public ObservableInt companyId = new ObservableInt(Prefs.getInt(PrefConstants.COMPANY_ID, 1));

    @Inject
    public DayCheckDao dayCheckDao;

    @Inject
    public GrievanceDao grievanceDao;

    @Inject
    public AttachmentDao attachmentDao;

    @Inject
    public GuardKitRequestDao kitRequestDao;

    @Inject
    public TaskDao taskDao;

    private TaskTimer tik = new TaskTimer(0);

    private CheckedGuardEntity checkedGuard;

    @Inject
    public GuardSummaryViewModel(@NonNull Application application) {
        super(application);
    }

    public void initViewModel() {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        int postId = Prefs.getInt(PrefConstants.CURRENT_POST);
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);
        int employeeId = Prefs.getInt(PrefConstants.SELECTED_EMPLOYEE_ID);

        addDisposable(dayCheckDao.fetchGuardSummary(taskId, siteId, postId, employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGuardDetailsFetch, Timber::e));

        addDisposable(dayCheckDao.fetchCheckedGuard(taskId, siteId, postId, employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> checkedGuard = item, Timber::e));

        addDisposable(grievanceDao.fetchAllOpenGrievanceByEmployeeId(employeeId, siteId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    grievanceAllCount.set(list.size());
                    grievanceRecyclerAdapter.clearAndSetItems(list);
                }, Timber::e));

        addDisposable(kitRequestDao.fetchAllEmployeeKitRequests(employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    kitRequestAllCount.set(list.size());
                    kitRequestRecyclerAdapter.clearAndSetItems(list);
                }, Timber::e));
    }

    private void onGuardDetailsFetch(GuardSummaryModel model) {
        item.set(model);

        if (!TextUtils.isEmpty(model.sleepingGuardGuid)) {

            addDisposable(Single.zip(
                    taskDao.getAttachmentTypeForGuard(SLEEPING_GUARD.getSourceType(), model.employeeId, model.sleepingGuardGuid, model.taskId, model.postId),
                    attachmentDao.fetchAttachmentByGuid(model.sleepingGuardGuid),
                    (guardAttachmentModel, attachmentItem) -> {
                        onAttachmentFetch(guardAttachmentModel, attachmentItem, model);
                        return true;
                    }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe((aBoolean, throwable) -> Timber.d("")));

        }

        if (!TextUtils.isEmpty(model.guardEvaluationGuid)) {

            addDisposable(Single.zip(
                    taskDao.getAttachmentTypeForGuard(GUARD_FULL_PHOTO.getSourceType(), model.employeeId, model.guardEvaluationGuid, model.taskId, model.postId),
                    attachmentDao.fetchAttachmentByGuid(model.guardEvaluationGuid),
                    (guardAttachmentModel, attachmentItem) -> {
                        onAttachmentFetch(guardAttachmentModel, attachmentItem, model);
                        return true;
                    }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe((aBoolean, throwable) -> Timber.d("")));
        }

        if (!TextUtils.isEmpty(model.guardSignatureGuid)) {

            addDisposable(Single.zip(
                    taskDao.getAttachmentTypeForGuard(GUARD_SIGNATURE.getSourceType(), model.employeeId, model.guardSignatureGuid, model.taskId, model.postId),
                    attachmentDao.fetchAttachmentByGuid(model.guardSignatureGuid),
                    (guardAttachmentModel, attachmentItem) -> {
                        onAttachmentFetch(guardAttachmentModel, attachmentItem, model);
                        return true;
                    }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe((aBoolean, throwable) -> Timber.d("")));
        }

    }

    private void onAttachmentFetch(AttachmentModel model, AttachmentEntity item, GuardSummaryModel guardSummaryModel) {
        if (!item.isDone) {
            item.storagePath = Objects.requireNonNull(model.getStoragePath()).concat(EXT_JPG);
            GuardAttachmentMetaData metaData = new GuardAttachmentMetaData();
            metaData.attachmentTypeId = 1;//for image
            metaData.attachmentSourceTypeId = item.attachmentSourceType;
            metaData.uuid = item.attachmentGuid;
            metaData.fileName = Objects.requireNonNull(model.getFileName()).concat(EXT_JPG);
            metaData.fileSize = String.valueOf(item.fileSize);
            metaData.storagePath = item.storagePath;
            metaData.siteId = guardSummaryModel.siteId;
            metaData.postId = guardSummaryModel.postId;
            metaData.taskId = guardSummaryModel.serverId;
            metaData.employeeId = guardSummaryModel.employeeId;
            metaData.employeeNo = guardSummaryModel.employeeNo;

            item.attachmentMetaData = new Gson().toJson(metaData);
            item.isDone = true;
//            item.imageText = TextUtils.concat(model.getTaskType(), " ", model.getSiteCode(), " ", guardSummaryModel.employeeNo).toString();
            item.imageText = TextUtils.concat(model.getTaskType(), " ", model.getSiteCode()).toString();
            addDisposable(attachmentDao.insert(item)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(row -> Timber.d("Attachment Added"), Timber::e));
        }
    }

    /**
     * create grievance for selected guard
     */
    public void onAddGrievanceClick(View view) {
        message.arg1 = Prefs.getInt(PrefConstants.SELECTED_EMPLOYEE_ID);
        message.what = NavigationConstants.ON_ADD_GRIEVANCE_CLICK;
        liveData.postValue(message);
    }

    public void onAddKitRequestClick(View view) {
        message.arg1 = Prefs.getInt(PrefConstants.SELECTED_EMPLOYEE_ID);
        message.what = NavigationConstants.ON_ADD_KIT_REQUEST_CLICK;
        liveData.postValue(message);
    }

    public void onGuardCheckCompleteClick(View view) {
        checkedGuard.updatedDateTime = LocalDateTime.now().toString();
        checkedGuard.checkedGuardStatus = CheckedGuardEntity.CheckedGuardStatus.COMPLETED.getCheckedGuardStatus();
        addDisposable(dayCheckDao.insertCheckedGuard(checkedGuard)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(row -> {
                    oneTimeWorkerWithNetwork(AttachmentsUploadWorker.class);
                    message.what = NavigationConstants.ON_GUARD_CHECK_SUCCESS;
                    liveData.postValue(message);
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
                .subscribe(rowId -> tik.cancel(), Timber::e));
    }
}
