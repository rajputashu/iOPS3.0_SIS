package com.sisindia.ai.android.workers

import android.content.Context
import androidx.work.WorkerParameters
import com.droidcommons.dagger.worker.AndroidWorkerInjection
import com.sisindia.ai.android.base.BaseWorker
import com.sisindia.ai.android.models.BillCollectionApiBodyMO
import com.sisindia.ai.android.room.dao.BillCollectionDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 5/22/2020.
 */
class UpdateBillCollectionWorker @Inject constructor(context: Context,
    workerParameters: WorkerParameters) :
    BaseWorker(context, workerParameters) {

    init {
        AndroidWorkerInjection.inject(this)
    }

    @Inject
    lateinit var billCollectionDao: BillCollectionDao

    override fun doWork(): Result {

        addDisposable(billCollectionDao.fetchUnSyncedDetails()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ collectionList ->
                updateToServer(collectionList)
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
        return Result.success()
    }

    private fun updateToServer(billCollectionEntityList: List<BillCollectionApiBodyMO>) {
        for (collectionEntity: BillCollectionApiBodyMO in billCollectionEntityList) {
            addDisposable(coreApi.updateBillCollection(collectionEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isDone)
                        updateSyncStatusOfBillCollection(collectionEntity.billId)
                }, this::onApiError))
        }
    }

    private fun updateSyncStatusOfBillCollection(id: Int) {
        addDisposable(billCollectionDao.updateSyncStatus(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ rowId ->
                Timber.e("Updated Bill Collection... - $rowId")
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
    }
}