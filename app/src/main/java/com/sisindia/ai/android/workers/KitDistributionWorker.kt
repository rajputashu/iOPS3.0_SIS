package com.sisindia.ai.android.workers

import android.content.Context
import androidx.work.WorkerParameters
import com.droidcommons.dagger.worker.AndroidWorkerInjection
import com.sisindia.ai.android.base.BaseWorker
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.models.kits.KitDistributionApiBodyMO
import com.sisindia.ai.android.models.kits.KitDistributionItemsBodyMO
import com.sisindia.ai.android.room.dao.KitItemDao
import com.sisindia.ai.android.room.entities.KitDistributionListEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 8/31/2020.
 */
class KitDistributionWorker @Inject constructor(context: Context, parameter: WorkerParameters) :
    BaseWorker(context, parameter) {

    private var isAllItemsDistributed: Boolean = false

    init {
        AndroidWorkerInjection.inject(this)
    }

    @Inject
    lateinit var kitItemsDao: KitItemDao

    override fun doWork(): Result {

        isAllItemsDistributed =
            inputData.getBoolean(IntentConstants.IS_ALL_KIT_ITEMS_DISTRIBUTED, false)

        addDisposable(kitItemsDao.fetchAllKitsForDistribution()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list ->
                fetchKitItemsFromKit(list)
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
        return Result.success()
    }

    private fun fetchKitItemsFromKit(distributionList: List<KitDistributionListEntity>) {
        for (collectionEntity: KitDistributionListEntity in distributionList) {
            addDisposable(kitItemsDao.fetchAllKitItemsForDistribution(collectionEntity.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list ->
                    if (!list.isNullOrEmpty()) {
                        val kitBody = KitDistributionApiBodyMO()
                        collectionEntity.apply {
                            kitBody.id = id
                            kitBody.issuingOfficerId = issuingOfficerId
                            kitBody.siteId = siteId
                            kitBody.distributionStatus = distributionStatus
//                            kitBody.siteTaskId = siteTaskId
                            kitBody.recipientId = recipientId
                            kitBody.branchId = branchId
                            kitBody.kitTypeId = kitTypeId
                            kitBody.kitRequestId = kitRequestId
                            kitBody.distributionDateTime = distributionDateTime
                            kitBody.recipientName = recipientName
                            kitBody.recipientCode = recipientCode
                            kitBody.createdDateTime = createdDateTime
                            kitBody.createdBy = createdBy
                            kitBody.updatedDateTime = updatedDateTime
                            kitBody.updatedBy = updatedBy
                        }
                        kitBody.kitDistributionItems = list
                        updateToServer(kitBody)
                    }
                }, { throwable: Throwable? ->
                    throwable!!.printStackTrace()
                }))
        }
    }

    private fun updateToServer(kitDistributionBody: KitDistributionApiBodyMO) {
        addDisposable(coreApi.updateKitDistribution(kitDistributionBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.statusCode == 200) {
                    if (!kitDistributionBody.kitDistributionItems.isNullOrEmpty()) {
                        val kitIdList = arrayListOf<Int>()
                        for (itemsMO: KitDistributionItemsBodyMO in kitDistributionBody.kitDistributionItems!!) {
                            kitIdList.add(itemsMO.id)
                        }

                        if (!kitIdList.isNullOrEmpty()) {
                            updateSyncStatusOfKitItems(kitIdList)
                            if (isAllItemsDistributed)
                                updateKitDistributionListStatus(kitDistributionBody.id)
                        }
                    }
                }
            }, this::onApiError))
    }

    private fun updateSyncStatusOfKitItems(listOfItemsId: List<Int>) {
        addDisposable(kitItemsDao.updateSyncStatus(listOfItemsId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ rowId ->
                Timber.d("Updated Kit Items Status - $rowId")
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
    }

    private fun updateKitDistributionListStatus(kitId: Int) {
        addDisposable(kitItemsDao.updateDistributedKitStatus(kitId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("Updated Kit List Status : If all Kit Items are distributed: IN Worker")
            }, { throwable: Throwable? ->
                Timber.e(throwable)
            }))
    }
}