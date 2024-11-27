package com.sisindia.ai.android.features.barracks.inspection

import android.app.Application
import androidx.work.Data
import com.droidcommons.base.timer.CountUpTimer
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.room.dao.TaskDao
import com.sisindia.ai.android.room.entities.TaskEntity
import com.sisindia.ai.android.workers.RotaTaskWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/20/2020.
 */
class BarrackInspectionViewModel @Inject constructor(val app: Application) :
    IopsBaseViewModel(app) {

    @Inject
    lateinit var timer: CountUpTimer

    @Inject
    lateinit var taskDao: TaskDao

    fun updateTaskExecutionStartDetails() {
        addDisposable(taskDao.updateTaskOnStartDayCheck(Prefs.getInt(PrefConstants.CURRENT_TASK),
            LocalDateTime.now().toString(), TaskEntity.TaskStatus.IN_PROGRESS.taskStatus)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ triggerRotaTaskWorker() }, { }))
    }

    private fun triggerRotaTaskWorker() {
        val inputData = Data.Builder().putInt(RotaTaskWorker::class.java.simpleName,
            RotaTaskWorker.RotaTaskWorkerType.SYNC_TO_SERVER.workerType).build()
        oneTimeWorkerWithInputData(RotaTaskWorker::class.java, inputData)
    }
}