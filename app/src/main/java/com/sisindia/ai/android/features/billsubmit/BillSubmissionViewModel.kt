package com.sisindia.ai.android.features.billsubmit

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.work.Data
import com.droidcommons.base.timer.CountUpTimer
import com.droidcommons.preference.Prefs
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.features.billsubmit.adapter.BillSubmissionCardsAdapter
import com.sisindia.ai.android.features.billsubmit.entities.BillSubmitDetailsMO
import com.sisindia.ai.android.features.billsubmit.entities.BillSubmitTaskResultMO
import com.sisindia.ai.android.models.LookUpType
import com.sisindia.ai.android.models.rota.RotaResponse
import com.sisindia.ai.android.room.dao.AttachmentDao
import com.sisindia.ai.android.room.dao.DailyTimeLineDao
import com.sisindia.ai.android.room.dao.LookUpDao
import com.sisindia.ai.android.room.dao.TaskDao
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.room.entities.DailyTimeLineEntity
import com.sisindia.ai.android.room.entities.TaskEntity
import com.sisindia.ai.android.uimodels.billsubmit.BillCheckListMO
import com.sisindia.ai.android.uimodels.billsubmit.BillSubmissionCardDetailsMO
import com.sisindia.ai.android.uimodels.billsubmit.BillSubmissionCountMO
import com.sisindia.ai.android.utils.TimeUtils
import com.sisindia.ai.android.workers.AttachmentsUploadWorker
import com.sisindia.ai.android.workers.RotaTaskWorker
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 3/12/2020.
 */
class BillSubmissionViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var timer: CountUpTimer

    @Inject
    lateinit var taskDao: TaskDao

    @Inject
    lateinit var attachmentDao: AttachmentDao

    @Inject
    lateinit var lookUpDao: LookUpDao

    @Inject
    lateinit var dailyTimeLineDao: DailyTimeLineDao

    private lateinit var taskEntity: TaskEntity
    val unitName = ObservableField("")
    val pendingCheckListCount = ObservableField("")
    val billHandedOverTo = ObservableField("")
    val billGivenToName = ObservableField("")
    val billGivenToDesignation = ObservableField("")
    val updateList = ObservableField<List<BillCheckListMO>>(arrayListOf())
    private val actualStartDateTime = LocalDateTime.now().toString()
    var billImageUri = ObservableField<Uri>()
    var billMetadata = ObservableField("")
    val obsIsOnlineOpted = ObservableBoolean(true)
    val obsBillHandedToEmpLabel = ObservableField("SIS")

    //---------------Obs used in Bill Submission Details in Card----------------//
    val obsBillPendingCount = ObservableField("0")
    val obsBillCompletedCount = ObservableField("0")
    val obsBillOverdueCount = ObservableField("0")

    val billSubmissionListener = object : BillSubmissionListener {
        override fun onBillSelected(dueBillSubmissionMO: Any) {
            val model = dueBillSubmissionMO as BillSubmissionCardDetailsMO
            Prefs.putInt(PrefConstants.CURRENT_TASK, model.taskId)
            Prefs.putInt(PrefConstants.CURRENT_SITE, model.siteId)
            message.what = NavigationConstants.OPEN_BILL_SUBMISSION_TASK_FROM_CARD
            message.obj = dueBillSubmissionMO
            liveData.postValue(message)
        }
    }
    val billSubmissionCardsAdapter = BillSubmissionCardsAdapter()
    //-----------------END-----------------------------------------------------//

    fun initBSViews() {

        when (Prefs.getInt(PrefConstants.COMPANY_ID, 1)) {
            1 -> obsBillHandedToEmpLabel.set("SIS")
            2 -> obsBillHandedToEmpLabel.set("UNIQ")
            4 -> obsBillHandedToEmpLabel.set("SLV")
        }

        getCurrentSelectedTaskDetails()
        getChecklistFromDB()
    }

    private fun updateTaskExecutionStartDetails() {
        addDisposable(taskDao.updateTaskOnStartDayCheck(Prefs.getInt(PrefConstants.CURRENT_TASK),
            LocalDateTime.now().toString(), TaskEntity.TaskStatus.IN_PROGRESS.taskStatus)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ triggerRotaTaskWorker() }, { }))
    }

    fun getBillCardsDetails() {
        addDisposable(Single.zip(taskDao.fetchBillSubmissionCount(),
            taskDao.fetchPendingBillDetails(), taskDao.fetchCompletedBillDetails(),
            Function3<BillSubmissionCountMO, List<BillSubmissionCardDetailsMO>, List<BillSubmissionCardDetailsMO>, ArrayList<Any>> { billCounts, pendingBillsList, completedBillsList ->
                return@Function3 onBillSubmissionDetailsFetch(billCounts,
                    pendingBillsList, completedBillsList)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                billSubmissionCardsAdapter.updateBillSubmissionData(it)
            }, {
                it.printStackTrace()
            }))
    }

    private fun onBillSubmissionDetailsFetch(countsDetails: BillSubmissionCountMO,
        pendingList: List<BillSubmissionCardDetailsMO>,
        completedList: List<BillSubmissionCardDetailsMO>): ArrayList<Any> {

        val finalList = arrayListOf<Any>()
        finalList.clear()

        countsDetails.apply {
            obsBillPendingCount.set(pendingCount)
            obsBillCompletedCount.set(completedCount)
            obsBillOverdueCount.set(overdueCount)
        }
        finalList.add("PENDING")
        if (pendingList.isNotEmpty())
            finalList.addAll(pendingList)
        finalList.add("COMPLETED")
        if (completedList.isNotEmpty())
            finalList.addAll(completedList)
        return finalList
    }

    private fun getChecklistFromDB() {
        setIsLoading(true)
        addDisposable(lookUpDao.fetchBillCheckList(LookUpType.CHECKLIST.typeId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list: List<BillCheckListMO>? ->
                if (list!!.isNotEmpty()) {
                    updateList.set(list)
                    updatePendingCheckListCount(list.size)
                }
            }, { throwable: Throwable? ->
                this.onFetchError(throwable!!)
            }))
    }

    private fun getCurrentSelectedTaskDetails() {
        val taskID = Prefs.getInt(PrefConstants.CURRENT_TASK)
        if (taskID != 0) {
            addDisposable(taskDao.getItemByPrimaryKeyId(taskID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ taskEntity: TaskEntity ->
                    this.taskEntity = taskEntity
//                    unitName.set(this.taskEntity.siteName)

                    if(taskEntity.actualTaskExecutionStartDateTime.isNullOrEmpty())
                        updateTaskExecutionStartDetails()

                }, { t: Throwable? ->
                    onFetchError(t!!)
                }))
        }
    }

    private fun onFetchError(throwable: Throwable) {
        setIsLoading(false)
        Timber.e(throwable)
        showToast("Error while fetching details...")
    }

    fun updatePendingCheckListCount(itemCount: Int) {
        pendingCheckListCount.set(String.format(app.resources.getString(R.string.dynamic_pending_item_count),
            itemCount))
    }

    val onRadioCheckedListener = object : RadioCheckedListener {
        override fun onRadioButtonChecked(value: String) {
            billHandedOverTo.set(value)
            if (value.contains("Accounts", ignoreCase = true) || value.contains("NAG",
                    ignoreCase = true))
                obsIsOnlineOpted.set(true)
            else
                obsIsOnlineOpted.set(false)
        }
    }

    fun onTaskCompleteBtnClick(view: View?) {
        if (billHandedOverTo.get().toString().isEmpty())
            showToast(app.resources.getString(R.string.valid_msg_bill_handed))
        else if (!obsIsOnlineOpted.get() && (billGivenToName.get().toString()
                .isEmpty() || billGivenToName.get().toString().trim().isEmpty()))
            showToast(app.resources.getString(R.string.valid_msg_bill_given_name))
        else if (!obsIsOnlineOpted.get() && (billGivenToDesignation.get().toString().isEmpty() ||
                    billGivenToDesignation.get().toString().trim().isEmpty()))
            showToast(app.resources.getString(R.string.valid_msg_bill_given_designation))
        else if (!obsIsOnlineOpted.get() && (billImageUri.get() == null || billImageUri.get()!! == Uri.EMPTY))
            showToast(app.resources.getString(R.string.valid_msg_take_photo))
        else
            saveBillSubmissionData()
    }

    fun onBillCheckListClick(view: View?) {
        message.what = NavigationConstants.OPEN_BILL_CHECKLIST_SHEET
        liveData.postValue(message)
    }

    fun onTakePhotoClick(view: View?) {
        message.what = NavigationConstants.ON_TAKE_PICTURE
        liveData.postValue(message)
    }

    private fun saveBillSubmissionData() {
        isLoading.set(View.VISIBLE)
        val taskResultMO = BillSubmitTaskResultMO()

        try {
            if (!obsIsOnlineOpted.get()) {
                val attachmentJson = JSONObject(billMetadata.get().toString())
                taskResultMO.billSubmissionGUID = attachmentJson.getString("uuid")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        taskResultMO.geoLocation = Prefs.getDouble(PrefConstants.LATITUDE).toString() + ", " +
                Prefs.getDouble(PrefConstants.LONGITUDE).toString()

        val checkListJson = JsonObject()
        for (billCheckListMO: BillCheckListMO in updateList.get()!!) {
            checkListJson.addProperty(billCheckListMO.lookUpName!!.replace(" ", ""),
                billCheckListMO.isOptionOpted)
        }

        val billDetails = BillSubmitDetailsMO(billHandedOverTo.get(),
            billGivenToName.get(), billGivenToDesignation.get(), checkListJson)
        taskResultMO.billSubmitDetails = billDetails
        val finalValue = Gson().toJson(taskResultMO)

        if (!::taskEntity.isInitialized)
            return

        //Updating TaskEntity
        taskEntity.apply {
            actualTaskExecutionStartDateTime = actualStartDateTime
            actualTaskExecutionEndDateTime = LocalDateTime.now().toString()
            taskExecutionResult = finalValue
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
            }, { t: Throwable? ->
                isLoading.set(View.GONE)
                Timber.e(t)
            }))

        if (!obsIsOnlineOpted.get())
            insertBillAttachment()
    }

    private fun insertBillAttachment() {
        addDisposable(attachmentDao.insert(AttachmentEntity(billImageUri.get().toString(),
            billMetadata.get()))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                oneTimeWorkerWithNetwork(AttachmentsUploadWorker::class.java)
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
    }

    private fun triggerRotaTaskWorker() {
        val inputData = Data.Builder().putInt(RotaTaskWorker::class.java.simpleName,
            RotaTaskWorker.RotaTaskWorkerType.SYNC_TO_SERVER.workerType).build()
        oneTimeWorkerWithInputData(RotaTaskWorker::class.java, inputData)
    }

    private fun addTimeLine() {
        val dailyTimeline = DailyTimeLineEntity("BillSubmission", "")
        addDisposable(dailyTimeLineDao.insert(dailyTimeline)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("Bill Submission Time Line added")
            }, { t: Throwable? ->
                Timber.e(t)
            }))
    }

    //    fun getBillSubmissionTasksFromServer(weekType: String) {
    fun getBillSubmissionTasksFromServer() {
        /*var currentWeekNumber = TimeUtils.getCurrentWeekOfYear()
        if (weekType == "LAST_WEEK")
            currentWeekNumber -= 1*/

        isLoading.set(View.VISIBLE)
        val currentDate = TimeUtils.YYYY_MM_DD_FORMAT(LocalDate.now())
        //        val currentDate = "2020-11-26"
        //        addDisposable(Single.zip(taskDao.fetchAllAutoBSRotas(), coreApi.getWeeklyBillRotas(currentWeekNumber),
        addDisposable(Single.zip(taskDao.fetchAllAutoBSRotas(),
            coreApi.getMonthlyBillRotas(currentDate),
            BiFunction<List<TaskEntity>, RotaResponse, Boolean> { localBSTasksList, rotaResponse ->
                return@BiFunction onResultFetch(localBSTasksList, rotaResponse)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message.what = NavigationConstants.ON_UPDATING_BS_UI
                liveData.value = message
            }, {
                isLoading.set(View.GONE)
                it.printStackTrace()
            }))
    }

    private fun onResultFetch(bsLocalRotasList: List<TaskEntity>,
        bsRotasResponse: RotaResponse): Boolean {
        isLoading.set(View.GONE)
        if (bsRotasResponse.statusCode == 200 && bsRotasResponse.rota != null && bsRotasResponse.rota.siteTasks != null)
            for (task in bsRotasResponse.rota.siteTasks) {
                if (!bsLocalRotasList.isNullOrEmpty() && isBSRotaExists(task.serverId,
                        bsLocalRotasList)) {
                    // Will handle further cases if required...
                    Timber.e("BillSubmission : Task Already exists")
                } else {
                    val serverTask = TaskEntity(task.siteId,
                        task.taskTypeId,
                        task.taskStatus,
                        task.estimatedTaskExecutionStartDateTime,
                        task.actualTaskExecutionStartDateTime,
                        task.estimatedTaskExecutionEndDateTime,
                        task.actualTaskExecutionEndDateTime,
                        task.isAutoCreation,
                        task.reasonLookUpIdentifier,
                        task.serverId,
                        task.barrackId,
                        task.taskExecutionResult,
                        task.otherTaskTypeLookUpIdentifier,
                        task.description)
                    insertBSRotas(serverTask)
                }
            }
        return true
    }

    private fun isBSRotaExists(serverBsRotaId: Int, bsLocalRotasList: List<TaskEntity>): Boolean {
        for (localBSTask in bsLocalRotasList) {
            if (localBSTask.serverId == serverBsRotaId)
                return true
        }
        return false
    }

    private fun insertBSRotas(task: TaskEntity) {
        addDisposable(taskDao.insert(task)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, { t: Throwable? ->
                Timber.e(t)
            }))
    }
}