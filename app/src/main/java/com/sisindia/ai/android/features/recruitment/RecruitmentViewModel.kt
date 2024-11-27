package com.sisindia.ai.android.features.recruitment

import android.app.Application
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.ObservableField
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.features.recruitment.adapter.RecommendedRecruitAdapter
import com.sisindia.ai.android.features.recruitment.adapter.RecruitmentGraphAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 5/6/2020.
 */
class RecruitmentViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    var obsRecommended = ObservableField("00")
    var obsSelected = ObservableField("00")
    var obsRejected = ObservableField("00")
    //    var obsInProgress = ObservableField("00")
    val graphAdapter = RecruitmentGraphAdapter()
    val addedRecruitmentAdapter = RecommendedRecruitAdapter()

    fun getRecruitmentDetailsFromAPI() {
        isLoading.set(VISIBLE)
        addDisposable(coreApi.recruitmentDetails
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.set(GONE)
                it?.apply {
                    data?.apply {
                        if (recruitGraphList != null && recruitGraphList.isNotEmpty())
                            graphAdapter.clearAndSetItems(recruitGraphList)
                        if (currentYearCountList != null && currentYearCountList.isNotEmpty()) {
                            currentYearCountList[0].apply {
                                obsRecommended.set(yearRecommendedCount.toString())
                                obsSelected.set(yearSelectedCount.toString())
                                obsRejected.set(yearRejectedCount.toString())
                            }
                        }
                        if (recruitedList != null && recruitedList.isNotEmpty())
                            addedRecruitmentAdapter.clearAndSetItems(recruitedList)
                    }
                }
            }, { throwable: Throwable? ->
                isLoading.set(GONE)
                throwable!!.printStackTrace()
            }))
    }

    fun ivRotaDrawerClick(view: View) {
        message.what = NavigationConstants.OPEN_DASH_BOARD_DRAWER
        liveData.postValue(message)
    }

    fun onAddRecruitment(view: View) {
        message.what = NavigationConstants.OPEN_ADD_RECRUITMENT_SHEET
        liveData.postValue(message)
    }
}