package com.sisindia.ai.android.workers

import android.content.Context
import androidx.work.WorkerParameters
import com.droidcommons.dagger.worker.AndroidWorkerInjection
import com.sisindia.ai.android.base.BaseWorker
import com.sisindia.ai.android.room.dao.EmployeeFineRewardDao
import com.sisindia.ai.android.room.dao.EmployeeSiteDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 3/10/2021.
 */
class EmployeesRewardFineWorker @Inject constructor(context: Context, workerParameters: WorkerParameters) :
    BaseWorker(context, workerParameters) {

    @Inject
    lateinit var rewardFineDao: EmployeeFineRewardDao

    @Inject
    lateinit var employeeDao: EmployeeSiteDao

    init {
        AndroidWorkerInjection.inject(this)
    }

    override fun doWork(): Result {
        addDisposable(coreApi.employeeMasterData
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                if (response.statusCode == 200 && response.data != null) {
                    if (!response?.data.employeeData.isNullOrEmpty()) {
                        addDisposable(employeeDao.insertAll(response?.data.employeeData)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Timber.i("List of EmployeeData inserted")
                            }, { t: Throwable? -> Timber.e(t) }))
                    }
                    if (!response?.data.fineRewardData.isNullOrEmpty()) {
                        addDisposable(rewardFineDao.insertAllFineRewards(response.data.fineRewardData)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Timber.i("List of FINE REWARD inserted")
                            }, { t: Throwable? -> Timber.e(t) }))
                    }
                }
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
        return Result.success()
    }
}