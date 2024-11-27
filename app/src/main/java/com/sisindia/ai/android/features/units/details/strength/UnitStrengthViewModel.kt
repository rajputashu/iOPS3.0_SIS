package com.sisindia.ai.android.features.units.details.strength

import android.app.Application
import android.view.View
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.room.dao.SiteStrengthDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 3/24/2020.
 */
class UnitStrengthViewModel @Inject constructor(app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var siteStrengthDao: SiteStrengthDao

    val strengthAdapter = UnitStrengthAdapter()

    fun fetchUnitStrengthFromDB() {
        isLoading.set(View.VISIBLE)
        addDisposable(siteStrengthDao.fetchStrengthGraphData(Prefs.getInt(PrefConstants.CURRENT_SITE))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ strengthList ->
                isLoading.set(View.GONE)
                if (!strengthList.isNullOrEmpty()) {
                    strengthAdapter.updateStrength(strengthList)
                }
            }, { throwable: Throwable? ->
                isLoading.set(View.GONE)
                Timber.e(throwable)
                showToast("Error while fetching details")
            }))
    }
}