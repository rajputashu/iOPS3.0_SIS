package com.sisindia.ai.android.workers

import android.content.Context
import androidx.work.WorkerParameters
import com.droidcommons.dagger.worker.AndroidWorkerInjection
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.base.BaseWorker
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.room.dao.AIProfileDao
import com.sisindia.ai.android.room.entities.AIProfileEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 5/9/2020.
 */
class AIUserProfileWorker @Inject constructor(context: Context,
    workerParameters: WorkerParameters) :
    BaseWorker(context, workerParameters) {

    init {
        AndroidWorkerInjection.inject(this)
    }

    @Inject
    lateinit var aiProfileDao: AIProfileDao


    override fun doWork(): Result {
        addDisposable(coreApi.fetchAIProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.statusCode == 200)
                    insertAIProfileToDB(it.aiProfileData!!)
            }, this::onApiError))
        return Result.success()
    }

    private fun insertAIProfileToDB(aiProfileEntity: AIProfileEntity) {
        Prefs.putInt(PrefConstants.AREA_INSPECTOR_ID, aiProfileEntity.id)
        Prefs.putString(PrefConstants.AREA_INSPECTOR_NAME, aiProfileEntity.employeeName)
        Prefs.putInt(PrefConstants.COMPANY_ID, aiProfileEntity.companyId)
        addDisposable(aiProfileDao.insertMasterAIDetails(aiProfileEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ rowId ->
                Timber.e("AI data inserted successfully - $rowId")
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
    }
}