package com.sisindia.ai.android.features.uar.poa

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.commons.POAStatus
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.features.uar.UARListener
import com.sisindia.ai.android.room.customdao.UarAndPoaDao
import com.sisindia.ai.android.uimodels.uarpoa.AtRiskAndPoaDetailsMO
import com.sisindia.ai.android.uimodels.uarpoa.POADetailsMO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/3/2020.
 */
class POAViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var dao: UarAndPoaDao

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

    val pendingPOAAdapter = POAAdapter()
    val completedPOAAdapter = POAAdapter()

    val poaListener = object : UARListener {
        override fun onUnitAtRiskSelected(selectedUAR: Any) {
        }

        override fun onPendingPOASelected(selectedPOA: Any) {
            message.what = NavigationConstants.OPEN_CLOSE_POA_SCREEN
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

    fun updatePoaCountsCardAndList(model: AtRiskAndPoaDetailsMO) {

        poaSiteName.set(model.SiteName)
        poaPendingCount.set(model.Pending)
        poaCompletedCount.set(model.Complete)
        poaTotalCount.set(model.TotalPOAs)
        poaProgressValue.set(model.Progress)

        if (model.Progress == 100 && isRefreshingPoaList) {
            showNoPendingUI.set(View.VISIBLE)
            showRecyclerViews.set(View.GONE)
        } else {
            labelPendingPOAsWithCount.set(getPendingPOAsCount(model.Pending!!))
            labelCompletedPOAsWithCount.set(getCompletedPOAsCount(model.Complete!!))
            getPendingPOAsDueDatesFromDB()
            getCompletedPOAsDueDatesFromDB()
        }
    }

    fun getUpdatedPOACountAfterClose() {
        addDisposable(dao.fetchUARDetailsFromSiteId(selectedSiteId.get()!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ dueDatesMO ->
                isRefreshingPoaList = true
                updatePoaCountsCardAndList(dueDatesMO)
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))
    }

    private fun getPendingPOAsDueDatesFromDB() {
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
    }

    private fun getPendingPOADetailsList() {
        addDisposable(dao.fetchAllPOAs(selectedSiteId.get()!!, POAStatus.PENDING.status)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ modelList ->
                onSuccessFetchPendingDetails(modelList)
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))
    }

    private fun getCompletedPOADetailsList() {
        addDisposable(dao.fetchAllPOAs(selectedSiteId.get()!!, POAStatus.CLOSED.status)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ modelList ->
                onSuccessFetchCompletedDetails(modelList)
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))
    }

    private fun onSuccessFetchPendingDetails(detailList: List<POADetailsMO>) {
        if (::dueDatesListOfPendingPOAs.isInitialized && detailList.isNotEmpty()) {
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
                pendingPOAAdapter.updatePOAData(finalList)
        }
    }

    private fun onSuccessFetchCompletedDetails(detailList: List<POADetailsMO>) {
        if (::dueDatesListOfCompletedPOAs.isInitialized && detailList.isNotEmpty()) {
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
        }
    }

    private fun onError(throwable: Throwable) {
        Timber.e(throwable)
        showToast("Error while fetching details")
    }
}