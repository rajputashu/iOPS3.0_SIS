package com.sisindia.ai.android.features.reviewinformation;

import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_DAY_CHECK_SCREEN;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.work.Data;

import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.models.ReviewInformationResponse;
import com.sisindia.ai.android.models.TableSyncResponse;
import com.sisindia.ai.android.room.dao.DayCheckDao;
import com.sisindia.ai.android.room.dao.SiteDao;
import com.sisindia.ai.android.room.dao.TaskDao;
import com.sisindia.ai.android.room.entities.DutyStatusEntity;
import com.sisindia.ai.android.uimodels.ReviewInformationModel;
import com.sisindia.ai.android.workers.RotaTaskWorker;
import com.sisindia.ai.android.workers.SiteConfigurationWorker;

import org.threeten.bp.LocalTime;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ReviewInformationViewModel extends IopsBaseViewModel {

    public ObservableField<ReviewInformationModel> item = new ObservableField<>(new ReviewInformationModel());
    public ObservableField<ReviewInformationResponse.IssueSummary> issueSummaryObs = new ObservableField<>(new ReviewInformationResponse.IssueSummary());
    public ObservableField<ReviewInformationResponse.IssueSummary> obsLastVisit = new ObservableField<>(new ReviewInformationResponse.IssueSummary());
    public ObservableField<List<String>> qrCodesList = new ObservableField<>();

    @Inject
    public TaskDao taskDao;

    @Inject
    public SiteDao siteDao;

    @Inject
    public DayCheckDao dayCheckDao;

    @Inject
    public ReviewInformationViewModel(@NonNull Application application) {
        super(application);
    }

    public void onStartDayNightCheckClick(View view) {
        ReviewInformationModel model = item.get();
        if (model != null && model.serverId == 0) {
//            initViewModel();
            syncPendingAdHocTaskToServer();
            showToast(R.string.rotaNotSyncedMsg);
            message.what = NavigationConstants.ON_FINISH_ACTIVITY;
            liveData.postValue(message);
            return;
        }

        if (model != null && model.taskTypeId == 2) {
            if (LocalTime.now().getHour() > 22)
                openDcNCScreen();
            else if (LocalTime.now().getHour() == 0 || LocalTime.now().getHour() < 6)
                openDcNCScreen();
            else
                showToast("Night check is allowed between 11:00 PM to 5:00 AM");
        } else if (model != null && model.taskTypeId == 1) {
            /*if (LocalTime.now().getHour() > 22)
                showToast("Day check is not allowed between 11:00 PM to 5:00 AM");*/
            if (LocalTime.now().getHour() >= 22)
                showToast("Day check task is allowed only between 5:00 AM to 10:00 PM");
            else if (LocalTime.now().getHour() == 0 || LocalTime.now().getHour() < 5)
                showToast("Day check task is allowed only between 5:00 AM to 10:00 PM");
            else
                openDcNCScreen();
        }
    }

    private void openDcNCScreen() {
        //Introducing below method call : to sync DutyOn/Off data to server
        uploadDutyStatusToServer();
        message.what = OPEN_DAY_CHECK_SCREEN;
        liveData.postValue(message);
    }

    public void initViewModel() {
//        syncPendingAdHocTaskToServer();
        Data siteConfigure = new Data.Builder().build();
        oneTimeBackgroundWorker(SiteConfigurationWorker.class, siteConfigure);

        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);

        addDisposable(taskDao.getAllQRCodesAtSite(siteId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(qrList -> qrCodesList.set(qrList), Timber::e));

        addDisposable(taskDao.fetchReviewInformationByTaskId(taskId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCurrentTaskFetch, Timber::e));

        addDisposable(siteDao.fetchSiteLastVisitDetail(siteId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLastVisitFetch, Timber::e));
    }

    private void syncPendingAdHocTaskToServer() {
        Data inputData = new Data.Builder()
                .putInt(RotaTaskWorker.class.getSimpleName(), RotaTaskWorker.RotaTaskWorkerType.SYNC_TO_SERVER.getWorkerType())
                .build();

//        oneTimeWorkerWithInputData(RotaTaskWorker.class, inputData);
        oneTimeKeepWorkerWithInputData(RotaTaskWorker.class, inputData);
    }

    private void onLastVisitFetch(ReviewInformationModel.LastVisitDetail lastVisit) {
//        ReviewInformationResponse.IssueSummary issueSummary = issueSummaryObs.get();
        ReviewInformationResponse.IssueSummary issueSummary = obsLastVisit.get();
        if (issueSummary != null) {
            issueSummary.lastTaskDone = lastVisit.lastVisitDateTime;
            issueSummary.taskType = lastVisit.activityName;
            issueSummary.authorizedGuards = lastVisit.authorizedGuards;
            issueSummary.checkedGuards = lastVisit.checkedGuards;
            issueSummary.taskType = lastVisit.activityName;
            /*issueSummaryObs.set(issueSummary);
            issueSummaryObs.notifyChange();*/
            obsLastVisit.set(issueSummary);
            obsLastVisit.notifyChange();
        }
    }

    private void onCurrentTaskFetch(ReviewInformationModel model) {
        this.item.set(model);
        ReviewInformationResponse.IssueSummary issueSummary = issueSummaryObs.get();
        if (issueSummary != null) {
            issueSummary.grievances = model.pendingGrievances;
            issueSummary.complaints = model.pendingComplaints;
            issueSummaryObs.set(issueSummary);
            issueSummaryObs.notifyChange();
        }
        Prefs.putInt(PrefConstants.TASK_SERVER_ID, model.serverId);
        setIsLoading(true);
        addDisposable(coreApi.getIssueSummary(model.siteId)
                .compose(transformSingle())
                .subscribe(this::onIssueSummaryResponse, this::onApiError));
    }

    private void onIssueSummaryResponse(ReviewInformationResponse response) {
        setIsLoading(false);
        if (response.statusCode == 200) {
            issueSummaryObs.set(response.issueSummary);
            issueSummaryObs.notifyChange();
        }
    }

    private void uploadDutyStatusToServer() {
        addDisposable(dutyStatusDao.fetchNotSyncDutyStatus(false)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onItemsFetched, e -> {
                }));
    }

    private void onItemsFetched(DutyStatusEntity item) {
        addDisposable(coreApi.isDutyOnOff(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.statusCode == 200)
                        onTableSynced(response.data, item);
                }, Timber::e));
    }

    private void onTableSynced(TableSyncResponse.TableSyncData data, DutyStatusEntity item) {
        if (data != null && data.serverId != 0) {
            addDisposable(dutyStatusDao.updateOnSyncToServer(item.localId, data.serverId, true)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rowId -> Timber.d("ReviewInfo Duty status row id %s", rowId), Timber::e));
        }
    }
}
