package com.sisindia.ai.android.features.performance.frags

import android.app.Application
import androidx.databinding.ObservableField
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.features.performance.SortItemSelectionListener
import com.sisindia.ai.android.models.performance.PerformanceEffortsMO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ashu Rajput on 5/8/2020.
 */
class PerformanceEffortsViewModel @Inject constructor(val app: Application) :
    IopsBaseViewModel(app) {

    @Inject
    @Named("PerformanceSortItems")
    lateinit var sortItems: ArrayList<String>

    //    val effortsAdapter = PerformanceEffortsAdapter()

    var efforts = ObservableField(PerformanceEffortsMO())

    var sortItemSelectionListener = SortItemSelectionListener { sortKey -> getPerformanceEffortsFromAPI(sortKey) }

    private fun getPerformanceEffortsFromAPI(sortKey: Int) {
        setIsLoading(true)
        addDisposable(coreApi.getPerformanceEffortsDetails(sortKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setIsLoading(false)
                efforts.set(it.data)
            }, { throwable: Throwable? ->
                setIsLoading(false)
                throwable!!.printStackTrace()
            }))
    }
}