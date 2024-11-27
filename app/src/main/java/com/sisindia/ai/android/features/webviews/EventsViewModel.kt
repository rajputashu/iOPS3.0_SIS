package com.sisindia.ai.android.features.webviews

import android.app.Application
import android.view.View
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.models.EventsDataMO
import com.sisindia.ai.android.utils.IopsUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 12-10-2022
 */
class EventsViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    private var list = ArrayList<EventsDataMO>()
    val adapter = EventsAdapter()
    val listener = object : EventClickListener {
        override fun onItemSelected(pos: Int) {
            message.what = NavigationConstants.OPEN_WEB_VIEW
            message.obj = list[pos]
            liveData.postValue(message)
        }
    }

    fun onViewClicks(view: View) {
        if (view.id == R.id.headerEvents) {
            message.what = NavigationConstants.OPEN_DASH_BOARD_DRAWER
            liveData.postValue(message)
        }
    }

    fun getEventsFromAPI() {
        isLoading.set(View.VISIBLE)
        addDisposable(coreApi.getEventsList(Prefs.getString(PrefConstants.AREA_INSPECTOR_CODE, ""))
//        addDisposable(coreApi.getEventsList("BEL008593")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.set(View.GONE)
                it?.data?.apply {
                    if (this.isNotEmpty()) {
                        list.addAll(this)
                        adapter.clearAndSetItems(this)
                    } else
                        showToast(app.resources.getString(R.string.dataNA))
                }
            }, {
                isLoading.set(View.GONE)
                if (IopsUtil.isInternetAvailable(app))
                    showToast(app.resources.getString(R.string.string_server_error))
                else
                    showToast(app.resources.getString(R.string.string_no_internet))
            }))
    }
}