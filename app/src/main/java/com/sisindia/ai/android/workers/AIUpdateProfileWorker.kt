package com.sisindia.ai.android.workers

import android.content.Context
import androidx.work.WorkerParameters
import com.droidcommons.dagger.worker.AndroidWorkerInjection
import com.sisindia.ai.android.base.BaseWorker
import com.sisindia.ai.android.models.profile.AIProfileUpdateBodyMO
import com.sisindia.ai.android.room.dao.AIProfileDao
import com.sisindia.ai.android.room.entities.CacheAIProfileEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 5/9/2020.
 */
class AIUpdateProfileWorker @Inject constructor(context: Context,
    workerParameters: WorkerParameters) :
    BaseWorker(context, workerParameters) {

    init {
        AndroidWorkerInjection.inject(this)
    }

    @Inject
    lateinit var aiProfileDao: AIProfileDao


    override fun doWork(): Result {
        addDisposable(aiProfileDao.fetchUnSyncedAIDetails()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ aiProfileEntity ->
                updateToServer(aiProfileEntity)
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
        return Result.success()
    }

    private fun updateToServer(aiProfileEntity: CacheAIProfileEntity) {
        val profileBody = AIProfileUpdateBodyMO()
        profileBody.aiId = aiProfileEntity.aiId
        profileBody.altAddress = aiProfileEntity.altAddress
        profileBody.altContactNo = aiProfileEntity.altContactNo
        profileBody.pinCode = aiProfileEntity.pinCode
        profileBody.createdDateTime = aiProfileEntity.createdDateTime
        profileBody.updatedDateTime = aiProfileEntity.updatedDateTime
        /*val geoPointItem = GeoPointItem()
        geoPointItem.latitude = aiProfileEntity.latitude
        geoPointItem.longitude = aiProfileEntity.longitude
        profileBody.geoPoint = geoPointItem*/
        profileBody.geoPoint = "" + aiProfileEntity.latitude + "," + aiProfileEntity.longitude

        addDisposable(coreApi.updateAIProfile(profileBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isDone)
                    updateSyncStatusOfAI()
            }, this::onApiError))
    }

    private fun updateSyncStatusOfAI() {
        addDisposable(aiProfileDao.updateSyncStatus()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ rowId ->
                Timber.e("Updated AI Profile... - $rowId")
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
    }
}