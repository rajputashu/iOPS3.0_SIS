package com.sisindia.ai.android.features.barracks.inspection.frags

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.features.billsubmit.RadioCheckedListener
import com.sisindia.ai.android.room.dao.BarrackDao
import com.sisindia.ai.android.room.entities.CacheBarrackInspectionEntity
import com.sisindia.ai.android.uimodels.barracks.BISpaceMO
import com.sisindia.ai.android.utils.GsonUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/21/2020.
 */
class BarrackSpaceViewModel @Inject constructor(val app: Application) :
    IopsBaseViewModel(app) {

    @Inject
    lateinit var barrackDao: BarrackDao

    var remarks = ObservableField("")
    var barrackSpaceRGChecked = ObservableField(R.id.rbCrampt)
    var waterSupplyRGChecked = ObservableField(R.id.rb24x7)
    var messingRGChecked = ObservableField(R.id.rbJoint)
    var messBoyRGChecked = ObservableField(R.id.rbYes)
    var utensilsRGChecked = ObservableField(R.id.rbNotAdequate)
    var recreationFacilityRGChecked = ObservableField(R.id.rbYesRecreation)
    val barrackSpaceSelected = ObservableField("")
    val waterSupplySelected = ObservableField("")
    val messingSelected = ObservableField("")
    val messBoySelected = ObservableField("")
    val utensilsSelected = ObservableField("")
    val recreationSelected = ObservableField("")
    lateinit var cacheBarrackInspectionEntity: CacheBarrackInspectionEntity

    val barrackSpaceRGListener = object : RadioCheckedListener {
        override fun onRadioButtonChecked(value: String) {
            barrackSpaceSelected.set(value)
        }
    }

    val waterSupplyRGListener = object : RadioCheckedListener {
        override fun onRadioButtonChecked(value: String) {
            waterSupplySelected.set(value)
        }
    }

    val messingRGListener = object : RadioCheckedListener {
        override fun onRadioButtonChecked(value: String) {
            messingSelected.set(value)
        }
    }

    val messBoyRGListener = object : RadioCheckedListener {
        override fun onRadioButtonChecked(value: String) {
            messBoySelected.set(value)
        }
    }

    val utensilsRGListener = object : RadioCheckedListener {
        override fun onRadioButtonChecked(value: String) {
            utensilsSelected.set(value)
        }
    }

    val recreationRGListener = object : RadioCheckedListener {
        override fun onRadioButtonChecked(value: String) {
            recreationSelected.set(value)
        }
    }

    fun fetchCacheBI() {
        addDisposable(barrackDao.fetchCacheData(Prefs.getInt(PrefConstants.CURRENT_TASK))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ cacheBI: CacheBarrackInspectionEntity ->
                cacheBI.apply {
                    cacheBarrackInspectionEntity = this
                    if (!cacheBI.spaceJson.isNullOrEmpty())
                        updateBISpaceUIWithCacheData()
                }
            }, { t: Throwable? ->
                Timber.e("Error while fetching cache data")
            }))
    }

    private fun updateBISpaceUIWithCacheData() {
        if (::cacheBarrackInspectionEntity.isInitialized) {
            cacheBarrackInspectionEntity.spaceJson.apply {
                val model = GsonUtils.toJsonWithoutExopse().fromJson(this, BISpaceMO::class.java)
                model.apply {
                    if (!barrackSpace.equals("Crampt", ignoreCase = true))
                        barrackSpaceRGChecked.set(R.id.rbReasonable)
                    if (!waterSupply.equals("24X7"))
                        waterSupplyRGChecked.set(R.id.rbWaterSupplyNotAdequate)
                    if (!messing.equals("Joint", ignoreCase = true))
                        messingRGChecked.set(R.id.rbSeparate)
                    if (!messBoyProvided.equals("Yes", ignoreCase = true))
                        messBoyRGChecked.set(R.id.rbNo)
                    if (!utensils.equals("Not Adequate", ignoreCase = true))
                        utensilsRGChecked.set(R.id.rbAdequate)
                    if (!recreationFacility.equals("Yes", ignoreCase = true))
                        recreationFacilityRGChecked.set(R.id.rbNoRecreation)
                    remarks.set(guardPerToiletRemarks)
                }
            }
        }
    }

    fun onDoneButtonClick(view: View) {
        if (remarks.get().toString().isEmpty()) {
            showToast("Please enter your remarks")
        } else
            updateBISpaceJson()
    }

    private fun updateBISpaceJson() {
        val biSpaceMO = BISpaceMO()
        biSpaceMO.barrackSpace = barrackSpaceSelected.get().toString()
        biSpaceMO.waterSupply = waterSupplySelected.get().toString()
        biSpaceMO.messing = messingSelected.get().toString()
        biSpaceMO.messBoyProvided = messBoySelected.get().toString()
        biSpaceMO.utensils = utensilsSelected.get().toString()
        biSpaceMO.recreationFacility = recreationSelected.get().toString()
        biSpaceMO.guardPerToiletRemarks = remarks.get().toString()

        if (::cacheBarrackInspectionEntity.isInitialized) {
            cacheBarrackInspectionEntity.spaceJson = GsonUtils.toJsonWithoutExopse()
                .toJson(biSpaceMO)
            cacheBarrackInspectionEntity.updatedDateTime = LocalDateTime.now().toString()

            addDisposable(barrackDao.updateBICache(cacheBarrackInspectionEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    message.what = NavigationConstants.ON_COMPLETE_BARRACK_INSPECTION_SPACE
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