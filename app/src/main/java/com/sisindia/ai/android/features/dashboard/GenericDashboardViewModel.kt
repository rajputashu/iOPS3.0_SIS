package com.sisindia.ai.android.features.dashboard

import android.app.Application
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.MenuItem
import android.widget.CompoundButton
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.droidcommons.preference.Prefs
import com.google.android.material.navigation.NavigationView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.sisindia.ai.android.BuildConfig
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.features.onboard.OnBoardActivity
import com.sisindia.ai.android.models.TableSyncResponse
import com.sisindia.ai.android.models.TableSyncResponse.TableSyncData
import com.sisindia.ai.android.room.IopsDatabase
import com.sisindia.ai.android.room.entities.DutyStatusEntity
import com.sisindia.ai.android.utils.AnalyticsEventKey
import com.sisindia.ai.android.utils.IopsUtil
import com.sisindia.ai.android.utils.TimeUtils
import com.sisindia.ai.android.workers.DutyStatusWorker
import com.sisindia.ai.android.workers.LocationWatcherWorker
import com.sisindia.ai.android.workers.RotaTaskWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Ashu_Rajput on 6/10/2021.
 */
class GenericDashboardViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app),
    NavigationView.OnNavigationItemSelectedListener {

    val isOnDuty = ObservableBoolean(Prefs.getBoolean(PrefConstants.IS_ON_DUTY))
    val isButtonEnable = ObservableBoolean(true)
    val waitingTime = ObservableField("")
    val userName = ObservableField(Prefs.getString(PrefConstants.AREA_INSPECTOR_NAME))
    val dutyOnOffDateTime = ObservableField("")
    val currentAppVersion = ObservableField(BuildConfig.VERSION_NAME)

    @Inject
    lateinit var database: IopsDatabase

    fun initGenericDashboard() {
        FirebaseCrashlytics.getInstance()
            .setUserId(Prefs.getString(PrefConstants.AREA_INSPECTOR_CODE, ""))
        val bundle = Bundle()
        bundle.putString(PrefConstants.AREA_INSPECTOR_NAME,
            Prefs.getString(PrefConstants.AREA_INSPECTOR_NAME))
        bundle.putString(PrefConstants.AREA_INSPECTOR_CODE,
            Prefs.getString(PrefConstants.AREA_INSPECTOR_CODE))
        bundle.putString("VERSION_NAME", BuildConfig.VERSION_NAME)
        analytics.logEvent(AnalyticsEventKey.DASHBOARD, bundle)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.actionDashboardRota -> message.what = NavigationConstants.OPEN_PRE_DASH_BOARD

            R.id.actionUnits -> message.what = NavigationConstants.ON_UNITS_CLICK

            R.id.actionPerformance -> message.what = NavigationConstants.ON_PERFORMANCE_CLICK

            R.id.action_timeline -> message.what = NavigationConstants.ON_TIMELINE_CLICK

            R.id.actionConveyance -> message.what = NavigationConstants.ON_CONVEYANCE_CLICK

            R.id.actionIssueManagement ->
                message.what = NavigationConstants.ON_ISSUE_MANAGEMENT_CLICK

            R.id.actionRecruit -> message.what = NavigationConstants.ON_RECRUIT_CLICK

            R.id.actionSalesReference ->
                message.what = NavigationConstants.ON_SALES_REFERENCE_CLICK

            R.id.actionDisbandment -> message.what = NavigationConstants.ON_DISBANDMENT_MENU_CLICK

            R.id.actionPlanOfActions -> message.what = NavigationConstants.ON_PLAN_OF_ACTIONS_CLICK

            R.id.actionSelfService -> message.what = NavigationConstants.ON_SELF_SERVICE_CLICK

            R.id.actionMyKPIs -> message.what = NavigationConstants.ON_MY_KPIS_CLICK

            R.id.action_settings -> message.what = NavigationConstants.ON_SETTING_CLICK

            R.id.actionManualSync -> message.what = NavigationConstants.ON_MANUAL_SYNC

            R.id.action_logout -> message.what = NavigationConstants.ON_LOGOUT_CLICK
        }

        liveData.postValue(message)
        return false
    }

    fun onDutyChanged(view: CompoundButton?, isChecked: Boolean?) {
        isOnDuty.set(isChecked!!)

        if (!isGspEnabled) {
            val sw = view as SwitchCompat
            sw.isChecked = !isChecked
            showToast("Please Enable Location Service")
            return
        }

        if (isChecked) {
            message.what = NavigationConstants.OPEN_CAMERA_FOR_SELFIE
            liveData.postValue(message)
        } else if (Prefs.getBoolean(PrefConstants.IS_ON_DUTY)) {
            triggerDutyOnOffFunctions(false)
        }
    }

    fun triggerDutyOnOffFunctions(isSwitchChecked: Boolean) {
        isButtonEnable.set(false)
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val sec = (millisUntilFinished / 1000).toInt()
                waitingTime.set(TimeUtils.dutyOnOffWaitingTime(sec))
            }

            override fun onFinish() {
                isButtonEnable.set(true)
            }
        }.start()

        Prefs.putBoolean(PrefConstants.IS_ON_DUTY, isSwitchChecked)

        if (!isSwitchChecked) {
            oneTimeWorkerWithNetwork(DutyStatusWorker::class.java)
            triggerRotaTaskWorkerBeforeDutyOff()
            //CANCELING PERIODIC WORKER IF DUTY IS OFF
            WorkManager.getInstance(app).cancelAllWorkByTag(IntentConstants.LOCATION_WORKER_TAG)

        } else {
            val pwr = PeriodicWorkRequest.Builder(LocationWatcherWorker::class.java, 15,
                TimeUnit.MINUTES)
                .addTag(IntentConstants.LOCATION_WORKER_TAG)
                .build()
            WorkManager.getInstance(app)
                .enqueueUniquePeriodicWork(IntentConstants.LOCATION_WORKER_TAG,
                    ExistingPeriodicWorkPolicy.KEEP, pwr)
        }

        //UPDATING DUTY ON/OFF STATUS TO LOCAL DB AND SYNCING TO SERVER
        Handler().postDelayed({
            insertOrUpdateDutyOnOffToDB(isSwitchChecked)
            triggerDutyOnOffToServer()
        }, 500)
    }

    private fun triggerRotaTaskWorkerBeforeDutyOff() {
        val inputData = Data.Builder().putInt(RotaTaskWorker::class.java.simpleName,
            RotaTaskWorker.RotaTaskWorkerType.SYNC_TO_SERVER.workerType).build()
        oneTimeKeepWorkerWithInputData(RotaTaskWorker::class.java, inputData)
    }

    private fun insertOrUpdateDutyOnOffToDB(isDutyOn: Boolean) {
        if (isDutyOn) {
            val item = DutyStatusEntity(true)
            item.dutyOnLocation = Prefs.getDouble(PrefConstants.LATITUDE, 0.0).toString() + "," +
                    Prefs.getDouble(PrefConstants.LONGITUDE, 0.0)
            addDisposable(dutyStatusDao.insertDutyOnRecord(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _: Long?, _: Throwable? -> })
        } else {
            val dutyOffDateTime = LocalDateTime.now().toString()
            val offLocation = Prefs.getDouble(PrefConstants.LATITUDE, 0.0).toString() + "," +
                    Prefs.getDouble(PrefConstants.LONGITUDE, 0.0)
            addDisposable(dutyStatusDao.updateDutyOffToLastRecord(dutyOffDateTime,
                offLocation, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _: Int?, _: Throwable? -> })
        }
    }

    private fun triggerDutyOnOffToServer() {
        message.what = NavigationConstants.SHOW_PROGRESS_DIALOG
        liveData.postValue(message)
        Handler().postDelayed({ this.uploadDutyStatusToServer() }, 1200)
    }

    private fun uploadDutyStatusToServer() {
        addDisposable(dutyStatusDao.fetchNotSyncDutyStatus(false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ item: DutyStatusEntity ->
                this.onItemsFetched(item)
            }) {
                onErrorFromApiOrQuery(app.resources.getString(R.string.error_msg1))
            })
    }

    private fun onItemsFetched(item: DutyStatusEntity) {
        addDisposable(coreApi.isDutyOnOff(item)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response: TableSyncResponse ->
                if (response.statusCode == 200) {
                    onTableSynced(response.data, item)
                    message.what = NavigationConstants.HIDE_PROGRESS_DIALOG
                    liveData.postValue(message)

                    //Triggering below worker to sync all DutyTask if not synced
                    oneTimeWorkerWithNetwork(DutyStatusWorker::class.java)
                } else {
                    onErrorFromApiOrQuery(response.statusMessage)
                }
            }) {
                var errorMsg = app.resources.getString(R.string.error_msg2)
                if (IopsUtil.isInternetAvailable(getApplication()))
                    errorMsg = app.resources.getString(R.string.error_msg3)
                onErrorFromApiOrQuery(errorMsg)
            })
    }

    private fun onTableSynced(data: TableSyncData?, item: DutyStatusEntity) {
        if (data != null && data.serverId != 0) {
            addDisposable(dutyStatusDao.updateOnSyncToServer(item.localId, data.serverId, true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ rowId: Int? ->
                    Timber.d("Duty status row id %s", rowId)
                }) { t: Throwable? ->
                    Timber.e(t)
                })
        }
    }

    private fun onErrorFromApiOrQuery(errorMessage: String) {
        message.what = NavigationConstants.HIDE_PROGRESS_DIALOG_SHOW_POPUP
        message.obj = errorMessage
        liveData.postValue(message)
    }

    fun clearAppData() {
        database.clearAllTables()
        Prefs.clear()
        Prefs.putBoolean(PrefConstants.CLEAR_PREFS, false)
        app.startActivity(OnBoardActivity.newIntent(app))
    }
}