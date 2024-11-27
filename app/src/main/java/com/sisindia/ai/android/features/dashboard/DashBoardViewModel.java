package com.sisindia.ai.android.features.dashboard;

import static com.sisindia.ai.android.constants.IntentConstants.LOCATION_WORKER_TAG;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_BARRACKS_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_DISBANDMENT_MENU_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_LAUNCHING_MY_SIS_APP;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_MANUAL_SYNC;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_MY_KPIS_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_PERFORMANCE_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_RECRUIT_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_SALES_REFERENCE_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_SELF_SERVICE_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.ON_UNITS_CLICK;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_PRE_DASH_BOARD;
import static com.sisindia.ai.android.constants.PrefConstants.CLEAR_PREFS;
import static java.util.concurrent.TimeUnit.MINUTES;

import android.app.Application;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.droidcommons.preference.Prefs;
import com.google.android.material.navigation.NavigationView;
import com.sisindia.ai.android.BuildConfig;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.features.onboard.OnBoardActivity;
import com.sisindia.ai.android.models.TableSyncResponse;
import com.sisindia.ai.android.room.IopsDatabase;
import com.sisindia.ai.android.room.dao.TaskDao;
import com.sisindia.ai.android.room.entities.DutyStatusEntity;
import com.sisindia.ai.android.room.entities.TaskEntity;
import com.sisindia.ai.android.utils.AnalyticsEventKey;
import com.sisindia.ai.android.utils.IopsUtil;
import com.sisindia.ai.android.utils.TimeUtils;
import com.sisindia.ai.android.workers.DutyStatusWorker;
import com.sisindia.ai.android.workers.LocationWatcherWorker;
import com.sisindia.ai.android.workers.RotaTaskWorker;

