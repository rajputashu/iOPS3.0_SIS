package com.sisindia.ai.android.features.improvementplans

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.features.uar.UARListener
import com.sisindia.ai.android.room.dao.ImprovementPoaDao
import com.sisindia.ai.android.uimodels.uarpoa.IPPoaPendingCompletedMO
import com.sisindia.ai.android.uimodels.uarpoa.SitesWithImprovePlansMO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 12/21/2020.
 */
class ImprovePoaListViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var dao: ImprovementPoaDao

    //POA COUNT'S CARD
    var poaSiteName = ObservableField<String>()
    var poaPendingCount = ObservableField<String>()
    var poaCompletedCount = ObservableField<String>()
    var poaTotalCount = ObservableField<String>()
    var poaProgressValue = ObservableField<Int>()

    var selectedSiteId = ObservableField(0)
    var labelPendingPOAsWithCount = ObservableField(getPendingPOAsCount("00"))
    var labelCompletedPOAsWithCount = ObservableField(getCompletedPOAsCount("00"))
    var showRecyclerViews = ObservableInt(View.VISIBLE)
    var showNoPendingUI = ObservableInt(View.GONE)
    var isRefreshingPoaList: Boolean = false

    private lateinit var dueDatesListOfPendingPOAs: List<String>
    private lateinit var dueDatesListOfCompletedPOAs: List<String>

    val pendingPOAAdapter = IPPoaDetailsAdapter()
    val completedPOAAdapter = IPPoaDetailsAdapter(false)

    val poaListener = object : UARListener {
        override fun onUnitAtRiskSelected(selectedUAR: Any) {
        }

        override fun onPendingPOASelected(selectedPOA: Any) {
            message.what = NavigationConstants.OPEN_CLOSE_IP_POA_SCREEN
            message.obj = selectedPOA
            liveData.postValue(message)
        }
    }

    private fun getPendingPOAsCount(pendingPOA: String): String {
        return app.resources.getString(R.string.dynamic_string_pending_poa, pendingPOA)
    }

    private fun getCompletedPOAsCount(completedPOA: String): String {
        return app.resources.getString(R.string.dynamic_string_completed_poa, completedPOA)
    }

    private fun updatePoaCountsCardAndList(model: SitesWithImprovePlansMO) {
        poaSiteName.set(model.siteName)
        poaPendingCount.set(model.pendingIP)
        poaCompletedCount.set(model.completedIP)
        poaTotalCount.set(model.totalIP)
        poaProgressValue.set(model.progress)

        if (model.progress == 100 && isRefreshingPoaList) {
            showNoPendingUI.set(View.VISIBLE)
            showRecyclerViews.set(View.GONE)
        } else {
            labelPendingPOAsWithCount.set(getPendingPOAsCount(model.pendingIP!!))
            labelCompletedPOAsWithCount.set(getCompletedPOAsCount(model.completedIP!!))
            /*getPendingPOAsDueDatesFromDB()
            getCompletedPOAsDueDatesFromDB()*/

            getPendingPOADetailsList()
            getCompletedPOADetailsList()
        }
    }

    fun getUpdatedPOACountAfterClose() {
        addDisposable(dao.fetchSingleSitesWithIP(selectedSiteId.get()!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ model ->
                isRefreshingPoaList = true
                updatePoaCountsCardAndList(model)
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))
    }

    /*private fun getPendingPOAsDueDatesFromDB() {
        addDisposable(dao.fetchAllDueDatesOfPOA(selectedSiteId.get()!!, POAStatus.PENDING.status)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ dueDatesMO ->
                if (dueDatesMO.isNotEmpty()) {
                    this.dueDatesListOfPendingPOAs = dueDatesMO
                    getPendingPOADetailsList()
                }
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))
    }

    private fun getCompletedPOAsDueDatesFromDB() {
        addDisposable(dao.fetchAllDueDatesOfPOA(selectedSiteId.get()!!, POAStatus.CLOSED.status)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ dueDatesMO ->
                if (dueDatesMO.isNotEmpty()) {
                    this.dueDatesListOfCompletedPOAs = dueDatesMO
                    getCompletedPOADetailsList()
                }
            }, { }))
    }*/

    private fun getPendingPOADetailsList() {
        addDisposable(dao.fetchPendingPoaAtSite(selectedSiteId.get()!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ modelList ->
                onSuccessFetchPendingDetails(modelList)
            }, {}))
    }

    private fun getCompletedPOADetailsList() {
        addDisposable(dao.fetchCompletedPoaAtSite(selectedSiteId.get()!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ modelList ->
                onSuccessFetchCompletedDetails(modelList)
            }, { }))
    }

    private fun onSuccessFetchPendingDetails(detailList: List<IPPoaPendingCompletedMO>) {
        /*if (::dueDatesListOfPendingPOAs.isInitialized && detailList.isNotEmpty()) {
            val finalList = ArrayList<Any>()
            for (date in dueDatesListOfPendingPOAs) {
                finalList.add(date)
                for (detailModel in detailList) {
                    if (date == detailModel.targetCompletionDate)
                        finalList.add(detailModel)
                }
            }
            //Finally Checking if List contains any data or not
            if (finalList.size > 1)
                pendingPOAAdapter.updatePOAData(finalList) }*/

        if (detailList.isNotEmpty())
            pendingPOAAdapter.updatePOAData(detailList)
        else
            pendingPOAAdapter.updatePOAData(arrayListOf())
    }

    private fun onSuccessFetchCompletedDetails(detailList: List<IPPoaPendingCompletedMO>) {
        /*if (::dueDatesListOfCompletedPOAs.isInitialized && detailList.isNotEmpty()) {
            val finalList = ArrayList<Any>()
            for (date in dueDatesListOfCompletedPOAs) {
                finalList.add(date)
                for (detailModel in detailList) {
                    if (date == detailModel.targetCompletionDate)
                        finalList.add(detailModel)
                }
            }
            //Finally Checking if List contains any data or not
            if (finalList.size > 1)
                completedPOAAdapter.updatePOAData(finalList)
        }*/

        if (detailList.isNotEmpty())
            completedPOAAdapter.updatePOAData(detailList)
    }

    private fun onError(throwable: Throwable) {
        Timber.e(throwable)
        showToast("Error while fetching details")
    }
}