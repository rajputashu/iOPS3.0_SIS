package com.sisindia.ai.android.firebase

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.gson.Gson
import com.sisindia.ai.android.models.AdHocNotificationMO
import com.sisindia.ai.android.models.kits.KitDistributionResponseMO
import com.sisindia.ai.android.models.rota.RotaResponse
import com.sisindia.ai.android.rest.CoreApi
import com.sisindia.ai.android.room.IopsDatabase
import com.sisindia.ai.android.room.entities.KitDistributionListEntity
import com.sisindia.ai.android.room.entities.SiteEntity
import com.sisindia.ai.android.room.entities.SitePostEntity
import com.sisindia.ai.android.room.entities.SiteStrengthEntity
import com.sisindia.ai.android.room.entities.TaskEntity
import com.sisindia.ai.android.services.GeoLocationService
import com.sisindia.ai.android.utils.TimeUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import timber.log.Timber

/**
 * Created by Ashu Rajput on 1/12/2021.
 */
class NotificationHandler constructor(val coreApi: CoreApi, val database: IopsDatabase) {

    private var compositeDisposable = CompositeDisposable()

    private fun addDisposable(disposable: Disposable?) {
        compositeDisposable.add(disposable!!)
    }

    /*
     * TRIGGERING BELOW METHOD TO GET NEW AUTO ROTAS
     */
    fun triggerGetRota() {
        //        Timber.e("Coming in Notification Handler triggerGetRota()")
        addDisposable(
            Single.zip(coreApi.serverRotas, database.taskDao().fetchAll(),
                BiFunction<RotaResponse, List<TaskEntity>, Boolean> { rotaResponse, localTaskList ->
                    return@BiFunction onResultFetch(rotaResponse, localTaskList)
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                }, {
                })
        )
    }

