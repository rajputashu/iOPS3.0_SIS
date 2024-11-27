package com.sisindia.ai.android.features.clientcoordination

import android.app.Application
import android.text.Spanned
import android.view.View
import androidx.databinding.ObservableField
import com.droidcommons.base.timer.CountUpTimer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.features.clientcoordination.adapters.CCRPerformanceRatingAdapter
import com.sisindia.ai.android.features.clientcoordination.entity.PerformanceRatingMO
import com.sisindia.ai.android.utils.toHtmlSpan
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 3/16/2020.
 */
class ClientCoordinationViewModel @Inject constructor(val app: Application) :
    IopsBaseViewModel(app) {

    @Inject
    lateinit var timer: CountUpTimer
    val unitName = ObservableField<String>("")
    val contactList = ObservableField<ArrayList<String>>(arrayListOf())
    val performanceAdapter = CCRPerformanceRatingAdapter(getData())
    val satisfactionIndex =
        ObservableField<Spanned>(app.resources.getString(R.string.dynamic_satisfaction_index,
            "10.0%").toHtmlSpan())

    val ccrListener = object : ClientCoordinationListener {
        override fun onContactSelected(contactName: String) {
        }
    }

    fun updateUnitName(name: String) {
        unitName.set(name)
    }

    fun updateContactList(list: ArrayList<String>) {
        contactList.set(list)
    }

    fun onTaskCompleteBtnClick(view: View?) {
        message.what = NavigationConstants.OPEN_DASH_BOARD
        liveData.postValue(message)
    }

    private fun getData(): ArrayList<PerformanceRatingMO> {
        val list = ArrayList<PerformanceRatingMO>()
        list.add(PerformanceRatingMO("1", "Maintenance of authorized strength", 0))
        list.add(PerformanceRatingMO("2", "Proficiency in duty procedures", 0))
        list.add(PerformanceRatingMO("3", "Communications skill of security personnel", 0))
        list.add(PerformanceRatingMO("4", "Trust and confidence on security personnel", 0))
        list.add(PerformanceRatingMO("5", "Quality of supervision on security personnel", 0))
        list.add(PerformanceRatingMO("6", "Performance of unit commander / assignment manager", 0))
        list.add(PerformanceRatingMO("7", "Quality of training conducted", 0))
        list.add(PerformanceRatingMO("8", "Response to you query", 0))
        list.add(PerformanceRatingMO("9", "Timely submission of bill documents", 0))
        list.add(PerformanceRatingMO("10", "Overall ability of fulfil your requirements", 0))
        return list
    }
}