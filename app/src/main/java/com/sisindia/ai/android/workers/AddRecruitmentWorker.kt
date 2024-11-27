package com.sisindia.ai.android.workers

import android.content.Context
import androidx.work.WorkerParameters
import com.droidcommons.dagger.worker.AndroidWorkerInjection
import com.sisindia.ai.android.base.BaseWorker
import com.sisindia.ai.android.models.recruit.RecruitmentApiBodyMO
import com.sisindia.ai.android.room.dao.RecruitmentDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 5/17/2020.
 */
class AddRecruitmentWorker @Inject constructor(context: Context,
    workerParameters: WorkerParameters) :
    BaseWorker(context, workerParameters) {

    init {
        AndroidWorkerInjection.inject(this)
    }

    @Inject
    lateinit var recruitmentDao: RecruitmentDao

    override fun doWork(): Result {
        addDisposable(recruitmentDao.fetchUnSyncedRecruitmentDetails()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ recruitmentEntity ->
                updateToServer(recruitmentEntity)
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
        return Result.success()
    }

    private fun updateToServer(recruitmentList: List<RecruitmentApiBodyMO>) {
        for (recruitmentEntity: RecruitmentApiBodyMO in recruitmentList) {
            addDisposable(coreApi.addRecruitment(recruitmentEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ updateSyncStatusOfAI(it.data!!.receivedUUID!!) }, this::onApiError))
        }
    }

    private fun updateSyncStatusOfAI(recruitGuid: String) {
        if (recruitGuid.isNotEmpty()) {
            addDisposable(recruitmentDao.updateSyncStatus(recruitGuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ rowId ->
                    Timber.e("recruitment Sync Id updated... - $rowId")
                }, { throwable: Throwable? ->
                    throwable!!.printStackTrace()
                }))
        }
    }
}