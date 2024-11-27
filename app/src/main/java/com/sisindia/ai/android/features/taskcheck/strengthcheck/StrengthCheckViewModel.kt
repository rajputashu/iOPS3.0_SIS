package com.sisindia.ai.android.features.taskcheck.strengthcheck

import android.app.Application
import android.view.View
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.room.dao.SiteStrengthDao
import com.sisindia.ai.android.room.dao.TaskDao
import com.sisindia.ai.android.uimodels.tasks.TimeElapsedMO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 3/18/2020.
 */
class StrengthCheckViewModel @Inject constructor(val appln: Application) : IopsBaseViewModel(appln) {

    @Inject
    lateinit var taskDao: TaskDao

    private var tik = TaskTimer(0)

    @Inject
    lateinit var siteStrengthDao: SiteStrengthDao

    val strengthAdapter = StrengthCheckAdapter()

    fun onTaskCompleteBtnClick(view: View?) {
        setIsLoading(true)
        addDisposable(siteStrengthDao.insertAllCacheStrengthEntity(strengthAdapter.items)
            .compose(transformSingle())
            .subscribe({
                setIsLoading(false)
                message.what = NavigationConstants.OPEN_DASH_BOARD
                liveData.postValue(message)
            }, {
                setIsLoading(false)
                showToast("Unable to update... Please Try Later")
                Timber.e(it)
            }))
    }

    fun initViewModel() {
        strengthAdapter.clear()
        addDisposable(siteStrengthDao.fetchAllCacheStrengthByTaskId(Prefs.getInt(PrefConstants.CURRENT_TASK))
            .compose(transformSingle())
            .subscribe({
                strengthAdapter.clearAndSetItems(it)
            }, {
                Timber.e(it)
            }))
    }

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