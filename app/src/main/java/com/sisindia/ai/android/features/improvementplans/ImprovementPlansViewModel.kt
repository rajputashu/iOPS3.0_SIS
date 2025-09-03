package com.sisindia.ai.android.features.improvementplans

import android.app.Application
import android.text.Spanned
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.features.uar.UARListener
import com.sisindia.ai.android.models.poa.ImprovementPoaResponseMO
import com.sisindia.ai.android.room.customdao.UarAndPoaDao
import com.sisindia.ai.android.room.dao.ImprovementPoaDao
import com.sisindia.ai.android.room.entities.ImprovementPoaEntity
import com.sisindia.ai.android.uimodels.uarpoa.SitesWithImprovePlansMO
import com.sisindia.ai.android.utils.toHtmlSpan
import com.sisindia.ai.android.workers.ClosedImprovePlanWorker
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 12/14/2020.
 */
class ImprovementPlansViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    /*val sitesWithPoaCount = ObservableField(getUpdatedSitesWithPoaCount("0"))
    var planOfActionCount = ObservableField(getUpdatedOpenPoas("0/0"))
    var labelPendingIPCounts = ObservableField(getUpdatedUARLabelCount("0"))*/
    val sitesWithPoaAdapter = SitesWithPoaAdapter()
    val isAtRiskDataAvailable = ObservableBoolean(false)

    @Inject
    lateinit var improvementPoaDao: ImprovementPoaDao

    @Inject
    lateinit var dao: UarAndPoaDao

    val uarListener = object : UARListener {
        override fun onUnitAtRiskSelected(selectedUAR: Any) {
            message.what = NavigationConstants.OPEN_IMPROVEMENT_PLAN_POA
            message.obj = selectedUAR
            liveData.postValue(message)
        }

        override fun onPendingPOASelected(selectedPOA: Any) {
        }
    }

    private fun getUpdatedSitesWithPoaCount(uarCount: String): Spanned {
        return app.getString(R.string.dynamic_string, uarCount, "Sites with POA").toHtmlSpan()
    }

    private fun getUpdatedOpenPoas(poaCount: String): Spanned {
        return app.getString(R.string.dynamic_string, poaCount, "Open POAs").toHtmlSpan()
    }

    private fun getUpdatedUARLabelCount(uarCount: String): String {
        return app.getString(R.string.dynamic_pending_plans_count, uarCount)
    }

    /* fun onClickDelegate(view: View) {
         when (view.id) {
             R.id.headerUAR -> {
                 message.what = NavigationConstants.OPEN_DASH_BOARD_DRAWER
                 liveData.postValue(message)
             }
             R.id.getLatestPOAs -> {
                 getImprovementPlans()
             }
         }
     }*/

    fun onAddImprovementPlans(view: View) {
        message.what = NavigationConstants.ON_ADDING_NEW_IMPROVE_PLAN
        liveData.postValue(message)
    }

    fun getHeaderUnitsAndPOAsCount() {
        setIsLoading(true)
        addDisposable(improvementPoaDao.fetchIPSiteAndPoaCount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ model ->
                setIsLoading(false)
//                sitesWithPoaCount.set(getUpdatedSitesWithPoaCount("${model.sitesWithPoa}"))
//                planOfActionCount.set(getUpdatedOpenPoas("${model.pendingPlanPoa}/${model.pendingPlanPoa + model.completedPlanPoa}"))
//                labelPendingIPCounts.set(getUpdatedUARLabelCount("${model.pendingPlanPoa}"))
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))
    }

    lateinit var dueDatesList: List<String>
    fun getDueDatesFromDB() {
        addDisposable(dao.fetchAllDueDates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ dueDatesMO ->
                if (dueDatesMO.isNotEmpty()) {
                    this.dueDatesList = dueDatesMO
                    getSitesWithImprovementPlans()
                }
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))
    }

    fun getSitesWithImprovementPlans() {
        setIsLoading(true)
        addDisposable(improvementPoaDao.fetchSitesWithIPList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ modelList ->
                setIsLoading(false)
                onSuccessFetchDetails(modelList)
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))
    }

    private fun onSuccessFetchDetails(detailList: List<SitesWithImprovePlansMO>) {
        /*if (::dueDatesList.isInitialized) {
            val finalList = ArrayList<Any>()
            for (date in dueDatesList) {
                finalList.add(date)
                for (detailModel in detailList) {
                    if (date == detailModel.targetCompletionDate) {
                        finalList.add(detailModel)
                    }
                }
            }
            //Finally Checking if List contains any data or not
            if (finalList.size > 1) {
                isAtRiskDataAvailable.set(true)
                sitesWithPoaAdapter.updateUARData(finalList)
            } else
                isAtRiskDataAvailable.set(false)
        }*/

        Timber.e("POA List Details ${detailList.size}")
        isAtRiskDataAvailable.set(true)
        sitesWithPoaAdapter.updateSitesWithPoaData(detailList)
    }

    private fun onError(throwable: Throwable) {
        setIsLoading(false)
        Timber.e(throwable)
        showToast("Error while fetching details")
    }

    fun getImprovementPlans() {
        isLoading.set(View.VISIBLE)
        addDisposable(Single.zip(coreApi.aiImprovementPlans,
            improvementPoaDao.getCurrentMonthLocalIPPoa(),
            BiFunction<ImprovementPoaResponseMO, List<ImprovementPoaEntity>, ArrayList<ImprovementPoaEntity>> { serverPoaResponse, localIPPoaList ->
                return@BiFunction onResultFetch(serverPoaResponse, localIPPoaList)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.set(View.GONE)
                if (!it.isNullOrEmpty()) {
                    Timber.e("Coming to update IP Poas UI")
                    insertAllImprovementPOAs(it)
                }
            }, {
                isLoading.set(View.GONE)
            }))
    }

    private fun onResultFetch(serverResponse: ImprovementPoaResponseMO,
        localResponse: List<ImprovementPoaEntity>): ArrayList<ImprovementPoaEntity> {
        val listOfNewIP = arrayListOf<ImprovementPoaEntity>()
        if (!serverResponse.improvementPlanData.isNullOrEmpty()) {
            var isUnSyncedLocalIPFound = false
            for (serverIP: ImprovementPoaEntity in serverResponse.improvementPlanData) {
                val index = localResponse.indexOfFirst { localIP ->
                    localIP.serverId == serverIP.serverId
                }
                //                Timber.e("Coming to iterate IP POAs Index : $index")
                if (index > -1) {
                    if (localResponse[index].status > serverIP.status) {
                        //                        Timber.e("IP Record exists in the local DB : but need to unSynced need to upload")
                        isUnSyncedLocalIPFound = true
                    }
                } else {
                    listOfNewIP.add(serverIP)
                }
            }

            if (isUnSyncedLocalIPFound)
                oneTimeWorkerWithNetwork(ClosedImprovePlanWorker::class.java)
        }
        return listOfNewIP
    }

    private fun insertAllImprovementPOAs(planList: List<ImprovementPoaEntity>) {
        addDisposable(improvementPoaDao.insertAll(planList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getHeaderUnitsAndPOAsCount()
                getSitesWithImprovementPlans()
            }, { }))
    }
}