import org.threeten.bp.LocalDateTime;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class DashBoardViewModel extends IopsBaseViewModel implements NavigationView.OnNavigationItemSelectedListener {

    public ObservableBoolean isOnDuty = new ObservableBoolean(Prefs.getBoolean(PrefConstants.IS_ON_DUTY));
    public ObservableBoolean isButtonEnable = new ObservableBoolean(true);
    public ObservableField<String> waitingTime = new ObservableField<>("");
    public ObservableField<String> userName = new ObservableField<>(Prefs.getString(PrefConstants.AREA_INSPECTOR_NAME));
    public ObservableField<String> dutyOnOffDateTime = new ObservableField<>("");
    public ObservableField<String> currentAppVersion = new ObservableField<>(BuildConfig.VERSION_NAME);

    @Inject
    IopsDatabase database;

    @Inject
    public TaskDao taskDao;

    @Inject
    public DashBoardViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actionDashboardRota)
            message.what = OPEN_PRE_DASH_BOARD;
        else if (id == R.id.actionUnits)
            message.what = ON_UNITS_CLICK;
        else if (id == R.id.actionPerformance)
            message.what = ON_PERFORMANCE_CLICK;
        else if (id == R.id.actionConveyance)
            message.what = NavigationConstants.ON_CONVEYANCE_CLICK;
        else if (id == R.id.actionPlanOfActions)
            message.what = NavigationConstants.ON_PLAN_OF_ACTIONS_CLICK;
        else if (id == R.id.actionIssueManagement)
            message.what = NavigationConstants.ON_ISSUE_MANAGEMENT_CLICK;
        else if (id == R.id.actionAnnualKitReplacement)
            message.what = NavigationConstants.ON_ANNUAL_KIT_REPLACEMENT_CLICK;
        /*else if (id == R.id.actionMaskDistribution)
            message.what = ON_MASK_DISTRIBUTION_CLICK;*/
        else if (id == R.id.actionSISEvents)
            message.what = NavigationConstants.OPEN_SIS_EVENTS;
        else if (id == R.id.actionRecruit)
            message.what = ON_RECRUIT_CLICK;
        else if (id == R.id.actionSalesReference)
            message.what = ON_SALES_REFERENCE_CLICK;
        else if (id == R.id.actionDisbandment)
            message.what = ON_DISBANDMENT_MENU_CLICK;
        else if (id == R.id.actionMySiS)
            message.what = ON_LAUNCHING_MY_SIS_APP;
        else if (id == R.id.actionSelfService)
            message.what = ON_SELF_SERVICE_CLICK;
        else if (id == R.id.actionBarracks)
            message.what = ON_BARRACKS_CLICK;
        else if (id == R.id.actionManualSync)
            message.what = ON_MANUAL_SYNC;
        else if (id == R.id.actionMyKPIs)
            message.what = ON_MY_KPIS_CLICK;
        else if (id == R.id.action_settings)
            message.what = NavigationConstants.ON_SETTING_CLICK;
        else if (id == R.id.action_timeline)
            message.what = NavigationConstants.ON_TIMELINE_CLICK;
        else if (id == R.id.action_logout)
            message.what = NavigationConstants.ON_LOGOUT_CLICK;
        liveData.postValue(message);
        return false;
    }

    public void onDutyChanged(CompoundButton view, Boolean isChecked) {

        isOnDuty.set(isChecked);

        if (!isGspEnabled()) {
            SwitchCompat sw = (SwitchCompat) view;
            sw.setChecked(!isChecked);
            showToast("Please Enable Location Service");
            return;
        }

        if (isChecked) {
            message.what = NavigationConstants.OPEN_CAMERA_FOR_SELFIE;
            liveData.postValue(message);
        } else if (Prefs.getBoolean(PrefConstants.IS_ON_DUTY)) {
//            if (!BuildConfig.DEBUG) // Comment If required
            triggerDutyOnOffFunctions(false);
        }
    }

    public void triggerDutyOnOffFunctions(Boolean isSwitchChecked) {

        // attemptCount = 0;
        /* Disable the button for 30 seconds */
        isButtonEnable.set(false);
        new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int sec = (int) ((millisUntilFinished) / 1000);
                waitingTime.set(TimeUtils.dutyOnOffWaitingTime(sec));
            }

            @Override
            public void onFinish() {
                isButtonEnable.set(true);
            }
        }.start();

        Prefs.putBoolean(PrefConstants.IS_ON_DUTY, isSwitchChecked);

        if (!isSwitchChecked) {
            //Triggering below worker to sync all DutyTask if not synced
            oneTimeWorkerWithNetwork(DutyStatusWorker.class);
            triggerRotaTaskWorkerBeforeDutyOff();

            //CANCELING PERIODIC WORKER IF DUTY IS OFF
            WorkManager.getInstance(getApplication()).cancelAllWorkByTag(LOCATION_WORKER_TAG);

        } else {
            //Enable below code and startLocationService() from LocationWatcherWorker and UserLocationActivity
            PeriodicWorkRequest pwr = new PeriodicWorkRequest.Builder(LocationWatcherWorker.class, 15, MINUTES)
                    .addTag(LOCATION_WORKER_TAG).build();
            WorkManager.getInstance(getApplication())
                    .enqueueUniquePeriodicWork(LOCATION_WORKER_TAG, ExistingPeriodicWorkPolicy.KEEP, pwr);
        }

        //UPDATING DUTY ON/OFF STATUS TO LOCAL DB AND SYNCING TO SERVER
        new Handler().postDelayed(() -> {
            insertOrUpdateDutyOnOffToDB(isSwitchChecked);
            triggerDutyOnOffToServer();
        }, 500);
    }

    public void initViewModel() {
        Bundle bundle = new Bundle();
        bundle.putString(PrefConstants.AREA_INSPECTOR_NAME, Prefs.getString(PrefConstants.AREA_INSPECTOR_NAME));
        bundle.putString(PrefConstants.AREA_INSPECTOR_CODE, Prefs.getString(PrefConstants.AREA_INSPECTOR_CODE));
        bundle.putString("VERSION_NAME", BuildConfig.VERSION_NAME);
        analytics.logEvent(AnalyticsEventKey.DASHBOARD, bundle);
        /*if (LocalTime.now().getHour() > 5)
            checkDutyOffStatus();*/
    }

    /*private void checkDutyOffStatus() {
        addDisposable(taskDao.checkDutyOffStatus()
                .compose(transformSingle())
                .subscribe((statusCode, throwable) -> {
                    if (statusCode > 0) {
                        message.what = NavigationConstants.ON_FORCE_DUTY_OFF;
                        liveData.postValue(message);
                    }
                }));
    }*/

    void clearAppData() {
        database.clearAllTables();
        Prefs.clear();
        Prefs.putBoolean(CLEAR_PREFS, false);
        getApplication().startActivity(OnBoardActivity.newIntent(getApplication().getApplicationContext()));
    }

    private void triggerRotaTaskWorkerBeforeDutyOff() {
        Data inputData = new Data.Builder().putInt(RotaTaskWorker.class.getSimpleName(),
                RotaTaskWorker.RotaTaskWorkerType.SYNC_TO_SERVER.getWorkerType()).build();
//        oneTimeWorkerWithInputData(RotaTaskWorker.class, inputData);
        oneTimeKeepWorkerWithInputData(RotaTaskWorker.class, inputData);
    }

    //    private int attemptCount = 0;
    public void triggerDutyOnOffToServer() {
//        attemptCount++;
//        if (attemptCount < 4) {
        message.what = NavigationConstants.SHOW_PROGRESS_DIALOG;
        liveData.postValue(message);
        new Handler().postDelayed(this::uploadDutyStatusToServer, 1200);
//        }
    }

    private void uploadDutyStatusToServer() {
        addDisposable(dutyStatusDao.fetchNotSyncDutyStatus(false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onItemsFetched, e -> {
//                            String errorMsg = "Error! while updating your duty status (local query error)\n\nPlease try again...";
                            onErrorFromApiOrQuery(getApplication().getResources().getString(R.string.error_msg1));
                        }
                ));
    }

    private void onItemsFetched(DutyStatusEntity item) {
        addDisposable(coreApi.isDutyOnOff(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.statusCode == 200) {
                        onTableSynced(response.data, item);
                        message.what = NavigationConstants.HIDE_PROGRESS_DIALOG;
                        liveData.postValue(message);

                        //Triggering below worker to sync all DutyTask if not synced
                        oneTimeWorkerWithNetwork(DutyStatusWorker.class);

                    } else {
                        onErrorFromApiOrQuery(response.statusMessage);
                    }
                }, error -> {
                    String errorMsg = getApplication().getResources().getString(R.string.error_msg2);
                    if (IopsUtil.isInternetAvailable(getApplication()))
                        errorMsg = getApplication().getResources().getString(R.string.error_msg3);
                    onErrorFromApiOrQuery(errorMsg);
                }));
    }

    private void onErrorFromApiOrQuery(String errorMessage) {
        message.what = NavigationConstants.HIDE_PROGRESS_DIALOG_SHOW_POPUP;
        message.obj = errorMessage;
        liveData.postValue(message);
    }

    private void insertOrUpdateDutyOnOffToDB(boolean isDutyOn) {
        if (isDutyOn) {
            DutyStatusEntity item = new DutyStatusEntity(true);
            item.dutyOnLocation = Prefs.getDouble(PrefConstants.LATITUDE, 0.0) + "," +
                    Prefs.getDouble(PrefConstants.LONGITUDE, 0.0);
            addDisposable(dutyStatusDao.insertDutyOnRecord(item)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((rowId, throwable) -> {
                    }));
        } else {
            String dutyOffDateTime = LocalDateTime.now().toString();
            String offLocation = Prefs.getDouble(PrefConstants.LATITUDE, 0.0) + "," +
                    Prefs.getDouble(PrefConstants.LONGITUDE, 0.0);
            addDisposable(dutyStatusDao.updateDutyOffToLastRecord(dutyOffDateTime, offLocation, false)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((rowId, throwable) -> {
                    }));
        }
    }

    private void onTableSynced(TableSyncResponse.TableSyncData data, DutyStatusEntity item) {
        if (data != null && data.serverId != 0) {
            addDisposable(dutyStatusDao.updateOnSyncToServer(item.localId, data.serverId, true)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rowId -> Timber.d("Duty status row id %s", rowId), Timber::e));
        }
    }

    public void updateMySiSStartStatus() {
        addDisposable(taskDao.updateTaskOnStartDayCheck(Prefs.getInt(PrefConstants.CURRENT_TASK),
                        LocalDateTime.now().toString(), TaskEntity.TaskStatus.IN_PROGRESS.getTaskStatus())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(row -> {
                }, Timber::e));
    }
}