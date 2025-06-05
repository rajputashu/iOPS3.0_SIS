package com.sisindia.ai.android.workers

import android.content.Context
import androidx.work.WorkerParameters
import com.droidcommons.dagger.worker.AndroidWorkerInjection
import com.sisindia.ai.android.base.BaseWorker
import com.sisindia.ai.android.room.dao.StateDistrictDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 5/06/2025.
 */
class StateDistrictWorker @Inject constructor(context: Context, workerParameters: WorkerParameters) :
    BaseWorker(context, workerParameters) {

    @Inject
    lateinit var stateDistrictDao: StateDistrictDao

    init {
        AndroidWorkerInjection.inject(this)
    }

    override fun doWork(): Result {
        addDisposable(coreApi.stateDistrict
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                if (response.statusCode == 200 && response.data != null) {
                    if (!response?.data.stateList.isNullOrEmpty()) {
                        addDisposable(stateDistrictDao.insertAll(response?.data.stateList)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Timber.i("List of States are inserted")
                            }, { t: Throwable? -> Timber.e(t) }))
                    }
                    if (!response?.data.districtList.isNullOrEmpty()) {
                        addDisposable(stateDistrictDao.insertAllDistricts(response.data.districtList)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Timber.i("List of Districts inserted")
                            }, { t: Throwable? -> Timber.e(t) }))
                    }
                }
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
        return Result.success()
    }
}