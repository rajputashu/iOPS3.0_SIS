package com.sisindia.ai.android.features.rota;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_CONVEYANCE_SCREEN;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_ADD_ROTA;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_DASH_BOARD_DRAWER;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_NUDGES_DASHBOARD;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_ROTA_BILL_SUBMISSION_TASK;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_ROTA_MONINPUT_TASK;
import static org.threeten.bp.DayOfWeek.SUNDAY;
import static org.threeten.bp.temporal.TemporalAdjusters.next;
import static org.threeten.bp.temporal.TemporalAdjusters.previous;

import android.app.Application;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.commons.CheckInStatus;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.firebase.NotificationHandler;
import com.sisindia.ai.android.models.TableSyncResponse;
import com.sisindia.ai.android.models.performance.AOConveyanceSummaryData;
import com.sisindia.ai.android.models.rota.RotaResponse;
import com.sisindia.ai.android.room.dao.DayCheckDao;
import com.sisindia.ai.android.room.dao.NotificationsDao;
import com.sisindia.ai.android.room.dao.TaskDao;
import com.sisindia.ai.android.room.entities.CheckInOutEntity;
import com.sisindia.ai.android.room.entities.RotaTasksEntity;
import com.sisindia.ai.android.room.entities.TaskEntity;
import com.sisindia.ai.android.uimodels.RotaTaskItemModel;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class RotaViewModel extends IopsBaseViewModel {

    public WeeklyRotaComplianceRecyclerAdapter viewPagerAdapter = new WeeklyRotaComplianceRecyclerAdapter();
    public DashBoardRotaTaskRecyclerAdapter dashBoardAdapter = new DashBoardRotaTaskRecyclerAdapter();
    public ObservableInt noTasksVisibility = new ObservableInt(GONE);
    public ObservableInt pendingCount = new ObservableInt(0);
    public ObservableInt obsNotificationCount = new ObservableInt(0);
    public ObservableField<LocalDate> selectedDateObs = new ObservableField<>(LocalDate.now());
    public ObservableField<String> billSubmissionCount = new ObservableField<>("");
    public ObservableField<String> monInputCount = new ObservableField<>("");
    public ObservableInt isShownBSLayout = new ObservableInt(GONE);
    public ObservableInt isShownMILayout = new ObservableInt(GONE);
    public ObservableField<String> todayConveyance = new ObservableField<>("");
    public ObservableField<String> monthsConveyance = new ObservableField<>("");
    public ObservableField<String> selectedPostCode = new ObservableField<>("");
    public ObservableField<Integer> checkInStatus = new ObservableField<>(CheckInStatus.DEFAULT.getStatus());
    public ObservableField<List<String>> qrCodesList = new ObservableField<>();
    public String selectedGeoString = "";

    @Inject
    public TaskDao taskDao;

    @Inject
    public DayCheckDao dayCheckDao;

    @Inject
    @Named("@NotificationHandler")
    NotificationHandler notificationHandler;

    @Inject
    NotificationsDao notificationDao;

    private final Handler refreshHandler = new Handler();

    boolean showConveyanceScreen = true;

    public RotaViewListeners rotaViewListeners = new RotaViewListeners() {
        @Override
        public void onDashBoardTaskItemClick(RotaTaskItemModel item) {

            if (!isGspEnabled()) {
                showMessage("Please Enable Location service");
                return;
            }

            if (Prefs.getBoolean(PrefConstants.IS_ON_DUTY)) {
                if (item.taskTypeId != 0) {

                    if (!(item.taskTypeId == 1 || item.taskTypeId == 2) && item.taskStatus == TaskEntity.TaskStatus.COMPLETED.getTaskStatus()) {
                        showMessage("Task already completed");
                        return;
                    }

                    //Checking if Task is not NightTask and User is not of UNIQ and SLV
                    if (Prefs.getInt(PrefConstants.COMPANY_ID) == 1 &&
                            item.taskTypeId != 2 && (!item.isAutoCreation && item.approvedDateTime == null)) {
                        showMessage("Current Adhoc task is not approved, please contact BH");
                        return;
                    }
                    startClickedRotaTask(item);
                }
            } else {
                showToast(R.string.please_turn_on_duty);
            }
        }

        @Override
        public void onWeeklyComplianceItemClick() {
        }
    };

    private void startClickedRotaTask(RotaTaskItemModel item) {

        Prefs.putInt(PrefConstants.CURRENT_TASK, item.taskId);
        Prefs.putInt(PrefConstants.CURRENT_SITE, item.siteId);

        if (item.taskDescription != null && item.taskDescription.equalsIgnoreCase("Dynamic")) {

//            Prefs.putInt(PrefConstants.CURRENT_TASK_TYPE_ID, item.taskTypeId);
            message.what = NavigationConstants.OPEN_DYNAMIC_TASKS;
            message.arg1 = item.taskTypeId;
        } else {
            switch (item.taskTypeId) {
                case 1:
                case 2:
                    fetchAllQRAtSite(item.siteId);
                    checkInStatus.set(item.checkInStatus);
                    selectedPostCode.set(item.dutyPostCode);
                    selectedGeoString = item.siteGeoPointString;
                    message.what = NavigationConstants.OPEN_REVIEW_INFORMATION;
                    break;
                case 3:
                    Prefs.putInt(PrefConstants.CURRENT_BARRACK_ID, item.barrackId);
                    message.what = NavigationConstants.OPEN_BARRACK_INSPECTION;
                    break;

                case 4:
                    message.what = NavigationConstants.OPEN_CLIENT_COORDINATION;
                    break;

                case 5:
                    message.what = NavigationConstants.OPEN_BILL_SUBMISSION_TASK;
                    break;

                case 6:
                    message.what = NavigationConstants.OPEN_BILL_COLLECTION_TASK;
                    break;

                case 7:
                case 8:
                case 9:
                    message.what = NavigationConstants.OPEN_OTHER_TASK;
                    break;
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                    message.what = NavigationConstants.OPEN_MYSIS_TASKS;
                    message.obj = item;
                    break;
                case 24:
                    Prefs.putInt(PrefConstants.TASK_SERVER_ID, item.serverId);
                    message.what = NavigationConstants.OPEN_PRACTO_APP_LOGIN;
                    break;
            }
        }
        liveData.postValue(message);
    }

    @Inject
    public RotaViewModel(@NonNull Application application) {
        super(application);
    }

    public void rotaDashboardClicks(View view) {
        if (view.getId() == R.id.ivRotaDrawer) {
            message.what = OPEN_DASH_BOARD_DRAWER;
            liveData.postValue(message);
        } else if (view.getId() == R.id.billSubmissionLayout) {
            message.what = OPEN_ROTA_BILL_SUBMISSION_TASK;
            liveData.postValue(message);
        } else if (view.getId() == R.id.monInputLayout) {
            message.what = OPEN_ROTA_MONINPUT_TASK;
            liveData.postValue(message);
        } else if (view.getId() == R.id.conveyanceOfMonthView) {
            if (showConveyanceScreen) {
                message.what = ON_CONVEYANCE_SCREEN;
                liveData.postValue(message);
            }
        }
    }

    public void tvRotaDateClick(View texView) {
        LocalDate selectedDate = selectedDateObs.get() == null ? LocalDate.now() : selectedDateObs.get();
        final LocalDate today = LocalDate.now();
        final LocalDate currentSunday = today.with(next(SUNDAY));
        final LocalDate pastSunday = currentSunday.with(previous(SUNDAY));
        final LocalDate nextSunday = currentSunday.with(next(SUNDAY));

        Calendar maxDate = Calendar.getInstance();
        maxDate.set(Calendar.YEAR, nextSunday.getYear());
        maxDate.set(Calendar.MONTH, nextSunday.getMonthValue() - 1);
        maxDate.set(Calendar.DAY_OF_MONTH, nextSunday.getDayOfMonth());
//        maxDate.set(Calendar.DAY_OF_MONTH, nextSunday.getDayOfMonth() - 5);

        Calendar minDate = Calendar.getInstance();
        minDate.set(Calendar.YEAR, pastSunday.getYear());
        minDate.set(Calendar.MONTH, pastSunday.getMonthValue() - 1);
//        minDate.set(Calendar.DAY_OF_MONTH, pastSunday.getDayOfMonth());
        minDate.set(Calendar.DAY_OF_MONTH, pastSunday.getDayOfMonth() - 1);

        DatePickerDialog datePickerDialog = new DatePickerDialog(texView.getContext(), (view, year, month, dayOfMonth) -> {
            selectedDateObs.set(LocalDate.of(year, month + 1, dayOfMonth));
            fetchRotaFromLocal();
        }, Objects.requireNonNull(selectedDate).getYear(), selectedDate.getMonthValue() - 1, selectedDate.getDayOfMonth());
        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        datePickerDialog.show();

        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
    }

    public void onViewsClick(View view) {
        if (view.getId() == R.id.nudgesLayout) {
            message.what = OPEN_NUDGES_DASHBOARD;
            liveData.postValue(message);
        } else if (view.getId() == R.id.tvAddRota) {
            if (Prefs.getBoolean(PrefConstants.IS_ON_DUTY)) {
                message.what = OPEN_ADD_ROTA;
                liveData.postValue(message);
            } else
                showToast(R.string.please_turn_on_duty);
        }
    }

    private void fetchByDate(LocalDate date) {
        addDisposable(dayCheckDao.fetchRotaListByDate(date.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onFetchTasks, Timber::e));
    }

    private void fetchPendingCount(LocalDate date) {
        addDisposable(taskDao.getPendingCount(date.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rows -> pendingCount.set(rows), Timber::e));
    }

    public void fetchPendingNotification() {
        addDisposable(notificationDao.fetchNudge()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    if (!list.isEmpty()) {
                        obsNotificationCount.set(list.size());
                        message.what = NavigationConstants.OPEN_DYNAMIC_NUDGE_SCREEN;
//                        message.obj = list.get(0).getNotificationId();
                        message.obj = list.get(0);
                        liveData.postValue(message);
                    } else {
                        obsNotificationCount.set(0);
                    }
                }, Timber::e));
    }

    private void onFetchTasks(List<RotaTaskItemModel> list) {
        List<RotaTaskItemModel> displayList = new ArrayList<>();
        List<RotaTaskItemModel> mySISList = new ArrayList<>();
        for (RotaTaskItemModel item : list) {
            if (item.taskTypeId == 2) {
                if (!TextUtils.isEmpty(item.siteCode))
                    displayList.add(item);
            } else if (item.taskTypeId == 14 || item.taskTypeId == 15 || item.taskTypeId == 16 || item.taskTypeId == 17 ||
                    item.taskTypeId == 18 || item.taskTypeId == 19 || item.taskTypeId == 20 || item.taskTypeId == 21 || item.taskTypeId == 22) {
                if (item.description != null && !item.description.isEmpty())
                    mySISList.add(item);
            } else
                displayList.add(item);
        }

        if (!mySISList.isEmpty()) {
            RotaTaskItemModel mySiSHeader = new RotaTaskItemModel();
            mySiSHeader.taskTypeId = 51; // Id for header
            mySiSHeader.description = "" + mySISList.size(); // Count of total Number of task
            displayList.add(mySiSHeader);
            displayList.addAll(mySISList);
        }

        noTasksVisibility.set(displayList.isEmpty() ? VISIBLE : GONE);
        dashBoardAdapter.clearAndSetItems(displayList);
    }

    private void fetchWeeklyCompliance() {
        viewPagerAdapter.clear();

        addDisposable(taskDao.fetchTodayRotaCompliance()
                .compose(transformSingle())
                .subscribe((todayRota, throwable) -> {
                    if (todayRota != null)
                        viewPagerAdapter.add(todayRota);
                }));

        new Handler().postDelayed(() -> {

            addDisposable(taskDao.fetchYesterdayRotaCompliance()
                    .compose(transformSingle())
                    .subscribe((yesterdayRota, throwable) -> {
                        if (yesterdayRota != null)
                            viewPagerAdapter.add(yesterdayRota);
                    }));

            addDisposable(taskDao.fetchThisWeekRotaCompliance()
                    .compose(transformSingle())
                    .subscribe((thisWeekRota, throwable) -> {
                        if (thisWeekRota != null)
                            viewPagerAdapter.add(thisWeekRota);
                    }));

            addDisposable(taskDao.fetchLastWeekRotaCompliance()
                    .compose(transformSingle())
                    .subscribe((lastWeekRota, throwable) -> {
                        if (lastWeekRota != null)
                            viewPagerAdapter.add(lastWeekRota);
                    }));

        }, 300);
    }

    private void fetchAllQRAtSite(int siteId) {
        addDisposable(taskDao.getAllQRCodesAtSite(siteId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(qrList -> qrCodesList.set(qrList), Timber::e));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        refreshHandler.removeCallbacksAndMessages(null);
    }

    public void fetchRotaFromLocal() {
        LocalDate date = selectedDateObs.get() == null ? LocalDate.now() : selectedDateObs.get();
        if (date != null) {
            fetchByDate(date);
            fetchPendingCount(date);
            fetchWeeklyCompliance();
            fetchPendingNotification();
//            checkDutyOffStatus();

            int currentDayNo = LocalDate.now().getDayOfMonth();
            if (currentDayNo > 0 && currentDayNo < 11) {
                isShownBSLayout.set(VISIBLE);
                getBillSubmissionCounts();
                if (currentDayNo < 6) {
                    isShownMILayout.set(VISIBLE);
                    getMonInputCounts();
                }
            }
        }
    }

    private void getBillSubmissionCounts() {
        addDisposable(taskDao.fetchBillSubmissionCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> billSubmissionCount.set(getApplication().getResources().getString(R.string.dynamic_bill_submission_count, model.getPendingCount(),
                        "" + (Integer.parseInt(model.getPendingCount()) + Integer.parseInt(model.getCompletedCount())))), Timber::e));
    }

    private void getMonInputCounts() {
        addDisposable(taskDao.fetchMonInputCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> monInputCount.set(getApplication().getResources().getString(R.string.dynamic_mon_input_count, model.getPendingCount(),
                        "" + (Integer.parseInt(model.getPendingCount()) + Integer.parseInt(model.getCompletedCount())))), Timber::e));
    }

    public void fetchRotaFromServer() {
        setIsLoading(true);
        addDisposable(Single.zip(coreApi.getServerRotas(), taskDao.fetchAll(), (rotaResponse, localTasks) -> {
                    //INSERTING ALL ROTA TASKS
                    if (rotaResponse.rota != null && rotaResponse.rota.rotaTasks != null) {
                        addDisposable(taskDao.deleteRotaTasks()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(i -> insertAllRotaTasksToDB(rotaResponse.rota.rotaTasks), Timber::e));
                    }

                    if (rotaResponse.rota != null && rotaResponse.rota.siteTasks != null) {
                        boolean isNcRotaAvailable = false;
                        for (RotaResponse.SiteTaskResponse serverTask : rotaResponse.rota.siteTasks) {
                            int taskPosition = isRotaAlreadyExist(serverTask.serverId, localTasks);
                            if (taskPosition != -1) {
                                TaskEntity localTask = localTasks.get(taskPosition);
                                if (localTask.isSynced && localTask.taskStatus > serverTask.taskStatus) {
                                    addDisposable(coreApi.addOrUpdateCreatedTask(localTask)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(task -> updateTaskTable(task.data, localTask), this::onApiError));
                                } else if (localTask.isSynced && localTask.taskStatus < serverTask.taskStatus)
                                    updateTaskStatus(serverTask.taskStatus, localTask.id);
                                else if (!localTask.isAutoCreation && localTask.approvedDateTime == null && serverTask.approvedDateTime != null)
                                    updateAdHocTaskApproved(serverTask.id, serverTask.approvedDateTime, serverTask.approvedBy, serverTask.taskStatus);
                                else
                                    Timber.e("Condition : Everything is perfect, Don't do anything for god sake :)");

                            } else {
                                TaskEntity newRotaTask = new TaskEntity(serverTask.siteId, serverTask.taskTypeId, serverTask.taskStatus, serverTask.estimatedTaskExecutionStartDateTime,
                                        serverTask.actualTaskExecutionStartDateTime, serverTask.estimatedTaskExecutionEndDateTime, serverTask.actualTaskExecutionEndDateTime, serverTask.isAutoCreation,
                                        serverTask.reasonLookUpIdentifier, serverTask.serverId, serverTask.barrackId, serverTask.taskExecutionResult, serverTask.otherTaskTypeLookUpIdentifier, serverTask.description);
                                insertTaskItem(newRotaTask);
                                isNcRotaAvailable = serverTask.taskTypeId == 2;
                            }
                        }
                        if (isNcRotaAvailable)
                            notificationHandler.triggerRefreshMySitesData();

                        //deleting inactive rotas from ids
                        if (rotaResponse.rota.inActiveRota != null && !rotaResponse.rota.inActiveRota.isEmpty()) {
                            addDisposable(taskDao.deleteInActiveRotas(rotaResponse.rota.inActiveRota)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(i -> Timber.e("Inactive rota deleted on refresh"), Timber::e));
                        }
                    }
                    return true;
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe((isDone, throwable) -> {
                    setIsLoading(false);
                    if (throwable != null)
                        onApiError(throwable);

                    dashBoardAdapter.clear();
                    viewPagerAdapter.clear();
                    fetchRotaFromLocal();
                }));
    }

    private void insertAllRotaTasksToDB(List<RotaTasksEntity> rotaTasksEntities) {
        addDisposable(taskDao.insertAllRotaTasks(rotaTasksEntities)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rowId -> Timber.d("rotaTasksEntities Inserted"), Timber::e));
    }

    private int isRotaAlreadyExist(int serverRotaId, List<TaskEntity> localTableRotasList) {
        for (int i = 0; i < localTableRotasList.size(); i++) {
            if (localTableRotasList.get(i).serverId == serverRotaId) {
                return i;
            }
        }
        return -1;
    }

    private void updateTaskTable(TableSyncResponse.TableSyncData data, TaskEntity item) {
        if (data != null && data.serverId != 0)
            addDisposable(taskDao.updateTaskOnServerSync(data.serverId, item.localId, true)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rowId -> Timber.d("updated in Rota task"), Timber::e));
    }

    private void updateTaskStatus(int updatedTaskStatus, int localTaskId) {
        addDisposable(taskDao.updateTaskStatus(updatedTaskStatus, localTaskId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rowId -> Timber.d("LOCAL TASK is updated in Rota task table"), Timber::e));
    }

    private void updateAdHocTaskApproved(int localTaskId, String approvedDateTime, int approveBy, int taskStatus) {
        addDisposable(taskDao.updateAdHocTask(localTaskId, approvedDateTime, approveBy, taskStatus)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rowId -> Timber.d("AdHoc task is updated in rota task table"), Timber::e));
    }

    private void insertTaskItem(TaskEntity item) {
        addDisposable(taskDao.insert(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rowId -> {
                }, Timber::e));
    }

    public void fetchAOConveyanceSummary() {
        todayConveyance.set(Prefs.getString(PrefConstants.TODAY_CONVEYANCE, "0 KM\n Today's Conveyance"));
        String monthName = (String) DateFormat.format("MMM", new Date());
        String monthConveyance = "0 KM\n" + monthName + " Conveyance";
        monthsConveyance.set(Prefs.getString(PrefConstants.MONTH_CONVEYANCE, monthConveyance));

        addDisposable(coreApi.getAOConveyanceSummary(Prefs.getInt(PrefConstants.AREA_INSPECTOR_ID), LocalDate.now().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(conveyanceSummary -> {
                    if (conveyanceSummary != null)
                        initConveyanceIncentiveToggle(conveyanceSummary.getData());
                }, e -> {
                }));
    }

    private void initConveyanceIncentiveToggle(AOConveyanceSummaryData data) {
        String monthName = (String) DateFormat.format("MMM", new Date());
        Prefs.putString(PrefConstants.TODAY_CONVEYANCE, "" + data.getTodayConveyance() + " KM\n Today's Conveyance");
        Prefs.putString(PrefConstants.MONTH_CONVEYANCE, "" + data.getThisMonthConveyance() + " KM\n" + monthName + " Conveyance");
        todayConveyance.set(Prefs.getString(PrefConstants.TODAY_CONVEYANCE));
        monthsConveyance.set(Prefs.getString(PrefConstants.MONTH_CONVEYANCE));
    }

    public void updateCheckInOutStatus(int status) {
        addDisposable(taskDao.updateCheckInStatus(status, LocalDateTime.now().toString(),
                        Prefs.getInt(PrefConstants.CURRENT_TASK))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> Timber.i("CheckIn Status Updated..."), Timber::e));
    }

    public void insertCheckInSkipRecord(String scannedQRCode, String skipReason) {
        CheckInOutEntity entity = new CheckInOutEntity();
        entity.checkInStatus = scannedQRCode.isEmpty() ? CheckInStatus.SKIPPED.getStatus() : CheckInStatus.CHECKED_IN.getStatus();
        entity.taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        entity.checkInDateTime = LocalDateTime.now().toString();
        entity.checkInQrCode = scannedQRCode;
        entity.checkInSkipReason = skipReason;
        addDisposable(taskDao.insertCheckInOutData(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> Timber.i("CheckInout Record Inserted"), Timber::e));
    }

    public void showMessage(String message) {
        showToast(message);
    }

    private TaskTimer tik = new TaskTimer(0);

    void startCheckInTimer() {
        addDisposable(taskDao.fetchTimeSpentViaTaskId(Prefs.getInt(PrefConstants.CURRENT_TASK))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    tik = new TaskTimer(model.getTimeElapsed() - findTimeDifference(model.getLastElapsedTime()));
                    tik.start();
                }, Timber::e));
    }

    void stopCheckInTimer() {
        addDisposable(taskDao.updateTimeSpentByTaskId(Prefs.getInt(PrefConstants.CURRENT_TASK),
                        tik.lastTik, getCurrentMilliOfTheDay())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rowId -> tik.cancel(), Timber::e));
    }

    public boolean validateLocationDistance() {

        double startLat = Prefs.getDouble(PrefConstants.LATITUDE);
        double startLong = Prefs.getDouble(PrefConstants.LONGITUDE);
        double endLat = Prefs.getDouble(PrefConstants.LATITUDE);
        double endLong = Prefs.getDouble(PrefConstants.LONGITUDE);

        try {
            if (!selectedGeoString.isEmpty()) {
                String[] arrayLatLong;
                if (selectedGeoString.contains(","))
                    arrayLatLong = selectedGeoString.split(",");
                else
                    arrayLatLong = selectedGeoString.split(" ");
                endLat = Double.parseDouble(arrayLatLong[0].trim());
                endLong = Double.parseDouble(arrayLatLong[1].trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        float[] results = new float[1];
        Location.distanceBetween(startLat, startLong, endLat, endLong, results);
//        showToast("Distance : ${results[0]} meter")
//        Timber.e("Google Distance in Meter " + results[0]);
        return results[0] < 150;
    }
}