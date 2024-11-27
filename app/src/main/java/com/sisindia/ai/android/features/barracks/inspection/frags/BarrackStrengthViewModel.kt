package com.sisindia.ai.android.features.barracks.inspection.frags

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.room.dao.BarrackDao
import com.sisindia.ai.android.room.entities.CacheBarrackInspectionEntity
import com.sisindia.ai.android.uimodels.barracks.BIStrengthMO
import com.sisindia.ai.android.utils.GsonUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/20/2020.
 */
class BarrackStrengthViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var barrackDao: BarrackDao

    var capacity = ObservableField<String>("0")
    var currentStrength = ObservableField<String>("")
    var leaveSick = ObservableField<String>("")
    var vacant = ObservableField<String>("0")
    var lastInspectionStrength = ObservableField<String>("4")
    lateinit var cacheBarrackInspectionEntity: CacheBarrackInspectionEntity

    fun fetchCacheBI() {
        addDisposable(barrackDao.fetchCacheData(Prefs.getInt(PrefConstants.CURRENT_TASK))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ cacheBI: CacheBarrackInspectionEntity ->
                cacheBI.apply {
                    cacheBarrackInspectionEntity = this
                    if (cacheBI.strengthJson.isNullOrEmpty())
                        fetchCapacityVacantLastInspectionDetails()
                    else
                        updateBIStrengthUIWithCacheData()
                }
            }, { t: Throwable? ->
                Timber.e("Error while fetching cache data")
            }))
    }

    private fun fetchCapacityVacantLastInspectionDetails() {

    }

    private fun updateBIStrengthUIWithCacheData() {
        if (::cacheBarrackInspectionEntity.isInitialized) {
            cacheBarrackInspectionEntity.strengthJson.apply {
                val model = GsonUtils.toJsonWithoutExopse().fromJson(this, BIStrengthMO::class.java)
                capacity.set(model.capacity)
                vacant.set(model.vacant)
                lastInspectionStrength.set(model.lastInspection)
                currentStrength.set(model.currentStrength)
                leaveSick.set(model.leaveSick)
            }
        }
    }

    fun onStrengthBtnClick(view: View) {
        when {
            currentStrength.get().toString().isEmpty() ->
                showToast("Please enter current strength")
            leaveSick.get().toString().isEmpty() ->
                showToast("Please enter leave/sick")
            else -> updateBIStrength()
        }
    }

    private fun updateBIStrength() {
        val biStrengthMO = BIStrengthMO()
        biStrengthMO.capacity = capacity.get().toString()
        biStrengthMO.vacant = vacant.get().toString()
        biStrengthMO.lastInspection = lastInspectionStrength.get().toString()
        biStrengthMO.currentStrength = currentStrength.get().toString()
        biStrengthMO.leaveSick = leaveSick.get().toString()

        if (::cacheBarrackInspectionEntity.isInitialized) {
            cacheBarrackInspectionEntity.strengthJson = GsonUtils.toJsonWithoutExopse()
                .toJson(biStrengthMO)
            cacheBarrackInspectionEntity.updatedDateTime = LocalDateTime.now().toString()

            addDisposable(barrackDao.updateBICache(cacheBarrackInspectionEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    message.what = NavigationConstants.ON_COMPLETE_BARRACK_INSPECTION_STRENGTH
                    liveData.postValue(message)
                }, { throwable: Throwable? ->
                    this.onError(throwable!!)
                }))
        }
    }

    private fun onError(throwable: Throwable) {
        Timber.e(throwable)
        showToast("Error occurred...")
    }
}