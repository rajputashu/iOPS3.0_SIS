package com.sisindia.ai.android.features.performance.frags

import android.app.Application
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.ObservableBoolean
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.features.performance.MonthYearListener
import com.sisindia.ai.android.features.performance.SortItemSelectionListener
import com.sisindia.ai.android.features.performance.adapters.IncentiveAdapter
import com.sisindia.ai.android.features.performance.adapters.SiteTaskSummaryAdapter
import com.sisindia.ai.android.models.performance.IncentiveDataMO
import com.sisindia.ai.android.models.performance.IncentiveRowData
import com.sisindia.ai.android.utils.IopsUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 11/17/2020.
 */
class SiteTaskSummaryViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    val sortItems = arrayListOf("This Month", "Last Month")
    val obsIsDataAvailable = ObservableBoolean(false)
    val obsIsIncentiveDataAvailable = ObservableBoolean(false)
    val adapter = SiteTaskSummaryAdapter()
    val incentiveAdapter = IncentiveAdapter()
    val obsIsIncentiveCall = ObservableBoolean(false)

    val monthYearListener = object : MonthYearListener {
        override fun onMonthYearSelected(month: Int, year: Int) {
            if (obsIsIncentiveCall.get())
                getIncentiveData(month, year)
        }
    }

    var sortItemSelectionListener = SortItemSelectionListener { sortKey ->
        getSiteTaskSummaryData(sortKey)
    }

//    var sortIncentiveListener = SortItemSelectionListener { sortKey -> getIncentiveData() }

    private fun getSiteTaskSummaryData(searchKeyId: Int) {
        isLoading.set(VISIBLE)
        //        addDisposable(coreApi.siteTaskSummaries
        addDisposable(coreApi.getSiteTaskSummaries(searchKeyId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.set(GONE)
                if (!it.taskSummaryData.isNullOrEmpty()) {
                    obsIsDataAvailable.set(true)
                    adapter.clearAndSetItems(it.taskSummaryData)
                }
            }, { throwable: Throwable? ->
                isLoading.set(GONE)
                throwable!!.printStackTrace()
            }))
    }

    //    fun getIncentiveData(searchKeyId: Int) {
    private fun getIncentiveData(month: Int, year: Int) {
        isLoading.set(VISIBLE)

        addDisposable(coreApi.getIncentive(month, year)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.set(GONE)
                it.data.incentiveDataList?.apply {
                    if (this.isNotEmpty()) {
                        obsIsIncentiveDataAvailable.set(true)
                        createListData(this[0])
                    } else {
                        obsIsIncentiveDataAvailable.set(false)
                    }
                }
            }, { throwable: Throwable? ->
                isLoading.set(GONE)
                if (IopsUtil.isInternetAvailable(app))
                    showToast("Connection Reset Error/ Server Error")
                else
                    showToast("Internet not available, please try again...")
                throwable!!.printStackTrace()
            }))
    }

    private fun createListData(dataMO: IncentiveDataMO) {

        val dataList = arrayListOf<IncentiveRowData>()
        dataList.add(IncentiveRowData("Eligibility",
            if (dataMO.eligibility) "YES" else "NO", dataMO.eligibility))
        dataList.add(IncentiveRowData("Incentive Amount", dataMO.incentiveAmount.toString(),
            dataMO.eligibility))
        dataList.add(IncentiveRowData("Present Days",
            dataMO.presentDays.toString() + "/" + dataMO.targetDays, dataMO.eligibility))
        dataList.add(IncentiveRowData("Rota Compliance", dataMO.rotaCompliance.toString()
                + "/" + dataMO.rotaComplianceTarget, dataMO.eligibility))
        dataList.add(IncentiveRowData("Business Results", dataMO.businessResult.toString()
                + "/" + dataMO.businessResultTarget, dataMO.eligibility))
        dataList.add(IncentiveRowData("Effective Load", dataMO.effectiveLoad.toString() +
                "/" + dataMO.effectiveLoadTarget, dataMO.eligibility))
        dataList.add(IncentiveRowData("Disbandment", dataMO.disbandment.toString(),
            dataMO.eligibility))
        dataList.add(IncentiveRowData("Hold by BH/HR", if (dataMO.holdByBHOrHR) "YES" else "NO",
            dataMO.eligibility))
        incentiveAdapter.clearAndSetItems(dataList)
    }
}