    private fun onResultFetch(rotaResponse: RotaResponse,
        localTaskList: List<TaskEntity>): Boolean {
        rotaResponse.let { it ->
            it.rota.let {
                it.rotaTasks?.let { rotaTasks ->
                    addDisposable(database.taskDao().insertAllRotaTasks(rotaTasks)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Timber.e("rotaTasksEntities Inserted")
                        }) { t: Throwable? -> Timber.e(t) })
                }

                if (it.siteTasks.size > 0) {
                    var index: Int
                    var isNcRotaAvailable = false
                    for (serverTask: RotaResponse.SiteTaskResponse in it.siteTasks) {
                        index = localTaskList.indexOfFirst { localTask ->
                            localTask.serverId == serverTask.serverId
                        }
                        //                        Timber.e("Notification : Index Value is $index")
                        if (index > -1) {
                            val existingTask = localTaskList[index]
                            if (existingTask.isSynced && existingTask.taskStatus > serverTask.taskStatus) {
                                //                                Timber.e("Notification : Rota Condition 1")
                            } else if (existingTask.isSynced && existingTask.taskStatus < serverTask.taskStatus) {
                                //                                Timber.e("Notification : Rota Condition 2")
                                updateTaskStatus(serverTask.taskStatus, existingTask.serverId)
                            } else {
                                Timber.e("Notification : Everything is perfect while handling Rotas...)")
                            }
                        } else {
                            val newTask = TaskEntity(
                                serverTask.siteId,
                                serverTask.taskTypeId,
                                serverTask.taskStatus,
                                serverTask.estimatedTaskExecutionStartDateTime,
                                serverTask.actualTaskExecutionStartDateTime,
                                serverTask.estimatedTaskExecutionEndDateTime,
                                serverTask.actualTaskExecutionEndDateTime,
                                serverTask.isAutoCreation,
                                serverTask.reasonLookUpIdentifier,
                                serverTask.serverId,
                                serverTask.barrackId,
                                serverTask.taskExecutionResult,
                                serverTask.otherTaskTypeLookUpIdentifier, serverTask.description)
                            insertNewTask(newTask)
                            // {Checking whether NC Rota available to insert into DB}
                            isNcRotaAvailable = (serverTask.taskTypeId == 2)
                        }
                    }

                    if (isNcRotaAvailable)
                        triggerRefreshMySitesData()
                }
            }
        }
        return true
    }

    private fun insertNewTask(taskEntity: TaskEntity) {
        addDisposable(database.taskDao().insert(taskEntity)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("Notification: New Task Inserted...")
            }) { t: Throwable? -> Timber.e(t) })
    }

    private fun updateTaskStatus(status: Int, localTaskId: Int) {
        addDisposable(database.taskDao().updateTaskStatus(status, localTaskId)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("Status of Task is updated....")
            }) { t: Throwable? -> Timber.e(t) })
    }

    /*
     * TRIGGERING BELOW METHOD TO GET NEW BILL SUBMISSION TASKS
     */
    fun triggerGetNewBill() {
        val currentDate = TimeUtils.YYYY_MM_DD_FORMAT(LocalDate.now())
        addDisposable(Single.zip(database.taskDao().fetchAllAutoBSRotas(),
            coreApi.getMonthlyBillRotas(currentDate),
            BiFunction<List<TaskEntity>, RotaResponse, Boolean> { localBSTasksList, rotaResponse ->
                return@BiFunction onResultFetch(localBSTasksList, rotaResponse)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, {
                printError(it, "NEW_BILL")
            }))
    }

    private fun onResultFetch(bsLocalRotasList: List<TaskEntity>,
        bsRotasResponse: RotaResponse): Boolean {
        if (bsRotasResponse.statusCode == 200 && bsRotasResponse.rota != null && bsRotasResponse.rota.siteTasks != null)
            for (task in bsRotasResponse.rota.siteTasks) {
                val index =
                    bsLocalRotasList.indexOfFirst { localTaskEntity -> localTaskEntity.serverId == task.serverId }
                if (index == -1) {
                    val serverTask = TaskEntity(
                        task.siteId,
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
                        task.otherTaskTypeLookUpIdentifier, task.description)
                    insertBSRotas(serverTask)
                } else
                    Timber.e("Notification BillSubmission : Task Already exists")
            }
        return true
    }

    private fun insertBSRotas(task: TaskEntity) {
        addDisposable(
            database.taskDao().insert(task)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ }, { printError(it, "BILL_SUBMISSION") })
        )
    }

    /*
     * TRIGGERING BELOW METHOD TO GET NEW SITE AT RISKS TASKS
     */
    fun triggerGetNewSiteAtRisk() {
        addDisposable(coreApi.getMonthlyAtRiskPOAs(TimeUtils.YYYY_MM_DD_FORMAT(LocalDate.now()))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ atRiskResponse ->
                if (atRiskResponse.statusCode == 200 && !atRiskResponse.siteRisks.isNullOrEmpty()) {
                    addDisposable(
                        database.siteAtRiskDao().deleteSiteAtRisk()
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                addDisposable(
                                    database.siteAtRiskDao()
                                        .insertAllSiteAtRisk(atRiskResponse.siteRisks)
                                        .subscribeOn(Schedulers.computation())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({
                                            Timber.i("siteRisks inserted")
                                        }, { t: Throwable? -> Timber.e(t) })
                                )
                            }, { t: Throwable? -> Timber.e(t) })
                    )

                    addDisposable(
                        database.siteAtRiskPoaDao().deleteSiteRiskPoa()
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                for (risk in atRiskResponse.siteRisks) {
                                    if (risk.siteRiskPos != null && risk.siteRiskPos.size != 0) {
                                        addDisposable(
                                            database.siteAtRiskPoaDao()
                                                .insertAllAtRiskPOA(risk.siteRiskPos)
                                                .subscribeOn(Schedulers.newThread())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe({
                                                    Timber.i("siteRiskPos inserted")
                                                }, { t: Throwable? -> Timber.e(t) }))
                                    }
                                }
                            }, { t: Throwable? -> Timber.e(t) })
                    )
                } else {
                    deleteAtRiskAndPoaDataFromTable()
                }
            }) { printError(it, "POA_SITE_AT_RISK") })
    }

    private fun deleteAtRiskAndPoaDataFromTable() {
        addDisposable(
            Single.zip(database.siteAtRiskDao().deleteSiteAtRisk().toSingleDefault(""),
                database.siteAtRiskPoaDao().deleteSiteRiskPoa().toSingleDefault(""),
                BiFunction<String, String, Boolean> { _, _ ->
                    return@BiFunction true
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                }, {
                    printError(it, "DELETE_POA")
                }))
    }

    /*
     * TRIGGERING BELOW METHOD TO GET SITES AND DELETE-INSERT SITES IN DB
     */
    fun triggerRefreshMySitesData() {
        addDisposable(coreApi.refreshedSiteList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.statusCode == 200 && it.mySitesData?.mySitesList != null) {
                    deleteAndInsertSites(it?.mySitesData?.mySitesList)
                    deleteAndInsertSiteStrength(it?.mySitesData?.siteStrengthList)
                    deleteAndInsertSitePosts(it?.mySitesData?.sitePostList)
                }
            }, { }))
    }

    private fun deleteAndInsertSites(mySiteList: List<SiteEntity>?) {
        if (!mySiteList.isNullOrEmpty()) {
            addDisposable(
                database.iopsSiteDao().deleteSite()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        addDisposable(
                            database.iopsSiteDao().insertAllSites(mySiteList)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ }, { })
                        )
                    }, { t: Throwable? -> Timber.e(t) })
            )

            addDisposable(database.siteExtensionDao().deleteSiteExtension()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    //                    mySiteList.forEach { _ -> }
                    for (site in mySiteList) {
                        if (site.siteExtension != null) {
                            site.siteExtension.siteId = site.id
                            addDisposable(
                                database.siteExtensionDao()
                                    .insertSiteExtension(site.siteExtension)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({ }, { })
                            )
                        }
                    }
                })

            addDisposable(database.siteCheckListDao().deleteSiteCheckList()
                .mergeWith(database.siteCheckListDao().deleteSiteCheckListOption())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    for (site in mySiteList) {
                        if (site.siteExtension != null && site.siteExtension.siteConfiguration != null) {
                            val siteConfiguration = site.siteExtension.siteConfiguration
                            if (siteConfiguration.siteChecklists != null && siteConfiguration.siteChecklists.size != 0) {
                                for (checkList in siteConfiguration.siteChecklists) {
                                    checkList.siteId = site.id
                                    addDisposable(
                                        database.siteCheckListDao()
                                            .insertSiteCheckList(checkList)
                                            .subscribeOn(Schedulers.computation())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe({
                                                if (checkList.siteChecklistOptions != null && checkList.siteChecklistOptions.size != 0) {
                                                    for (option in checkList.siteChecklistOptions) {
                                                        option.siteId = site.id
                                                        option.siteChecklistId =
                                                            checkList.siteChecklistId
                                                        addDisposable(
                                                            database.siteCheckListDao()
                                                                .insertSiteChecklistOption(
                                                                    option
                                                                )
                                                                .subscribeOn(Schedulers.newThread())
                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                .subscribe({ },
                                                                    { t: Throwable? ->
                                                                        Timber.e(
                                                                            t
                                                                        )
                                                                    })
                                                        )
                                                    }
                                                }
                                            }, { t: Throwable? -> Timber.e(t) })
                                    )
                                }
                            }
                        }
                    }
                })
        }
    }

    private fun deleteAndInsertSiteStrength(siteStrengthList: List<SiteStrengthEntity>?) {
        if (!siteStrengthList.isNullOrEmpty()) {
            addDisposable(
                database.siteStrengthDao().deleteSiteStrength()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        addDisposable(
                            database.siteStrengthDao().insertAllSiteStrength(siteStrengthList)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({}, { t: Throwable? -> Timber.e(t) })
                        )
                    }, { t: Throwable? -> Timber.e(t) })
            )
        }
    }

    private fun deleteAndInsertSitePosts(sitePostList: List<SitePostEntity>?) {
        if (!sitePostList.isNullOrEmpty()) {
            addDisposable(
                database.sitePostDao().deleteSitePost()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        addDisposable(
                            database.sitePostDao().insertAllSitePost(sitePostList)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ }, { t: Throwable? -> Timber.e(t) })
                        )
                    }, { t: Throwable? -> Timber.e(t) })
            )

            addDisposable(
                database.postRegistersDao().deletePostRegister()
                    .mergeWith(database.postCheckListDao().deletePostCheckList())
                    .mergeWith(database.postCheckListDao().deletePostCheckListOption())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        for (post in sitePostList) {
                            if (post.postConfiguration != null && post.postConfiguration.postRegisters != null) {
                                for (pr in post.postConfiguration.postRegisters) {
                                    pr.siteId = post.siteId
                                    pr.postId = post.id
                                    addDisposable(
                                        database.postRegistersDao().insertPostRegister(pr)
                                            .subscribeOn(Schedulers.newThread())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe({}, { t: Throwable? -> Timber.e(t) })
                                    )
                                }
                            }
                            if (post.postConfiguration != null && post.postConfiguration.postChecklists != null) {
                                for (pc in post.postConfiguration.postChecklists) {
                                    pc.siteId = post.siteId
                                    pc.postId = post.id
                                    addDisposable(
                                        database.postCheckListDao().insertPostCheckList(pc)
                                            .subscribeOn(Schedulers.newThread())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe({
                                                if (pc.postChecklistOptions != null) {
                                                    for (pco in pc.postChecklistOptions) {
                                                        pco.siteId = post.siteId
                                                        pco.postId = post.id
                                                        pco.postChecklistId = pc.postChecklistId
                                                        addDisposable(
                                                            database.postCheckListDao()
                                                                .insertPostCheckListOption(pco)
                                                                .subscribeOn(Schedulers.newThread())
                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                .subscribe({ },
                                                                    { t: Throwable? -> Timber.e(t) })
                                                        )
                                                    }
                                                }
                                            }, { t: Throwable? -> Timber.e(t) })
                                    )
                                }
                            }
                        }
                    }, { t: Throwable? -> Timber.e(t) })
            )
        }
    }

    /*
     * TRIGGERING BELOW METHOD TO GET NEW ANNUAL KIT REPLACEMENT DETAILS
     */
    fun triggerGetNewAKRs() {
        addDisposable(Single.zip(database.kitItemDao().fetchAllKitListIds(),
            coreApi.kitDistributionList,
            BiFunction<List<Int>, KitDistributionResponseMO, Boolean> { localAkrList, akrResponse ->
                return@BiFunction onResultFetch(localAkrList, akrResponse)
            })
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ }, { printError(it, "AKR_SYNC") }))
    }

    private fun onResultFetch(akrIds: List<Int>, akrResponse: KitDistributionResponseMO): Boolean {
        var isAkrKitInserted = false
        if (akrResponse.statusCode == 200 && !akrResponse.kitDistributionList.isNullOrEmpty()) {
            for (task in akrResponse.kitDistributionList!!) {
                val index = akrIds.indexOfFirst { localId -> localId == task.id }
//                Timber.e("AKR Result Local Index $index")
                if (index == -1) {
                    insertKitDistributionListAndKits(task)
                    isAkrKitInserted = true
                } else
                    Timber.e("Notification AKR Kit : Task Already exists")
            }
        }
        return isAkrKitInserted
    }

    private fun insertKitDistributionListAndKits(kitList: KitDistributionListEntity) {
        addDisposable(database.kitItemDao().insertAkrKit(kitList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ }, { Timber.d("Notification : AKR Kit Not inserted") }))

        if (!kitList.kitDistributionItems.isNullOrEmpty()) {
            kitList.kitDistributionItems.apply {
                addDisposable(database.kitItemDao().insertAllKitDistributionItems(this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ }, { Timber.d("Notification : AKR Kit LIST Not inserted") }))
            }
        }
    }

    /*
     * TRIGGERING BELOW METHOD TO UPDATE ADHOC TASK DATA {APPROVED DATE TIME AND ID}
     */
    fun triggerToUpdateAdHocTask(jsonData: String? = null) {
        if (!jsonData.isNullOrEmpty()) {
            val adHocTaskMO = Gson().fromJson(jsonData, AdHocNotificationMO::class.java)
            addDisposable(database.taskDao().updateAdHocTask(adHocTaskMO.taskId,
                adHocTaskMO.approvedOn, adHocTaskMO.approvedBy, adHocTaskMO.taskStatus)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("Notification : AdHocTask Updated Successfully")
                }, { printError(it, "AdHocApproved") }))
        }
    }

    private fun printError(throwable: Throwable, module: String) {
        Timber.e("$module Notification Error has occurred")
        throwable.printStackTrace()
    }

    fun triggerToStartLocationService(context: Context) {
        if (!isLocationServiceRunning(context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context.startForegroundService(Intent(context, GeoLocationService::class.java))
            else
                context.startService(Intent(context, GeoLocationService::class.java))
        }
    }

    private fun isLocationServiceRunning(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (GeoLocationService::class.java.name == service.service.className)
                return true
        }
        return false
    }

    fun triggerSQLQuery(rawQuery: String) {
        addDisposable(database.rawQueriesDao()
            .deleteOrUpdateViaRawQuery(SimpleSQLiteQuery(rawQuery))
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.e("Query Executed Successfully $it")
            }, { t: Throwable? -> Timber.e(t) }))
    }
}