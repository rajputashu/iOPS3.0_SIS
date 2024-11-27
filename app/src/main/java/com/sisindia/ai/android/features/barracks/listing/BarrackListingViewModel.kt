package com.sisindia.ai.android.features.barracks.listing

import android.app.Application
import android.view.View
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.room.dao.BarrackDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/18/2020.
 */
class BarrackListingViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var barrackDao: BarrackDao

    val barrackAdapter = BarrackListingAdapter()

    val barrackListener = object : BarrackListener {
        override fun onBarrackSelected(selectedPosition: Int) {
            Prefs.putInt(PrefConstants.CURRENT_BARRACK_ID,
                barrackAdapter.getItem(selectedPosition).id!!)
            Prefs.putBoolean(PrefConstants.IS_CREATING_BARRACK_TASK, true)
            message.what = NavigationConstants.OPEN_TASK_CREATE_ACTIVITY
//            message.obj = barrackAdapter.getItem(selectedPosition)
            liveData.postValue(message)
        }

        override fun onTagQRSelected(barrackId: Int) {
            message.what = NavigationConstants.OPEN_BARRACK_QR_TAGGING
            message.arg1 = barrackId
            liveData.postValue(message)
        }
    }

    fun getBarrackListing() {
        isLoading.set(View.VISIBLE)
        addDisposable(barrackDao.fetchBarrackListing()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ barrackList ->
                isLoading.set(View.GONE)
                if (!barrackList.isNullOrEmpty()) {
                    barrackAdapter.clearAndSetItems(barrackList)
                }
            }, { throwable: Throwable? ->
                isLoading.set(View.GONE)
                Timber.e(throwable)
                showToast("Error while fetching details")
            }))
    }

    fun ivRotaDrawerClick(view: View) {
        message.what = NavigationConstants.OPEN_DASH_BOARD_DRAWER
        liveData.postValue(message)
    }
}