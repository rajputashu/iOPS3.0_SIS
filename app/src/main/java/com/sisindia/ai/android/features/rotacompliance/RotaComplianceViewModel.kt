package com.sisindia.ai.android.features.rotacompliance

import android.app.Application
import android.view.View
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.features.rotacompliance.adapters.RotaComplianceAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 3/5/2020.
 */
class RotaComplianceViewModel @Inject constructor(var appln: Application) :
    IopsBaseViewModel(appln) {

    /*var rotaCompInLeaveTV = ObservableField(appln.getString(R.string.dynamic_string,
        "21Hr", appln.getString(R.string.string_inleave)).toHtmlSpan())
    var rotaCompInBranchTV = ObservableField(appln.getString(R.string.dynamic_string,
        "41Hr", appln.getString(R.string.string_inbranch)).toHtmlSpan())*/

    val complianceAdapter = RotaComplianceAdapter()

    fun getRotaComplianceDetailsFromAPI() {
        isLoading.set(View.VISIBLE)
        addDisposable(coreApi.performanceRotaCompliance
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.set(View.GONE)
                it?.apply {
                    if (data != null && data.isNotEmpty())
                        complianceAdapter.clearAndSetItems(data)
                }
            }, { throwable: Throwable? ->
                isLoading.set(View.GONE)
                throwable!!.printStackTrace()
            }))
    }
}