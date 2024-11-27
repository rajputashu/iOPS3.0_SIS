package com.sisindia.ai.android.workers

import android.content.Context
import androidx.work.WorkerParameters
import com.droidcommons.dagger.worker.AndroidWorkerInjection
import com.sisindia.ai.android.base.BaseWorker
import com.sisindia.ai.android.room.dao.ImprovementPoaDao
import com.sisindia.ai.android.room.entities.ClosedImprovementPoaEntity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 12/26/2020.
 */
class ClosedImprovePlanWorker @Inject constructor(context: Context, workerParameters: WorkerParameters) :
    BaseWorker(context, workerParameters) {

    init {
        AndroidWorkerInjection.inject(this)
    }

    @Inject
    lateinit var improvementDao: ImprovementPoaDao

    override fun doWork(): Result {
        addDisposable(improvementDao.fetchAllClosedButNotSynced()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ poaList ->
                if (poaList.isNotEmpty())
                    uploadClosedPOAsToServer(poaList)
            }, { }))
        return Result.success()
    }

    private fun uploadClosedPOAsToServer(poaList: List<ClosedImprovementPoaEntity>) {
        for (entity: ClosedImprovementPoaEntity in poaList) {
            addDisposable(coreApi.updateClosedImprovementPOA(entity.poaId, entity.closingRemarks)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isDone)
                        updateSyncedStatusOfClosedPOA(entity.poaId)
                }, this::onApiError
                ))
        }
    }

    private fun updateSyncedStatusOfClosedPOA(poaId: Int) {
        addDisposable(Single.zip(improvementDao.updateStatusOfIP(poaId), improvementDao.updateSyncedStatus(poaId),
            BiFunction<Int, Int, Boolean> { _, _ ->
                return@BiFunction true
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it) Timber.e("ImprovementPOA id is updated...")
            }, {
                it.printStackTrace()
            }))
    }
}