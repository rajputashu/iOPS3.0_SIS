package com.sisindia.ai.android.features.akr.details

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.features.akr.AKRListener
import com.sisindia.ai.android.features.akr.adapters.KitAssignedDistributedAdapter
import com.sisindia.ai.android.room.dao.GuardKitRequestDao
import com.sisindia.ai.android.uimodels.akr.AkrUIResult
import com.sisindia.ai.android.uimodels.akr.KitAssignedDistributedMO
import com.sisindia.ai.android.uimodels.akr.KitBySiteDetailsMO
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/16/2020.
 */
class KitAssignedDistributedViewModel @Inject constructor(val app: Application) :
    IopsBaseViewModel(app) {

    @Inject
    lateinit var guardKitRequestDao: GuardKitRequestDao
    var kitUnitName = ObservableField("")
    var kitPending = ObservableField("")
    var kitItems = ObservableField("")
    var kitDistributed = ObservableField("")
    var unPaidKits = ObservableField("")
    var isAssignedDataAvailableForRV = ObservableField(View.VISIBLE)
    var isDistributedDataAvailableForRV = ObservableField(View.VISIBLE)
    var isAssignedDataAvailable = ObservableField(View.GONE)
    var isDistributedDataAvailable = ObservableField(View.GONE)

    val assignedAdapter = KitAssignedDistributedAdapter()
    val distributedAdapter = KitAssignedDistributedAdapter()

    val listener = object : AKRListener {
        override fun onAkrSelected() {
        }

        override fun onAssignedAkrForGuardSelected() {
            message.what = NavigationConstants.OPEN_KIT_ASSIGNED_SELECTED
            liveData.postValue(message)
        }
    }

    fun initAndUpdateKitAssignedUI() {

        /*fetchCardViewDetailsFromDB()
        fetchAssignedKitDetailsFromDB()
        fetchDistributedKitDetailsFromDB()*/

        isLoading.set(View.VISIBLE)

        addDisposable(Single.zip(
            guardKitRequestDao.fetchKitDetailsViaSiteId(Prefs.getInt(PrefConstants.AKR_SITE_ID)),
            guardKitRequestDao.fetchAssignedKit(Prefs.getInt(PrefConstants.AKR_SITE_ID)),
            guardKitRequestDao.fetchDistributedKit(Prefs.getInt(PrefConstants.AKR_SITE_ID)))
        { cardMO, assignedList, distributedList ->
            onResultFetch(cardMO, assignedList, distributedList)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                isLoading.set(View.GONE)


                kitUnitName.set(it.cardMO.siteName)
                kitPending.set(it.cardMO.kitPending)
                kitItems.set(it.cardMO.kitItems)
                kitDistributed.set(it.cardMO.kitDistributed)
                unPaidKits.set(it.cardMO.unPaidKits)

                if (it.pendingList.isNotEmpty()) {
                    assignedAdapter.clearAndSetItems(it.pendingList)
                    isAssignedDataAvailable.set(View.GONE)
                } else {
                    isAssignedDataAvailableForRV.set(View.GONE)
                    isAssignedDataAvailable.set(View.VISIBLE)
                }

                if (it.completedList.isNotEmpty()) {
                    distributedAdapter.clearAndSetItems(it.completedList)
                    isDistributedDataAvailableForRV.set(View.VISIBLE)
                    isDistributedDataAvailable.set(View.GONE)
                } else {
                    isDistributedDataAvailableForRV.set(View.GONE)
                    isDistributedDataAvailable.set(View.VISIBLE)
                }

            }, { throwable ->
                isLoading.set(View.GONE)
                throwable.printStackTrace()
            }))
    }

    fun onResultFetch(cardMO: KitBySiteDetailsMO,
                      pendingList: List<KitAssignedDistributedMO>,
                      completedList: List<KitAssignedDistributedMO>): AkrUIResult {

//        val v = AkrUIResult(cardMO, pendingList, completedList)
        return AkrUIResult(cardMO, pendingList, completedList)
    }

    private fun fetchCardViewDetailsFromDB() {
        addDisposable(guardKitRequestDao.fetchKitDetailsViaSiteId(Prefs.getInt(PrefConstants.AKR_SITE_ID))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ kitSummary ->
                if (kitSummary != null) {
                    kitUnitName.set(kitSummary.siteName)
                    kitPending.set(kitSummary.kitPending)
                    kitItems.set(kitSummary.kitItems)
                    kitDistributed.set(kitSummary.kitDistributed)
                    unPaidKits.set(kitSummary.unPaidKits)
                } else
                    Timber.e("Kit Summary Empty")
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))
    }

    private fun fetchAssignedKitDetailsFromDB() {
        isLoading.set(View.VISIBLE)
        addDisposable(guardKitRequestDao.fetchAssignedKit(Prefs.getInt(PrefConstants.AKR_SITE_ID))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ kitList ->
                isLoading.set(View.GONE)
                if (kitList != null && kitList.isNotEmpty()) {
                    assignedAdapter.clearAndSetItems(kitList)
                    isAssignedDataAvailable.set(View.GONE)
                } else {
                    isAssignedDataAvailableForRV.set(View.GONE)
                    isAssignedDataAvailable.set(View.VISIBLE)
                }
            }, { throwable: Throwable? ->
                isLoading.set(View.GONE)
                this.onError(throwable!!)
            }))
    }

    private fun fetchDistributedKitDetailsFromDB() {
        addDisposable(guardKitRequestDao.fetchDistributedKit(Prefs.getInt(PrefConstants.AKR_SITE_ID))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ kitList ->
                if (kitList != null && kitList.isNotEmpty()) {
                    distributedAdapter.clearAndSetItems(kitList)
                    isDistributedDataAvailableForRV.set(View.VISIBLE)
                    isDistributedDataAvailable.set(View.GONE)
                } else {
                    isDistributedDataAvailableForRV.set(View.GONE)
                    isDistributedDataAvailable.set(View.VISIBLE)
                }
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))
    }

    private fun onError(throwable: Throwable) {
        Timber.e(throwable)
        showToast("Error while fetching details")
    }
}