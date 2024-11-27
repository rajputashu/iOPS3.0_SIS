package com.sisindia.ai.android.features.performance

import android.app.Application
import android.view.View
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 5/7/2020.
 */
class PerformanceViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {


    fun ivRotaDrawerClick(view: View) {
        message.what = NavigationConstants.OPEN_DASH_BOARD_DRAWER
        liveData.postValue(message)
    }




}