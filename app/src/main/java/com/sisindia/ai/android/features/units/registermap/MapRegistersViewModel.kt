package com.sisindia.ai.android.features.units.registermap

import android.app.Application
import android.view.View
import androidx.databinding.ObservableBoolean
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.models.register.MapRegistersDataMO
import com.sisindia.ai.android.models.register.RegistersInfoMO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 10/21/2020.
 */
class MapRegistersViewModel @Inject constructor(app: Application) : IopsBaseViewModel(app) {

    val alreadyMappedRegisterAdapter = MapRegistersAdapter()
    val newMapRegisterAdapter = MapRegistersAdapter()
    val isMappedRegisterAvailable = ObservableBoolean(false)
    val isNewRegisterAvailable = ObservableBoolean(false)

    fun getRegistersToMapFromServer() {
        isLoading.set(View.VISIBLE)
        //        addDisposable(coreApi.getSiteRegisters(Prefs.getInt(PrefConstants.CURRENT_SITE))
        addDisposable(coreApi.getSiteRegisters(1069)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.set(View.GONE)
                it?.apply {
                    mapRegistersData?.apply {
                        updateRegistersRV(this)
                    }
                }
            }, { throwable: Throwable? ->
                isLoading.set(View.GONE)
                throwable!!.printStackTrace()
            }))
    }

    private fun updateRegistersRV(registersData: MapRegistersDataMO) {
        //        alreadyMappedRegisterAdapter.clearAndSetItems(registersData.siteRegistersList)
        val newMappingList = ArrayList<RegistersInfoMO>()
        newMappingList.addAll(registersData.globalRegistersList!!)
        newMappingList.addAll(registersData.sectorRegistersList!!)
        newMappingList.addAll(registersData.clientRegistersList!!)
        newMappingList.addAll(registersData.siteRegistersList!!)
        if (newMappingList.isNotEmpty()) {
            isNewRegisterAvailable.set(true)
            newMapRegisterAdapter.clearAndSetItems(newMappingList)
        }
    }

    fun onSaveBtnClick(view: View) {
        message.what = NavigationConstants.ON_MAPPING_REGISTERS_SUCCESSFULLY
        liveData.value = message
    }
}