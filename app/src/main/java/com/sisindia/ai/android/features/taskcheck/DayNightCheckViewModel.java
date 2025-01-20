package com.sisindia.ai.android.features.taskcheck;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_CLIENT_HANDSHAKE_SCREEN;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_PRE_POST_CHECK_SCREEN;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_STRENGTH_CHECK_SCREEN;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.work.Data;

import com.droidcommons.preference.Prefs;
import com.google.gson.Gson;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.commons.CheckInStatus;
import com.sisindia.ai.android.commons.GroupCompany;
import com.sisindia.ai.android.commons.MenuNavigationItem;
import com.sisindia.ai.android.commons.MenuNavigationListeners;
import com.sisindia.ai.android.commons.MenuNavigationRecyclerAdapter;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.models.StrengthCheckModel;
import com.sisindia.ai.android.room.dao.DailyTimeLineDao;
import com.sisindia.ai.android.room.dao.DayCheckDao;
import com.sisindia.ai.android.room.dao.RegistersDao;
import com.sisindia.ai.android.room.dao.SitePostDao;
import com.sisindia.ai.android.room.dao.SiteStrengthDao;
import com.sisindia.ai.android.room.dao.TaskDao;
import com.sisindia.ai.android.room.entities.CheckedPostEntity;
import com.sisindia.ai.android.room.entities.CheckedSiteCheckListEntity;
import com.sisindia.ai.android.room.entities.DailyTimeLineEntity;
import com.sisindia.ai.android.room.entities.RegisterAttachmentEntity;
import com.sisindia.ai.android.room.entities.TaskEntity;
import com.sisindia.ai.android.uimodels.BreadCumItemModel;
import com.sisindia.ai.android.uimodels.CheckedPostModel;
import com.sisindia.ai.android.uimodels.DayNightCheckData;
import com.sisindia.ai.android.uimodels.DayNightCheckModel;
import com.sisindia.ai.android.uimodels.ScanPostQRModel;
import com.sisindia.ai.android.uimodels.TaskValidationModel;
import com.sisindia.ai.android.utils.TimeUtils;
import com.sisindia.ai.android.workers.AddRewardFineWorker;
import com.sisindia.ai.android.workers.RotaTaskWorker;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class DayNightCheckViewModel extends IopsBaseViewModel {

    public ObservableField<DayNightCheckModel> item = new ObservableField<>(new DayNightCheckModel());
    public ObservableField<StrengthCheckModel> strength = new ObservableField<>(new StrengthCheckModel());
    public ObservableField<DayNightCheckData.ClientHandShakeData> client = new ObservableField<>(new DayNightCheckData.ClientHandShakeData());
    public ObservableField<List<BreadCumItemModel>> breadCums = new ObservableField<>(new ArrayList<>());
    public MenuNavigationRecyclerAdapter menuNavigationAdapter = new MenuNavigationRecyclerAdapter();
    public MenuNavigationListeners menuNavigationListeners = DayNightCheckViewModel.this::onMenuNavigationClick;
    public PostCheckRecyclerAdapter postCheckRecyclerAdapter = new PostCheckRecyclerAdapter();
    public SiteCheckListRecyclerAdapter siteCheckListAdapter = new SiteCheckListRecyclerAdapter();
    public ObservableInt noOfPosts = new ObservableInt(0);
    public ObservableInt noOfCheckLists = new ObservableInt(0);
    public ObservableInt strengthVisibility = new ObservableInt(GONE);
    public ObservableInt clientHandShakeVisibility = new ObservableInt(GONE);
    public ObservableField<Boolean> isStrengthChecked = new ObservableField<>(false);
    public ObservableField<String> checkedInTime = new ObservableField<>("");
    public ObservableInt showCheckInBar = new ObservableInt(GONE);

    @Inject
    public TaskDao taskDao;

    @Inject
    public DayCheckDao dayCheckDao;

    @Inject
    public RegistersDao registersDao;

    @Inject
    public SiteStrengthDao strengthDao;

    @Inject
    SitePostDao sitePostDao;
    public ObservableField<List<String>> qrCodesList = new ObservableField<>();

    public DayCheckViewListeners dayCheckViewListeners = new DayCheckViewListeners() {

        @Override
        public void onCheckedPostClick(CheckedPostModel item) {
            onPendingCheckedPostClick(item);
        }

        @Override
        public void onSiteCheckListItemClick(CheckedSiteCheckListEntity item) {
            message.obj = item;
            message.what = NavigationConstants.ON_SITE_CHECK_LIST_ITEM_CLICK;
            liveData.postValue(message);
        }
    };
    @Inject
    DailyTimeLineDao dailyTimeLineDao;

    private TaskTimer tik = new TaskTimer(0);

    @Inject
    public DayNightCheckViewModel(@NonNull Application application) {
        super(application);
    }

    public void initViewModel() {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);
        if (taskId != 0) {

            /* Get updated task item */
            addDisposable(taskDao.fetchStartDayCheckModelByTaskId(taskId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onCurrentTaskFetch, Timber::e));

            /* get all Checked Posts for task*/
            addDisposable(dayCheckDao.fetchCheckedPostBySite(taskId, siteId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onCheckedPostFetch, Timber::e));

            /* get All site check list*/
            addDisposable(dayCheckDao.fetchAllCheckedSiteCheckListByTaskAndSite(taskId, siteId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onCheckedSiteCheckListFetch, Timber::e));

            /* get Strength Check */
            addDisposable(strengthDao.fetchCheckedStrengthCheck(taskId)
                    .compose(transformSingle())
                    .subscribe(this::onStrengthCheckFetch, t -> strengthVisibility.set(GONE)));

            /* get Client Hand Shake*/
            addDisposable(dayCheckDao.fetchClientHandShakeData(taskId)
                    .compose(transformSingle())
                    .subscribe(this::onClientHandShakeFetch, t -> clientHandShakeVisibility.set(GONE)));
        }
    }

    private void onClientHandShakeFetch(DayNightCheckData.ClientHandShakeData data) {
        clientHandShakeVisibility.set(data.isMetClient ? VISIBLE : GONE);
        client.set(data);
    }

    private void onStrengthCheckFetch(StrengthCheckModel model) {
        strengthVisibility.set(VISIBLE);
        strength.set(model);
        if (model.pendingStrengthCheck == 0) // StrengthCheck is done
            isStrengthChecked.set(true);
    }

    private void onCheckedSiteCheckListFetch(List<CheckedSiteCheckListEntity> list) {
        noOfCheckLists.set(list.size());
        siteCheckListAdapter.clearAndSetItems(list);
    }

    private void onPendingCheckedPostClick(CheckedPostModel item) {
        Prefs.putInt(PrefConstants.CURRENT_POST, item.postId);
        message.what = NavigationConstants.ON_SITE_POST_CLICK;
        liveData.postValue(message);
    }

    private void onCheckedPostFetch(List<CheckedPostModel> list) {
        noOfPosts.set(list.size());
        postCheckRecyclerAdapter.clearAndSetItems(list);
    }

    private void onMenuNavigationClick(MenuNavigationItem model) {
        switch (model.itemType) {

            case POST_CHECK:
                message.what = OPEN_PRE_POST_CHECK_SCREEN;
                break;
            case STRENGTH_CHECK:
                message.what = OPEN_STRENGTH_CHECK_SCREEN;
                break;
            case CLIENT_HAND_SHAKE:
                message.what = OPEN_CLIENT_HANDSHAKE_SCREEN;
                break;
        }
        liveData.postValue(message);
    }

    private void onCurrentTaskFetch(DayNightCheckModel model) {
        item.set(model);

        List<BreadCumItemModel> items = new ArrayList<>();
        items.add(BreadCumItemModel.postItem(model.noOfCheckedPosts, model.availablePosts == 0));
        items.add(BreadCumItemModel.strengthItem(model.availableStrength, model.noOfCheckedStrength, model.availableStrength == 0));
        items.add(BreadCumItemModel.siteCheckListItem(model.availableSiteCheckList, model.noOfSiteCheckListDone, model.availableSiteCheckList == 0));
        items.add(BreadCumItemModel.clientHandShakeItem(model.clientHandshakeIsDone, model.taskTypeId != 1));
        breadCums.set(items);

        menuNavigationAdapter.clear();

        if (model.availablePosts != 0)
            menuNavigationAdapter.add(MenuNavigationItem.postItem());

        if (model.availableStrength != 0)
            menuNavigationAdapter.add(MenuNavigationItem.strengthItem());

        if (model.taskTypeId == 1)
            menuNavigationAdapter.add(MenuNavigationItem.clientHandShakeItem());

        if (TextUtils.isEmpty(model.actualTaskExecutionStartDateTime)) {
            String startDateTime = LocalDateTime.now().toString();
            int status = TaskEntity.TaskStatus.IN_PROGRESS.getTaskStatus();

            addDisposable(taskDao.updateTaskOnStartDayCheck(model.taskId, startDateTime, status)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(row -> {
                        Data inputData = new Data.Builder().putInt(RotaTaskWorker.class.getSimpleName(),
                                RotaTaskWorker.RotaTaskWorkerType.SYNC_TO_SERVER.getWorkerType()).build();
                        oneTimeWorkerWithInputData(RotaTaskWorker.class, inputData);
                    }, Timber::e));
        }

        if (model.checkInStatus != CheckInStatus.SKIPPED.getStatus()) {
            showCheckInBar.set(VISIBLE);
            checkedInTime.set("Checked In at " + TimeUtils.getFormatDateTime(model.checkInDateTime));
        }
    }

    public void onCompleteDayCheckClick(View view) {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
//        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);
        int companyId = Prefs.getInt(PrefConstants.COMPANY_ID, 1);
        if (item.get() != null && companyId == GroupCompany.UNIQ.getCompanyId()) {

            if (!Objects.requireNonNull(item.get()).actualTaskExecutionStartDateTime.isEmpty()) {
                LocalDateTime startedTaskTime = TimeUtils.getServerTimeFromStringDate(Objects.requireNonNull(item.get()).actualTaskExecutionStartDateTime);
                LocalDateTime currentTime = LocalDateTime.now();
                Duration duration = Duration.between(startedTaskTime, currentTime);
                long minutes = duration.toMinutes();
                if (minutes < 10) {
                    showToast("Task can not be complete before 10 minutes");
                    return;
                }
            }
        }

        setIsLoading(true);
        addDisposable(dayCheckDao.validateTaskCompleted(taskId)
                .compose(transformSingle())
                .subscribe(this::onTaskValidation, Timber::e));
    }

    private void onTaskValidation(TaskValidationModel model) {

        if (model.checkedGuardCount < model.minGuardCheckCount) {
            setIsLoading(false);
            showToast("Check minimum " + model.minGuardCheckCount + " guard(s) to complete");
            return;
        }

        if (model.posts < model.minPostCheckCount) {
            setIsLoading(false);
            showToast("Check minimum " + model.minPostCheckCount + " post(s) to complete");
            return;
        }

        if (model.totalSiteCheckList != 0 && model.totalEdited != model.totalSiteCheckList) {
            setIsLoading(false);
            showToast("Site check list is pending");
            return;
        }

        if (model.taskType == 1 && (model.clientHandShakeStatus == 1 || model.clientHandShakeStatus == 0)) {
            setIsLoading(false);
            showToast("Client hand shake is pending");
            return;
        }

        if (model.isPendingStrength != 0) {
            setIsLoading(false);
            showToast("Site strength is pending");
            return;
        }

        if (qrCodesList.get() == null) {
            addDisposable(taskDao.getAllQRCodesAtSite(Prefs.getInt(PrefConstants.CURRENT_SITE))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(qrList -> qrCodesList.set(qrList), Timber::e));
            openQRScannerForCheckout();
        } else
            openQRScannerForCheckout();
    }

    private void openQRScannerForCheckout() {
        message.what = NavigationConstants.OPEN_SCANNER_FOR_CHECKOUT;
        liveData.postValue(message);
    }

    public void updateCheckOutSkipRecord(String scannedQRCode, String skipReason) {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        addDisposable(taskDao.updateCheckOutStatus(scannedQRCode, skipReason,
                        LocalDateTime.now().toString(), taskId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> finallySaveTaskToDB(taskId), Timber::e));
    }

    private void finallySaveTaskToDB(int taskId) {
        DayNightCheckData data = new DayNightCheckData();

        addDisposable(Single.zip(dayCheckDao.fetchAllPostsOnDayCheckDone(taskId),
                dayCheckDao.fetchAllGuardsOnDayCheckDone(taskId),
                dayCheckDao.fetchAllCheckedRegisters(taskId),
                registersDao.fetchAllRegisterAttachmentsByTask(taskId),
                dayCheckDao.fetchAllSecurityCheckData(taskId),
                dayCheckDao.fetchAllPostCheckListData(taskId),
                dayCheckDao.fetchAllSiteCheckListData(taskId),
                dayCheckDao.fetchAllSiteStrengthData(taskId),
                dayCheckDao.fetchCheckInOutData(taskId),
                (postCheckData, guardCheckData, registerCheckData, registerAttachments, securityCheckData,
                 postCheckListData, siteCheckListData, siteStrengthData, checkInOutEntity) -> {
                    data.posts = postCheckData;
                    data.guards = guardCheckData;
                    data.registers = registerCheckData;
                    data.securityRisks = securityCheckData;
                    data.postCheckLists = postCheckListData;
                    data.siteCheckLists = siteCheckListData;
                    data.siteStrength = siteStrengthData;
                    data.checkInOutData = checkInOutEntity;

                    //ADDING CHECK IN OR OUT DATA TO TASK_EXECUTION_RESULT JSON
                    /*DayNightCheckData.CheckInOutData checkInOutData = new DayNightCheckData.CheckInOutData();
                    checkInOutData.checkInDateTime = Objects.requireNonNull(item.get()).checkInDateTime;
                    checkInOutData.status = Objects.requireNonNull(item.get()).checkInStatus;
                    checkInOutData.qrCode = "";
                    data.checkInOutData = checkInOutData;*/

                    for (DayNightCheckData.RegisterCheckData reg : data.registers) {
                        for (RegisterAttachmentEntity regAttach : registerAttachments) {
                            if (reg.postId == regAttach.postId && reg.registerTypeId == regAttach.registerTypeId) {
                                reg.registerAttachments.add(new DayNightCheckData.RegisterAttachmentCheckData(regAttach.postId, regAttach.siteId, regAttach.taskId, regAttach.registerTypeId, regAttach.registerAttachmentId, regAttach.registerAttachmentGuid));
                            }
                        }
                    }

                    updateTaskItem(data, taskId);
                    return new DayNightCheckData();

                }).compose(transformSingle()).subscribe((dayNightCheckData, throwable) -> {
            if (throwable != null) {
                showToast("Error in Complete task please try later");
            }
        }));
    }

    private void updateTaskItem(DayNightCheckData data, int taskId) {
        addDisposable(dayCheckDao.fetchClientHandShakeData(taskId)
                .compose(transformSingle())
                .subscribe((clientHandShakeData, throwable) -> {
                    data.clientHandShakeData = clientHandShakeData;
                    String location = Prefs.getDouble(PrefConstants.LATITUDE) + ", " + Prefs.getDouble(PrefConstants.LONGITUDE);
                    addDisposable(taskDao.updateTaskOnFinish(taskId, LocalDateTime.now().toString(), new Gson().toJson(data), location)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(rowId -> {
                                setIsLoading(false);
                                addTimeLine();
                                Data inputData = new Data.Builder().putInt(RotaTaskWorker.class.getSimpleName(),
                                        RotaTaskWorker.RotaTaskWorkerType.SYNC_TO_SERVER.getWorkerType()).build();
                                oneTimeWorkerWithInputData(RotaTaskWorker.class, inputData);
                                showToast("Task Completed");

                                Prefs.putInt(PrefConstants.ALREADY_STARTED_TASK_ID, 0);

                                message.what = NavigationConstants.ON_DAY_NIGHT_CHECK_DONE;
                                liveData.postValue(message);

                                //Adding Reward Fine Worker Call{when }
                                oneTimeWorkerWithNetwork(AddRewardFineWorker.class);
                            }, t -> {
                                showToast("Error in Complete task please try later");
//                                Timber.e(t);
                            }));
                }));
    }

    public void onSitePostQrScanned(String qrRawData) {
//        showToast(qrRawData);
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);
        int status = CheckedPostEntity.CheckedPostStatus.COMPLETED.getPostStatus();
        if (siteId != 0) {
            addDisposable(sitePostDao.fetchPendingPostViaQR(siteId, taskId, status, qrRawData)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onFetchPostDetailViaQR, e -> showToast("Scanned Post QR not available at this site")));
        }
    }

    private void onFetchPostDetailViaQR(ScanPostQRModel model) {
        if (model.getCheckedPost() != null) {
            if (model.getCheckedPost() > 0) {
                showToast("Post check at this site already completed");
            } else {
                Prefs.putInt(PrefConstants.CURRENT_POST, model.getId());
                message.what = NavigationConstants.ON_SITE_POST_CLICK;
                liveData.postValue(message);
            }
        } else {
            showToast("Scanned Post QR not available at this site");
        }
    }

    void startTimer() {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
//        addDisposable(taskDao.fetchTimeSpentByTaskId(taskId)
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
//        addDisposable(taskDao.updateTimeSpentByTaskId(taskId, tik.lastTik)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rowId -> tik.cancel(), Timber::e));
    }

    private void addTimeLine() {
        DayNightCheckModel model = item.get();
        if (model != null) {
            DailyTimeLineEntity dailyTimeline = new DailyTimeLineEntity(model.taskName, model.siteCode);
            addDisposable(dailyTimeLineDao.insert(dailyTimeline)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rowId -> Timber.d("Time Line added"), Timber::e));
        }
    }
}