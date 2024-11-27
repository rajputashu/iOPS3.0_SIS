package com.sisindia.ai.android.features.performance.frags

import android.app.Application
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.features.performance.SortItemSelectionListener
import com.sisindia.ai.android.models.performance.PerformanceResultsMO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ashu Rajput on 5/8/2020.
 */
class PerformanceResultsViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    @Named("PerformanceSortItems")
    lateinit var sortItems: ArrayList<String>

    //    val resultsAdapter = PerformanceResultsAdapter()

    var results = ObservableField(PerformanceResultsMO())
    var incentiveTitle = ObservableField("")
    var incentiveValue = ObservableField("")
    val complianceProgress = ObservableInt(0)

    var sortItemSelectionListener = SortItemSelectionListener { sortKey -> getPerformanceResultsFromAPI(sortKey) }

    /*val resultListener = object : PerformanceListener {

        override fun onResultsItemSelected(type: Int) {
            when (type) {
                0 -> message.what = NavigationConstants.OPEN_ROTA_COMPLIANCE_CARDS_ACTIVITY
                1 -> message.what = NavigationConstants.OPEN_LOAD_FACTOR_ACTIVITY
                2 -> {
                }
            }
            liveData.postValue(message)
        }

        override fun onEffortsItemSelected() {
        }
    }*/

    private fun getPerformanceResultsFromAPI(sortKey: Int) {
        setIsLoading(true)
        addDisposable(coreApi.getPerformanceResultDetails(sortKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setIsLoading(false)
                results.set(it.data)
                complianceProgress.set(it.data.rotaCompliance!!.toInt())

                incentiveTitle.set("Incentive :" +
                        if (it.data.incentiveStatus == "NO")
                            " No"
                        else
                            " Yes")

                incentiveValue.set(if (it.data.incentiveStatus == "NO")
                    "Reason : " + it.data.reason
                else
                    "Amount : " + it.data.amount)

            }, { throwable: Throwable? ->
                setIsLoading(false)
                throwable!!.printStackTrace()
            }))
    }

}