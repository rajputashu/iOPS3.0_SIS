package com.sisindia.ai.android.workers

import android.content.Context
import androidx.work.WorkerParameters
import com.droidcommons.dagger.worker.AndroidWorkerInjection
import com.sisindia.ai.android.base.BaseWorker
import com.sisindia.ai.android.models.poa.ClosePoaApiBodyMO
import com.sisindia.ai.android.room.dao.SiteRiskPoaDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/26/2020.
 */
class AtRiskPoaWorker @Inject constructor(context: Context, workerParameters: WorkerParameters) :
    BaseWorker(context, workerParameters) {

    init {
        AndroidWorkerInjection.inject(this)
    }

    @Inject
    lateinit var siteRiskPoaDao: SiteRiskPoaDao

    override fun doWork(): Result {
        addDisposable(siteRiskPoaDao.fetchAllCompletedButNotSynced()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ poaList ->
                if (poaList.isNotEmpty())
                    uploadClosedPOAsToServer(poaList)
            }, { }))
        return Result.success()
    }

    private fun uploadClosedPOAsToServer(poaList: List<ClosePoaApiBodyMO>) {
        for (poaEntity: ClosePoaApiBodyMO in poaList) {
            addDisposable(coreApi.updateClosedPOA(poaEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isDone)
                        updateSyncedStatusOfClosedPOA(poaEntity.id!!)
                }, this::onApiError
                ))
        }
    }

    private fun updateSyncedStatusOfClosedPOA(poaId: Int) {
        addDisposable(siteRiskPoaDao.updateSyncedStatus(poaId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ rowId ->
                Timber.e("Updated Row ID POA - $rowId")
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
    }
}