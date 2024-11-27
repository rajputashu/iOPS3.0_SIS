package com.sisindia.ai.android.features.selfservice

import android.app.Application
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.room.dao.AIProfileDao
import com.sisindia.ai.android.room.entities.AIProfileEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 1/7/2021.
 */
class SelfServiceViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    val webViewUrl = ObservableField("")
    val isWebPageLoaded = ObservableBoolean(true)

    @Inject
    lateinit var aiProfileDao: AIProfileDao

    fun ivRotaDrawerClick(view: View) {
        message.what = NavigationConstants.OPEN_DASH_BOARD_DRAWER
        liveData.postValue(message)
    }

    fun getTicketToken() {
        addDisposable(aiProfileDao.getAIProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ entity: AIProfileEntity ->
                val url = entity.url + "?Token=" + entity.token
                //                Timber.e("SelfService URL $url")
                webViewUrl.set(url)
                message.what = NavigationConstants.ON_FETCHING_TICKET_TOKEN
                liveData.postValue(message)
            }, { }))
    }
}