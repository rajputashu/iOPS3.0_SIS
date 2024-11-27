package com.sisindia.ai.android.features.practo

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.work.Data
import com.droidcommons.preference.Prefs
import com.google.gson.Gson
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.features.taskcheck.postcheck.PostCheckViewListeners
import com.sisindia.ai.android.models.practo.PractoCompleteTaskMO
import com.sisindia.ai.android.room.dao.DailyTimeLineDao
import com.sisindia.ai.android.room.dao.DayCheckDao
import com.sisindia.ai.android.room.dao.TaskDao
import com.sisindia.ai.android.room.entities.CheckedPostCheckListEntity
import com.sisindia.ai.android.room.entities.DailyTimeLineEntity
import com.sisindia.ai.android.room.entities.TaskEntity
import com.sisindia.ai.android.uimodels.CheckedGuardItemModel
import com.sisindia.ai.android.uimodels.SitePostModel
import com.sisindia.ai.android.uimodels.tasks.TimeElapsedMO
import com.sisindia.ai.android.workers.AttachmentsUploadWorker
import com.sisindia.ai.android.workers.RotaTaskWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 10-07-2023
 */
class PractoTaskViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var dayCheckDao: DayCheckDao

    @Inject
    lateinit var taskDao: TaskDao

    @Inject
    lateinit var dailyTimeLineDao: DailyTimeLineDao

    private var tik = TaskTimer(0)

    val obsSiteName = ObservableField("")
    val obsNoOfGuards = ObservableInt(0)
    val adapter = GuardCheckedAdapter()
    private lateinit var taskEntity: TaskEntity

    val listener = object : PostCheckViewListeners {

        override fun onSitePostItemClick(item: SitePostModel?) {

        }

        override fun onCheckedGuardClick(item: CheckedGuardItemModel?) {
            Prefs.putInt(PrefConstants.SELECTED_EMPLOYEE_ID, item!!.employeeId)
            Prefs.putString(PrefConstants.SELECTED_EMPLOYEE_NO, item.employeeNo)
            message.what = NavigationConstants.OPEN_DIRECT_PRACTO_BOTTOM_SHEET
            liveData.value = message
        }

        override fun onPostCheckListItemClick(item: CheckedPostCheckListEntity?) {

        }

        override fun onCheckedRegisterClick() {

        }
    }

    fun initTaskUI() {
        val taskId = Prefs.getInt(PrefConstants.CURRENT_TASK)
//        val siteId = Prefs.getInt(PrefConstants.CURRENT_SITE)
        addDisposable(dayCheckDao.fetchAllCheckedGuardByTaskId(taskId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (!it.isNullOrEmpty()) {
                    adapter.clearAndSetItems(it)
                    obsNoOfGuards.set(it.size)
                }
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
    }

    fun getTaskSiteName() {
        val taskID = Prefs.getInt(PrefConstants.CURRENT_TASK)
        if (taskID != 0) {
            addDisposable(taskDao.getItemByPrimaryKeyId(taskID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ taskEntity: TaskEntity ->
                    this.taskEntity = taskEntity
                    obsSiteName.set(taskEntity.siteName)

                    if (taskEntity.actualTaskExecutionStartDateTime.isNullOrEmpty())
                        updateTaskExecutionStartDetails()

                }, { t: Throwable? -> }))
        }
    }

    private fun updateTaskExecutionStartDetails() {
        addDisposable(taskDao.updateTaskOnStartDayCheck(Prefs.getInt(PrefConstants.CURRENT_TASK),
            LocalDateTime.now().toString(), TaskEntity.TaskStatus.IN_PROGRESS.taskStatus)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ triggerRotaTaskWorker() }, { }))
    }

    private fun triggerRotaTaskWorker() {
        val inputData = Data.Builder().putInt(RotaTaskWorker::class.java.simpleName,
            RotaTaskWorker.RotaTaskWorkerType.SYNC_TO_SERVER.workerType).build()
        oneTimeWorkerWithInputData(RotaTaskWorker::class.java, inputData)
    }

    fun onViewClicks(view: View) {

        when (view.id) {
            R.id.openScannerGuardCheck -> {
                message.what = NavigationConstants.ON_SCAN_GUARD_QR_CLICK
                liveData.value = message
            }

            R.id.completeTaskButton -> {
                if (obsNoOfGuards.get() == 0) {
                    showToast("Please check at least one guard to complete task")
                    return
                } else {
                    addDisposable(taskDao.getAllPractoCheckedGuards(Prefs.getInt(PrefConstants.CURRENT_TASK))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            if (!it.isNullOrEmpty()) {
                                val taskExecutionResult = Gson().toJson(PractoCompleteTaskMO(it))
                                updateTaskTable(taskExecutionResult)
                            }
                        }, { throwable: Throwable? ->
                            throwable!!.printStackTrace()
                        }))
                }
            }
        }
    }

    private fun updateTaskTable(executionResult: String) {
        taskEntity.apply {
//            actualTaskExecutionStartDateTime = actualStartDateTime
            actualTaskExecutionEndDateTime = LocalDateTime.now().toString()
            taskExecutionResult = executionResult
            isSynced = false
            taskStatus = TaskEntity.TaskStatus.COMPLETED.taskStatus
            taskGeoLocation = Prefs.getDouble(PrefConstants.LATITUDE).toString() + ", " +
                    Prefs.getDouble(PrefConstants.LONGITUDE).toString()
        }
        addDisposable(taskDao.update(taskEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.set(View.GONE)
                //UPDATING TASK TO SERVER
                addTimeLine()
                triggerRotaTaskWorker()
                message.what = NavigationConstants.OPEN_DASH_BOARD
                liveData.postValue(message)

                oneTimeWorkerWithNetwork(AttachmentsUploadWorker::class.java)

                showToast("Task completed successfully")

            }, { t: Throwable? ->
                isLoading.set(View.GONE)
                Timber.e(t)
            }))
    }

    private fun addTimeLine() {
        val dailyTimeline = DailyTimeLineEntity("PractoAppCheck", "")
        addDisposable(dailyTimeLineDao.insert(dailyTimeline)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("PractoAppCheck Time Line added")
            }, { t: Throwable? ->
                Timber.e(t)
            }))
    }

    fun startTimer() {
        val taskId: Int = Prefs.getInt(PrefConstants.CURRENT_TASK)
        addDisposable(taskDao.fetchTimeSpentViaTaskId(taskId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ model: TimeElapsedMO ->
                tik = TaskTimer(model.timeElapsed - findTimeDifference(model.lastElapsedTime))
                tik.start()
            }, { t: Throwable? -> Timber.e(t) }))
    }

    fun stopTimer() {
        val taskId: Int = Prefs.getInt(PrefConstants.CURRENT_TASK)
        addDisposable(taskDao.updateTimeSpentByTaskId(taskId, tik.lastTik, currentMilliOfTheDay)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ tik.cancel() }, { t: Throwable? -> Timber.e(t) }))
    }
}