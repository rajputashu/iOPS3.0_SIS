package com.sisindia.ai.android.features.akr

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.features.akr.adapters.AKRAdapter
import com.sisindia.ai.android.models.kits.KitDistributionResponseMO
import com.sisindia.ai.android.room.dao.GuardKitRequestDao
import com.sisindia.ai.android.room.dao.KitItemDao
import com.sisindia.ai.android.room.entities.KitDistributionListEntity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/16/2020.
 */
class AKRViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var guardKitRequestDao: GuardKitRequestDao

    @Inject
    lateinit var kitItemsDao: KitItemDao

    val dueAKRCount = ObservableField("0")
    val addedAKRCount = ObservableField("0")
    val pendingAKRCount = ObservableField("0")
    val distributedAKRCount = ObservableField("0")
    private var isSyncing = false
    val adapter = AKRAdapter()
    val akrListener = object : AKRListener {
        override fun onAkrSelected() {
            message.what = NavigationConstants.OPEN_AKR_SELECTED
            liveData.postValue(message)
        }

        override fun onAssignedAkrForGuardSelected() {
            Timber.e("Main AKR Screen ... Guard Kit is selected...")
        }
    }

    fun initOrUpdateAKRScreenUI() {
        getKitCountsFromDB()
        getKitDetailsBySiteFromDB()
    }

    private fun getKitCountsFromDB() {
        addDisposable(guardKitRequestDao.fetchPendingDistributedKitCounts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ kitCountsMO ->
                if (kitCountsMO != null) {
                    dueAKRCount.set(kitCountsMO.DueSinceLastMonth.toString())
                    addedAKRCount.set(kitCountsMO.AddedThisMonth.toString())
                    pendingAKRCount.set(kitCountsMO.PendingForDistribution.toString())
                    distributedAKRCount.set(kitCountsMO.DistributedThisMonth.toString())
                }
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))
    }

    private fun getKitDetailsBySiteFromDB() {
        addDisposable(guardKitRequestDao.fetchAllKitDetails()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ kitList ->
                if (kitList.isNotEmpty())
                    adapter.clearAndSetItems(kitList)
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))
    }

    fun ivRotaDrawerClick(view: View) {
        when (view.id) {
            R.id.akrMenuButton -> {
                message.what = NavigationConstants.OPEN_DASH_BOARD_DRAWER
                liveData.postValue(message)
            }
            R.id.akrRefreshIcon -> getKitDistributionDetailsFromAPI()
        }
    }

    private fun onError(throwable: Throwable) {
        Timber.e(throwable)
    }

    private fun getKitDistributionDetailsFromAPI() {
        if (isSyncing) {
            showToast("Syncing in progress, please wait!!!")
            return
        }
        isSyncing = true
        isLoading.set(View.VISIBLE)

        addDisposable(Single.zip(kitItemsDao.fetchAllKitListIds(), coreApi.kitDistributionList,
            BiFunction<List<Int>, KitDistributionResponseMO, Boolean> { localAkrList, akrResponse ->
                return@BiFunction onResultFetch(localAkrList, akrResponse)
            })
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.set(View.GONE)
                isSyncing = false
                if (it) {
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(500)
                        withContext(Dispatchers.Main) {
                            initOrUpdateAKRScreenUI()
                        }
                    }
                }
            }, {
                isSyncing = false
                isLoading.set(View.GONE)
                showToast("Please check your internet connection!")
                Timber.e(it)
            }))
    }

    private fun onResultFetch(akrIds: List<Int>, akrResponse: KitDistributionResponseMO): Boolean {
        var isAkrKitInserted = false
        if (akrResponse.statusCode == 200 && !akrResponse.kitDistributionList.isNullOrEmpty()) {
            for (task in akrResponse.kitDistributionList!!) {
                val index = akrIds.indexOfFirst { localId -> localId == task.id }
                Timber.e("AKR Result Local Index $index")
                if (index == -1) {
                    insertKitDistributionListAndKits(task)
                    isAkrKitInserted = true
                } else
                    Timber.e("AKR Kit : Task Already exists")
            }
        }
        return isAkrKitInserted
    }

    /*private fun insertKitDistributionListAndKits(kitList: List<KitDistributionListEntity>) {

        addDisposable(kitItemsDao.insertAllKitDistributionList(kitList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))

        for (kitDistributionList: KitDistributionListEntity in kitList) {
            if (!kitDistributionList.kitDistributionItems.isNullOrEmpty()) {
                kitDistributionList.kitDistributionItems.apply {
                    addDisposable(kitItemsDao.insertAllKitDistributionItems(this)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                        }, { throwable: Throwable? ->
                            onError(throwable!!)
                        }))
                }
            }
        }

        //Calling below methods again to refresh UI with fresh data
        CoroutineScope(Dispatchers.IO).launch {
            delay(500)
            withContext(Dispatchers.Main) {
                initOrUpdateAKRScreenUI()
            }
        }
    }*/

    private fun insertKitDistributionListAndKits(kitList: KitDistributionListEntity) {

        addDisposable(kitItemsDao.insertAkrKit(kitList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))

        if (!kitList.kitDistributionItems.isNullOrEmpty()) {
            kitList.kitDistributionItems.apply {
                addDisposable(kitItemsDao.insertAllKitDistributionItems(this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                    }, { throwable: Throwable? ->
                        onError(throwable!!)
                    }))
            }
        }
    }
}