package com.sisindia.ai.android.features.sync

import android.app.Application
import android.os.Handler
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.work.Data
import com.droidcommons.preference.Prefs
import com.google.gson.Gson
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.features.uar.dialog.DialogListener
import com.sisindia.ai.android.models.DeviceInfo
import com.sisindia.ai.android.models.TableSyncResponse
import com.sisindia.ai.android.models.TableSyncResponse.TableSyncData
import com.sisindia.ai.android.rest.AttachmentUploadAPI
import com.sisindia.ai.android.room.dao.AIProfileDao
import com.sisindia.ai.android.room.dao.AttachmentDao
import com.sisindia.ai.android.room.dao.SiteAtRiskDao
import com.sisindia.ai.android.room.dao.SiteRiskPoaDao
import com.sisindia.ai.android.room.dao.TaskDao
import com.sisindia.ai.android.room.entities.AIProfileEntity
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.room.entities.DutyStatusEntity
import com.sisindia.ai.android.room.entities.TaskEntity
import com.sisindia.ai.android.uimodels.attachments.SelfieAttachmentMetadata
import com.sisindia.ai.android.utils.FileUtils
import com.sisindia.ai.android.utils.TimeUtils
import com.sisindia.ai.android.workers.AtRiskPoaWorker
import com.sisindia.ai.android.workers.AttachmentsUploadWorker
import com.sisindia.ai.android.workers.CommonMasterDataWorker
import com.sisindia.ai.android.workers.ComplaintWorker
import com.sisindia.ai.android.workers.GrievanceWorker
import com.sisindia.ai.android.workers.KitDistributionWorker
import com.sisindia.ai.android.workers.UpdateBillCollectionWorker
import com.sisindia.ai.android.workers.UserMasterDataWorkerV2
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import org.threeten.bp.LocalDate
import timber.log.Timber
import javax.inject.Inject

class ManualSyncViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    private var isSync = false
    val handler: Handler = Handler()
    val obsIsDataSyncing = ObservableInt(GONE)
    val obsSyncingStatusMsg = ObservableInt(INVISIBLE)
    private var attachmentUnSyncedCount = 0
    private var tasksUnSyncedCount = 0
    private var isNeedToSyncDutyOnOff = false
    private var isNeedToSyncSelfieAPI = false
    private lateinit var dutyEntity: DutyStatusEntity
    private lateinit var attachmentList: List<AttachmentEntity>
    val loadingMessage = ObservableField<String>("Loading options...")
    val syncStatusMessage = ObservableField<String>("")

    @Inject
    lateinit var deviceInfo: DeviceInfo

    @Inject
    lateinit var siteAtRiskDao: SiteAtRiskDao

    @Inject
    lateinit var siteRiskPoaDao: SiteRiskPoaDao

    @Inject
    lateinit var taskDao: TaskDao

    @Inject
    lateinit var attachmentDao: AttachmentDao

    @Inject
    lateinit var attachmentUploadApi: AttachmentUploadAPI

    @Inject
    lateinit var aiProfileDao: AIProfileDao

    private val syncOptionList = arrayListOf<SyncOptionsMO>()

    fun initManualSyncAdapter() {
        syncOptionList.add(SyncOptionsMO("Sync Master Tables", 1, true))
        syncOptionList.add(SyncOptionsMO("Sync Completed Rota Task", 2, false,
            isNeedToShowCount = true))
        syncOptionList.add(SyncOptionsMO("Sync Grievance", 3, true))
        syncOptionList.add(SyncOptionsMO("Sync Complaints", 4, false))
        syncOptionList.add(SyncOptionsMO("Get POAs", 5, true))
        syncOptionList.add(SyncOptionsMO("Push Task Table To Server", 6, false))
        syncOptionList.add(SyncOptionsMO("Sync Images", 7, false, isNeedToShowCount = true))
        syncOptionList.add(SyncOptionsMO("Duty On/Off", 8, false, isNeedToShowCount = false))
        syncOptionList.add(SyncOptionsMO("Selfie Image", 9, false, isNeedToShowCount = false))
        syncOptionList.add(SyncOptionsMO("AI Profile", 10, true, isNeedToShowCount = false))
        syncOptionList.add(SyncOptionsMO("Push Local DB To Server", 11, false,
            isNeedToShowCount = false))
        syncOptionList.add(SyncOptionsMO("Push POA, AKR, Bill Tasks", 12, false))
        syncOptionList.add(SyncOptionsMO("Sync Inactive Rotas", 13, false))
        getUnSyncedCountsFromDB()
    }

    private fun getUnSyncedCountsFromDB() {
        obsIsDataSyncing.set(VISIBLE)

        addDisposable(
            Single.zip(attachmentDao.fetchCountOfUnSyncedAttachments(),
                taskDao.fetchCountOfUnSyncedTasks(),
                BiFunction<Int, Int, Boolean> { imageCount, taskCount ->
                    return@BiFunction onResultFetch(imageCount, taskCount)
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ }, { })
        )

        addDisposable(
            dutyStatusDao.fetchNotSyncDutyStatus(false).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    var dutyStatusMsg = ""
                    this.dutyEntity = it
                    if (!dutyEntity.dutyOffDateTime.isNullOrEmpty()) {
                        dutyStatusMsg = "Duty Off (not synced)"
                        isNeedToSyncDutyOnOff = true
                    } else if (!dutyEntity.dutyOnDateTime.isNullOrEmpty()) {
                        dutyStatusMsg = "Duty On (not synced)"
                        isNeedToSyncDutyOnOff = true
                    }
                    syncOptionList[7].dutySyncStatus = dutyStatusMsg
                }, {
                    it.printStackTrace()
                })
        )

        addDisposable(
            attachmentDao.fetchAttachmentWhenApiFailed()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    var selfieStatusMsg = ""
                    if (!it.isNullOrEmpty()) {
                        this.attachmentList = it
                        selfieStatusMsg = "(Not Synced)"
                        isNeedToSyncSelfieAPI = true
                    }
                    syncOptionList[8].dutySyncStatus = selfieStatusMsg
                }, {
                    it.printStackTrace()
                })
        )

        Handler().postDelayed({
            obsIsDataSyncing.set(GONE)
            syncAdapter.clearAndSetItems(syncOptionList)
        }, 3000)
    }

    private fun onResultFetch(imageUnSyncedCount: Int, tasksUnSyncedCount: Int): Boolean {
        this.tasksUnSyncedCount = tasksUnSyncedCount
        syncOptionList[1].unSyncedCount = tasksUnSyncedCount
        attachmentUnSyncedCount = imageUnSyncedCount
        syncOptionList[6].unSyncedCount = imageUnSyncedCount
        return true
    }

    val syncAdapter = ManualSyncAdapter()
    val syncListener = object : ManualSyncAdapter.ManualSyncListener {
        override fun onChoosingOption(position: Int) {
            if (isSync) {
                showCustomToast("Please wait another sync is in progress...")
                return
            }

            when (position) {
                1 -> {
//                    onMasterSyncClick()
                    message.what = NavigationConstants.OPEN_MASTER_SYNC_DIALOG
                    liveData.postValue(message)
                    return
                }
                2 -> onTaskDataSyncToServer()
                3 -> onGrievanceSyncClick()
                4 -> onComplaintSyncClick()
                5 -> onPOAsSyncClick()
                6 -> onTasksSyncToServer()
                7 -> uploadUnSyncedImagesToServer()
                8 -> syncDutyStatusToServer()
                9 -> syncSelfieMetaDataToServer()
                10 -> syncAIProfile()
                11 -> backupDatabase()
                12 -> onSyncingOtherTasks()
                13 -> updateInactiveRotas()
            }
            triggerSyncingMessageAndLoader()
        }
    }

    private fun triggerSyncingMessageAndLoader() {
        isSync = true
        obsIsDataSyncing.set(VISIBLE)
        obsSyncingStatusMsg.set(INVISIBLE)
        loadingMessage.set("Syncing please wait...")
    }

    val dialogListener = object : DialogListener {
        override fun onCrossButtonClick() {
        }

        override fun onViewAllPOAClick() {
        }

        override fun onYesButtonClicked() {
            triggerSyncingMessageAndLoader()
            onMasterSyncClick()
        }

        override fun onNoButtonClicked() {
        }
    }

    fun ivRotaDrawerClick(view: View) {
        message.what = NavigationConstants.OPEN_DASH_BOARD_DRAWER
        liveData.postValue(message)
    }

    fun onMasterSyncClick() {
        handler.postDelayed({ onUserSession(deviceInfo) }, 100)
        handler.postDelayed({ oneTimeWorkerWithNetwork(CommonMasterDataWorker::class.java) }, 10000)
        handler.postDelayed({ oneTimeWorkerWithNetwork(UserMasterDataWorkerV2::class.java) }, 20000)
        handler.postDelayed({ setDefaultStatus() }, 40000)
    }

    fun onTaskDataSyncToServer() {
        if (tasksUnSyncedCount > 0) {
            addDisposable(taskDao.fetchAllNotSyncedTask(false)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ tasks: List<TaskEntity> ->
                    if (!tasks.isNullOrEmpty())
                        this.uploadTaskToServerRecursively(tasks, 0)
                }) {
                    showToast("No Items or Nothing to sync...")
                })
        } else {
            setDefaultStatus()
            showToast("No rota task to sync...")
        }
    }

    /*private void uploadToServer(List<TaskEntity> tasks) {
        for (TaskEntity item : tasks) {
            //Changes made by AR
            if (item.serverId == 0 && item.taskStatus == 4) {
                item.taskStatus = 1;
                updateTaskStatusOfCompletedTask(item, false);
            } else {
                addDisposable(coreApi.addOrUpdateCreatedTask(item)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(task -> updateTaskTable(task.data, item), this::onApiError));
            }
        }
    }*/

    private fun uploadTaskToServerRecursively(tasks: List<TaskEntity>, noOfCalls: Int) {
        if (noOfCalls < tasks.size) {
            addDisposable(coreApi.addOrUpdateCreatedTask(tasks[noOfCalls])
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { task: TableSyncResponse ->
                        if (task.statusCode == 200) updateTaskTable(task.data, tasks[noOfCalls])
                        uploadTaskToServerRecursively(tasks, noOfCalls + 1)
                    }) { e: Throwable? ->
                    uploadTaskToServerRecursively(tasks, noOfCalls + 1)
                })
        } else {
            showCustomToast("Please wait! Updating rota task syncing count")
            setDefaultStatus()
            getUnSyncedCountsFromDB()
        }
    }

    /*private fun uploadUnSyncedRotasToServer(tasks: List<TaskEntity>) {
        var taskSyncCounter = 0
        for (item in tasks) {
            *//*if (item.serverId == 0 && item.taskStatus == 4) {
                item.taskStatus = 1
                updateTaskStatusOfCompletedTask(item, false)
            } else {*//*

            addDisposable(coreApi.addOrUpdateCreatedTask(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ task: TableSyncResponse ->
                    if (task.statusCode == 200) {
                        taskSyncCounter++
                        updateTaskTable(task.data, item)
                        if (tasksUnSyncedCount == taskSyncCounter) {
                            showToast("Rota Tasks Synced To Server")
                            setDefaultStatus()
                            getUnSyncedCountsFromDB()
                        }
                    } else {
                        taskSyncCounter++
                        if (tasksUnSyncedCount == taskSyncCounter) {
                            setDefaultStatus()
                            showCustomToast("API Error, unable to sync all ROTA task")
                            getUnSyncedCountsFromDB()
                        }
                    }
                }) {
                    taskSyncCounter++
                    if (tasksUnSyncedCount == taskSyncCounter) {
                        setDefaultStatus()
                        showCustomToast("Internal Server Error, unable to sync all ROTA task")
                        getUnSyncedCountsFromDB()
                    }
                })
            //            }
        }
    }*/

    private fun updateTaskTable(data: TableSyncData?, item: TaskEntity) {
        if (data != null && data.serverId != 0)
            addDisposable(taskDao.updateTaskOnServerSync(data.serverId, item.localId, true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ Timber.d("Manual Sync updated Rota task") })
                { t: Throwable? ->
                    Timber.e(t)
                })
    }

    private fun onSyncingOtherTasks() {
        oneTimeWorkerWithNetwork(AtRiskPoaWorker::class.java)
        handler.postDelayed({
            oneTimeWorkerWithNetwork(KitDistributionWorker::class.java)
        }, 3000)
        handler.postDelayed({
            oneTimeWorkerWithNetwork(UpdateBillCollectionWorker::class.java)
        }, 6000)
        handler.postDelayed({
            getUnSyncedCountsFromDB()
            setDefaultStatus()
        }, 10000)
    }

    fun onGrievanceSyncClick() {
        val rotaInput = Data.Builder().putInt(
            GrievanceWorker::class.java.simpleName,
            GrievanceWorker.GrievanceWorkerType.SYNC_FROM_SERVER.workerType
        ).build()
        oneTimeWorkerWithInputData(GrievanceWorker::class.java, rotaInput)

        handler.postDelayed({
            val rotaInputWithData = Data.Builder().putInt(
                GrievanceWorker::class.java.simpleName,
                GrievanceWorker.GrievanceWorkerType.SYNC_TO_SERVER.workerType
            ).build()
            oneTimeWorkerWithInputData(GrievanceWorker::class.java, rotaInputWithData)
        }, 5000)

        handler.postDelayed({
            setDefaultStatus()
        }, 12000)
    }

    fun onComplaintSyncClick() {
        val rotaInputFromServer = Data.Builder().putInt(
            ComplaintWorker::class.java.simpleName,
            ComplaintWorker.ComplaintWorkerType.SYNC_FROM_SERVER.workerType
        ).build()
        oneTimeWorkerWithInputData(ComplaintWorker::class.java, rotaInputFromServer)

        handler.postDelayed({
            val rotaInput = Data.Builder().putInt(
                ComplaintWorker::class.java.simpleName,
                ComplaintWorker.ComplaintWorkerType.SYNC_TO_SERVER.workerType
            ).build()
            oneTimeWorkerWithInputData(ComplaintWorker::class.java, rotaInput)
        }, 5000)

        handler.postDelayed({
            setDefaultStatus()
        }, 10000)
    }

    fun onPOAsSyncClick() {
        addDisposable(coreApi.getMonthlyAtRiskPOAs(TimeUtils.YYYY_MM_DD_FORMAT(LocalDate.now()))
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ atRiskResponse ->
                if (atRiskResponse.statusCode == 200) {
                    setDefaultStatus()
                    if (!atRiskResponse.siteRisks.isNullOrEmpty()) {
                        addDisposable(
                            siteAtRiskDao.deleteSiteAtRisk()
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                                    addDisposable(
                                        siteAtRiskDao.insertAllSiteAtRisk(atRiskResponse.siteRisks)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe({ rows: List<Long?>? ->
                                                Timber.i("siteRisks inserted")
                                            }, { t: Throwable? -> Timber.e(t) })
                                    )
                                }, { t: Throwable? -> Timber.e(t) })
                        )

                        addDisposable(
                            siteRiskPoaDao.deleteSiteRiskPoa()
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                                    for (risk in atRiskResponse.siteRisks) {
                                        if (risk.siteRiskPos != null && risk.siteRiskPos.size != 0) {
                                            addDisposable(
                                                siteRiskPoaDao.insertAllAtRiskPOA(risk.siteRiskPos)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe({ rows: List<Long?>? ->
                                                        Timber.i("siteRiskPos inserted")
                                                    }, { t: Throwable? -> Timber.e(t) })
                                            )
                                        }
                                    }
                                }, { t: Throwable? -> Timber.e(t) })
                        )

                        addDisposable(siteRiskPoaDao.deleteSiteRiskReasons()
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Timber.i("ManualSync class SiteRiskReasons deleted")
                                for (risk in atRiskResponse.siteRisks) {
                                    if (risk.siteRiskReasons != null && risk.siteRiskReasons.isNotEmpty()) {
                                        addDisposable(siteRiskPoaDao.insertAllAtRiskReasons(risk.siteRiskReasons)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe({
                                                Timber.i("siteRiskPos inserted")
                                            }) { t: Throwable? -> Timber.e(t) })
                                    }
                                }
                            }) { t: Throwable? -> Timber.e(t) })

                    } else deleteAtRiskAndPoaDataFromTable()
                } else {
                    setDefaultStatus()
                    setSyncStatusMsg(atRiskResponse.statusMessage)
                }
            }) {
                setDefaultStatus()
                setSyncStatusMsg("Error! Please try again...")
            })
    }

    private fun deleteAtRiskAndPoaDataFromTable() {
        addDisposable(Single.zip(siteAtRiskDao.deleteSiteAtRisk()
            .toSingleDefault(""), siteRiskPoaDao.deleteSiteRiskPoa()
            .toSingleDefault(""), BiFunction<String, String, Boolean> { _, _ ->
            return@BiFunction true
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({}, {
            it.printStackTrace()
        })
        )
    }

    private fun onTasksSyncToServer() {
        addDisposable(taskDao.fetchAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                sendAllTaskToServer(it)
            }, {
                it.printStackTrace()
                setDefaultStatus()
            }))
    }

    private fun sendAllTaskToServer(taskList: List<TaskEntity>) {
        addDisposable(coreApi.sendAllTaskToServer(taskList).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                setDefaultStatus()
                if (it.statusCode == 200)
                    showToast("Done, Task Table Synced To Server")
                else
                    setSyncStatusMsg(it.statusMessage)
            }, {
                setDefaultStatus()
                setSyncStatusMsg("Error! Please try again...")
            }))
    }

    private fun uploadUnSyncedImagesToServer() {
        if (attachmentUnSyncedCount > 0) {
            showToast("Image syncing will take time to upload, So please wait...")
            oneTimeWorkerWithNetwork(AttachmentsUploadWorker::class.java)
            val delayTimer: Long = when (attachmentUnSyncedCount) {
                in 1..5 -> 15000L
                in 5..10 -> 25000L
                else -> 35000L
            }
            handler.postDelayed({
                showToast("Please wait, updating attachments count")
                setDefaultStatus()
                getUnSyncedCountsFromDB()
            }, delayTimer)
        } else {
            setDefaultStatus()
            showCustomToast(" All attachments are synced to server... ")
        }
    }

    //TODO : Push all duty ON OFF records to server instead of latest one
    private fun syncDutyStatusToServer() {
        if (isNeedToSyncDutyOnOff && this::dutyEntity.isInitialized) {
            addDisposable(coreApi.isDutyOnOff(dutyEntity).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: TableSyncResponse ->
                    if (response.statusCode == 200) {
                        setDefaultStatus()
                        onTableSynced(response.data, dutyEntity.localId)
                        isNeedToSyncDutyOnOff = true
                        syncOptionList[7].dutySyncStatus = ""
                        syncAdapter.clearAndSetItems(syncOptionList)
                    } else {
                        setSyncStatusMsg(response.statusMessage)
                    }
                }) {
                    setDefaultStatus()
                    setSyncStatusMsg("Server Error! Please try again...")
                })
        } else setDefaultStatus()
    }

    private fun syncSelfieMetaDataToServer() {
        if (isNeedToSyncSelfieAPI && this::attachmentList.isInitialized && attachmentList.isNotEmpty()) {
            val metaDataJsonObject = JSONObject(attachmentList[0].attachmentMetaData)
            var userContainerSAS = Prefs.getString(PrefConstants.SAS_USER_CONTAINER_KEY)
            userContainerSAS =
                userContainerSAS.replace("?", "/${metaDataJsonObject.getString("storagePath")}?")
            val metaData = Gson().fromJson(
                attachmentList[0].attachmentMetaData,
                SelfieAttachmentMetadata::class.java
            )
            metaData.path = userContainerSAS
            addDisposable(
                coreApi.pushDutyOnMetaDataAttachment(metaData)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        setDefaultStatus()
                        if (it.statusCode == 200) {
                            setSyncStatusMsg(" Selfie synced to server, Successfully ")
                            //                        getUnSyncedCountsFromDB()
                            isNeedToSyncDutyOnOff = true
                            syncOptionList[8].dutySyncStatus = ""
                            syncAdapter.clearAndSetItems(syncOptionList)
                        } else {
                            setSyncStatusMsg(it.statusMessage)
                        }
                    }, {
                        setDefaultStatus()
                        setSyncStatusMsg("Server Error! Please try again...")
                    }))
        } else setDefaultStatus()
    }

    private fun onTableSynced(data: TableSyncData?, localId: String) {
        if (data != null && data.serverId != 0) {
            addDisposable(dutyStatusDao.updateOnSyncToServer(localId, data.serverId, true)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ }) { t: Throwable? -> Timber.e(t) })
        }
    }

    private fun syncAIProfile() {
        /*oneTimeWorkerWithNetwork(AIUserProfileWorker::class.java)
        handler.postDelayed({
            showToast(" Profile updated... ")
            message.what = NavigationConstants.ON_DASHBOARD_ROTA_CLICK
            liveData.postValue(message)
        }, 8000)*/

        addDisposable(coreApi.fetchAIProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setDefaultStatus()
                if (it.statusCode == 200) {
                    showCustomToast(" Profile Updated Successfully ")
                    insertAIProfileToDB(it.aiProfileData!!)
                } else
                    showCustomToast(" Error while updating profile from server ")
            }, {
                setDefaultStatus()
                showCustomToast(" Error while updating profile from server ")
            }))
    }

    private fun insertAIProfileToDB(aiProfileEntity: AIProfileEntity) {
        Prefs.putInt(PrefConstants.AREA_INSPECTOR_ID, aiProfileEntity.id)
        Prefs.putString(PrefConstants.AREA_INSPECTOR_NAME, aiProfileEntity.employeeName)
        Prefs.putInt(PrefConstants.COMPANY_ID, aiProfileEntity.companyId)
        addDisposable(aiProfileDao.insertMasterAIDetails(aiProfileEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ rowId ->
                Timber.e("AI data inserted successfully - $rowId")
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
    }

    private fun updateInactiveRotas() {
        addDisposable(coreApi.inActiveRota.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                setDefaultStatus()
                if (it.statusCode == 200) {
                    if (it.data!!.isNotEmpty())
                        updateStatusOfRotasToInActive(it.data)
                    else
                        setSyncStatusMsg("Nothing to update")
                } else
                    setSyncStatusMsg(it.statusMessage)
            }, {
                setDefaultStatus()
                setSyncStatusMsg("Error! Please try again...")
            }))
    }

    private fun updateStatusOfRotasToInActive(idsList: List<Int>) {
//        addDisposable(taskDao.updateStatusToInActive(idsList)
        addDisposable(taskDao.deleteInActiveRotas(idsList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                showToast("Status of rotas updated")
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
    }

    private fun backupDatabase() {
        val dbFile = FileUtils.saveDB(app)
        val attachmentEntity = AttachmentEntity(AttachmentEntity.AttachmentSourceType.LOCAL_DB)
        val builder = StringBuilder()
        builder.append(attachmentEntity.attachmentSourceType)
        builder.append("_")
        builder.append(Prefs.getInt(PrefConstants.AREA_INSPECTOR_ID))
        builder.append("_")
        builder.append(attachmentEntity.attachmentGuid)
        //        builder.append(FileUtils.EXT_JPG)
        attachmentEntity.storagePath = builder.toString()

        val headerMap = HashMap<String, Any>()
        headerMap["x-ms-blob-type"] = "BlockBlob"
        headerMap["x-ms-meta-attachmentTypeId"] = 4
        headerMap["x-ms-meta-attachmentSourceTypeId"] = attachmentEntity.attachmentSourceType
        headerMap["x-ms-meta-uuid"] = attachmentEntity.attachmentGuid
        headerMap["x-ms-meta-fileName"] = builder.toString()
        headerMap["x-ms-meta-fileSize"] = (dbFile.length() / 1024)
        val azureStoragePath =
            "LocalDB/" + Prefs.getString(PrefConstants.AREA_INSPECTOR_NAME) + "/" + attachmentEntity.storagePath
        headerMap["x-ms-meta-storagePath"] = azureStoragePath

        if (dbFile.exists()) {
            val fileMimeType = "application/x-sqlite3"
            val requestBody = dbFile.asRequestBody(fileMimeType.toMediaTypeOrNull())
            var userContainerSAS = Prefs.getString(PrefConstants.SAS_USER_CONTAINER_KEY)
            userContainerSAS = userContainerSAS.replace("?", "/${azureStoragePath}?")
            addDisposable(attachmentUploadApi.uploadFileWithHeader(headerMap,
                userContainerSAS, requestBody)!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setDefaultStatus()
                    showToast("iOPS database uploaded successfully")
                }, { throwable: Throwable? ->
                    setDefaultStatus()
                    showToast("Error! while saving your iOPS Database")
                    throwable!!.printStackTrace()
                }))
        }
    }

    private fun setDefaultStatus() {
        obsIsDataSyncing.set(GONE)
        isSync = false
    }

    private fun setSyncStatusMsg(errorMsg: String) {
        obsSyncingStatusMsg.set(VISIBLE)
        syncStatusMessage.set(errorMsg)
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(null)
        super.onCleared()
    }

    private fun showCustomToast(message: String) {
        val toast: Toast = Toast.makeText(app, message, Toast.LENGTH_LONG)
        //        toast.view.setBackgroundColor(Color.parseColor("#ffa400"))
        toast.view?.setBackgroundResource(R.color.colorStatusDone)
        toast.show()
    }
}