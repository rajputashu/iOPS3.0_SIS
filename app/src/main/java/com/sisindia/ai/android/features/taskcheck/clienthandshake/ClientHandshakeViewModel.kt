package com.sisindia.ai.android.features.taskcheck.clienthandshake

import android.app.Application
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.room.dao.TaskDao
import com.sisindia.ai.android.uimodels.tasks.TimeElapsedMO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/8/2020.
 */
class ClientHandshakeViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var taskDao: TaskDao

    private var tik = TaskTimer(0)

    /*  fun startTimer() {
          val taskId = Prefs.getInt(PrefConstants.CURRENT_TASK)
          addDisposable(taskDao.fetchTimeSpentByTaskId(taskId)
              .subscribeOn(Schedulers.newThread())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe({ timeInMillis: Long? ->
                  tik = TaskTimer(timeInMillis!!)
                  tik.start()
              },
                  { t: Throwable? ->
                      Timber.e(t)
                  }))
      }

      fun stopTimer() {
          val taskId = Prefs.getInt(PrefConstants.CURRENT_TASK)
          addDisposable(taskDao.updateTimeSpentByTaskId(taskId, tik.lastTik)
              .subscribeOn(Schedulers.newThread())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe({ rowId: Int? -> tik.cancel() },
                  { t: Throwable? ->
                      Timber.e(t)
                  }))
      }*/

    fun startTimer() {
        val taskId: Int = Prefs.getInt(PrefConstants.CURRENT_TASK)
        addDisposable(taskDao.fetchTimeSpentViaTaskId(taskId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ model: TimeElapsedMO ->
                tik = TaskTimer(model.timeElapsed - findTimeDifference(model.lastElapsedTime))
                tik.start()
            }, { t: Throwable? -> Timber.e(t) }))
    }

    fun stopTimer() {
        val taskId: Int = Prefs.getInt(PrefConstants.CURRENT_TASK)
        addDisposable(taskDao.updateTimeSpentByTaskId(taskId, tik.lastTik, currentMilliOfTheDay)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ tik.cancel() }, { t: Throwable? -> Timber.e(t) }))
    }
}