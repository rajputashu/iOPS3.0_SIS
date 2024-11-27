package com.sisindia.ai.android.workers

import android.content.Context
import androidx.work.WorkerParameters
import com.droidcommons.dagger.worker.AndroidWorkerInjection
import com.sisindia.ai.android.base.BaseWorker
import com.sisindia.ai.android.commons.SyncPOA
import com.sisindia.ai.android.models.poa.ImprovementPoaResponseMO
import com.sisindia.ai.android.room.dao.ImprovementPoaDao
import com.sisindia.ai.android.room.dao.SiteAtRiskDao
import com.sisindia.ai.android.room.dao.SiteRiskPoaDao
import com.sisindia.ai.android.room.entities.ImprovementPoaEntity
import com.sisindia.ai.android.utils.TimeUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 1/23/2021.
 */
class SyncPoaWorker @Inject constructor(context: Context, workerParameters: WorkerParameters) :
//    ListenableWorker(context, workerParameters){
    BaseWorker(context, workerParameters) {

    @Inject
    lateinit var improvementPoaDao: ImprovementPoaDao

    @Inject
    lateinit var siteRiskPoaDao: SiteRiskPoaDao

    @Inject
    lateinit var siteAtRiskDao: SiteAtRiskDao

    init {
        AndroidWorkerInjection.inject(this)
    }

    override fun doWork(): Result {
        when (inputData.getInt(SyncPoaWorker::class.java.simpleName, SyncPOA.DEFAULT.typeId)) {
            SyncPOA.AT_RISK.typeId -> syncAtRiskPoaFromServer()
            SyncPOA.IMPROVEMENT_PLAN.typeId -> syncImprovementPoaFromServer()
            else -> Timber.e("Default POA is executing")
        }
        return Result.success()
    }

    /*GETTING AND SYNCING AT RISK POAs FROM SERVER*/
    private fun syncAtRiskPoaFromServer() {
        addDisposable(coreApi.getMonthlyAtRiskPOAs(TimeUtils.YYYY_MM_DD_FORMAT(LocalDate.now())).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({ atRiskResponse ->
                if (atRiskResponse.statusCode == 200 && !atRiskResponse.siteRisks.isNullOrEmpty()) {
                    addDisposable(siteAtRiskDao.deleteSiteAtRisk().subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            addDisposable(siteAtRiskDao
                                .insertAllSiteAtRisk(atRiskResponse.siteRisks)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ rows: List<Long?>? ->
                                    Timber.i("siteRisks inserted")
                                }, { t: Throwable? -> Timber.e(t) }))
                        }, { t: Throwable? -> Timber.e(t) }))

                    addDisposable(siteRiskPoaDao.deleteSiteRiskPoa()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            for (risk in atRiskResponse.siteRisks) {
                                if (risk.siteRiskPos != null && risk.siteRiskPos.size != 0) {
                                    addDisposable(siteRiskPoaDao.insertAllAtRiskPOA(risk.siteRiskPos)
                                        .subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({ rows: List<Long?>? ->
                                            Timber.i("siteRiskPos inserted")
                                        }, { t: Throwable? -> Timber.e(t) }))
                                }
                            }
                        }, { t: Throwable? -> Timber.e(t) }))
                } else {
                    deleteAtRiskAndPoaDataFromTable()
                }
            }) { throwable: Throwable? ->
                onApiError(throwable)
            })
    }

    private fun deleteAtRiskAndPoaDataFromTable() {
        addDisposable(Single.zip(siteAtRiskDao.deleteSiteAtRisk().toSingleDefault(""),
            siteRiskPoaDao.deleteSiteRiskPoa().toSingleDefault(""),
            BiFunction<String, String, Boolean> { _, _ ->
                return@BiFunction true
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ }, {
                it.printStackTrace()
            }))
    }

    /*GETTING AND SYNCING IMPROVEMENT PLAN POAs FROM SERVER*/
    private fun syncImprovementPoaFromServer() {
        addDisposable(Single.zip(coreApi.aiImprovementPlans, improvementPoaDao.getCurrentMonthLocalIPPoa(),
            BiFunction<ImprovementPoaResponseMO, List<ImprovementPoaEntity>, ArrayList<ImprovementPoaEntity>> { serverPoaResponse, localIPPoaList ->
                return@BiFunction onResultFetch(serverPoaResponse, localIPPoaList)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (!it.isNullOrEmpty()) {
                    insertAllImprovementPOAs(it)
                }
            }, { }))
    }

    private fun onResultFetch(serverResponse: ImprovementPoaResponseMO, localResponse: List<ImprovementPoaEntity>): ArrayList<ImprovementPoaEntity> {
        val listOfNewIP = arrayListOf<ImprovementPoaEntity>()
        if (!serverResponse.improvementPlanData.isNullOrEmpty()) {
            var isUnSyncedLocalIPFound = false
            for (serverIP: ImprovementPoaEntity in serverResponse.improvementPlanData) {
                val index = localResponse.indexOfFirst { localIP ->
                    localIP.serverId == serverIP.serverId
                }
                if (index > -1) {
                    if (localResponse[index].status > serverIP.status) {
                        isUnSyncedLocalIPFound = true
                    }
                } else {
                    listOfNewIP.add(serverIP)
                }
            }
            //            if (isUnSyncedLocalIPFound)
            //                oneTimeWorkerWithNetwork(ClosedImprovePlanWorker::class.java)
        }
        return listOfNewIP
    }

    private fun insertAllImprovementPOAs(planList: List<ImprovementPoaEntity>) {
        addDisposable(improvementPoaDao.insertAll(planList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.e("ImprovementPlan All Data is Synced... and inserted into the DB")
            }, { }))
    }
}
