package com.sisindia.ai.android.features.nudges

import android.app.Application
import android.view.View
import androidx.databinding.ObservableBoolean
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.room.dao.NotificationsDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NudgesViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    val nudgesAdapter = NudgesDashboardAdapter()
    val isNudgesAvailable = ObservableBoolean(false)

    @Inject
    lateinit var notificationDao: NotificationsDao

    val listener = object : NudgesListener {
        override fun onDashboardNudgesSelection(nudgesId: String?) {
            message.what = NavigationConstants.OPEN_DYNAMIC_NUDGE_SCREEN
            message.obj = nudgesId
            liveData.postValue(message)
        }
    }

    fun initNudgesDashboard() {
        addDisposable(notificationDao.fetchAll().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                if (!it.isNullOrEmpty()) {
                    isNudgesAvailable.set(true)
                    nudgesAdapter.clearAndSetItems(it)
                } else {
                    isNudgesAvailable.set(false)
                }
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
    }

    fun onViewClicks(view: View) {
        if (view.id == R.id.headerNudgesDashboard) {
            message.what = NavigationConstants.OPEN_DASH_BOARD_DRAWER
            liveData.postValue(message)
        } else if (view.id == R.id.nudgesFetchButton) {

        }
    }
}