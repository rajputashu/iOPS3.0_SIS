package com.sisindia.ai.android.features.loadfactor

import android.app.Application
import com.sisindia.ai.android.base.IopsBaseViewModel
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 3/11/2020.
 */
class LoadFactorViewModel @Inject constructor(var appln: Application) :
    IopsBaseViewModel(appln) {

}