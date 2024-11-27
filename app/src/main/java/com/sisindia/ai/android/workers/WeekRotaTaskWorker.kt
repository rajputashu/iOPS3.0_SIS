package com.sisindia.ai.android.workers

import android.content.Context
import androidx.work.WorkerParameters
import com.droidcommons.dagger.worker.AndroidWorkerInjection
import com.sisindia.ai.android.base.BaseWorker
import com.sisindia.ai.android.models.rota.RotaResponse
import com.sisindia.ai.android.room.dao.TaskDao
import com.sisindia.ai.android.room.entities.TaskEntity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 1/10/2021.
 */
class WeekRotaTaskWorker @Inject constructor(context: Context, workerParameters: WorkerParameters) :
    BaseWorker(context, workerParameters) {

    init {
        AndroidWorkerInjection.inject(this)
    }

    @Inject
    lateinit var taskDao: TaskDao

    override fun doWork(): Result {
        syncFromServer()
        return Result.success()
    }

    private fun syncFromServer() {
        //        val lastWeekNo = TimeUtils.getCurrentWeekOfYear() - 1
        addDisposable(Single.zip(coreApi.getServerRotasViaWeek(true), taskDao.fetchAll(),
            BiFunction<RotaResponse, List<TaskEntity>, Boolean> { rotaResponse, localTaskList ->
                return@BiFunction onResultFetch(rotaResponse, localTaskList)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

            }, {

            }))
    }

    private fun onResultFetch(rotaResponse: RotaResponse, localTaskList: List<TaskEntity>): Boolean {
        rotaResponse.let { it ->
            it.rota.let {
                it.rotaTasks?.let {
                    addDisposable(taskDao.insertAllRotaTasks(it)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Timber.e("rotaTasksEntities Inserted")
                        }) { t: Throwable? -> Timber.e(t) })
                }

                if (it.siteTasks.size > 0) {
                    var index: Int
                    for (serverTask: RotaResponse.SiteTaskResponse in it.siteTasks) {
                        index = localTaskList.indexOfFirst { localTask ->
                            localTask.serverId == serverTask.serverId
                        }
//                        Timber.e("Condition : Index Value is $index")
                        if (index > -1) {
                            /*val existingTask = localTaskList[index]
                            if (existingTask.isSynced && existingTask.taskStatus > serverTask.taskStatus) {
                                Timber.e("Condition : Last Week Rota Condition 1")
                            } else if (existingTask.isSynced && existingTask.taskStatus < serverTask.taskStatus) {
                                Timber.e("Condition : Last Week Rota Condition 2")
                                //                                    updateTaskStatus(serverTask.taskStatus, existingTask.serverId)
                            } else {
                                Timber.e("Condition : Everything is perfect while handling Rotas...)")
                            }*/
                        } else {
//                            Timber.e("Condition : Adding new Last Week Rota")
                            val newTask = TaskEntity(serverTask.siteId, serverTask.taskTypeId, serverTask.taskStatus,
                                serverTask.estimatedTaskExecutionStartDateTime,
                                serverTask.actualTaskExecutionStartDateTime,
                                serverTask.estimatedTaskExecutionEndDateTime,
                                serverTask.actualTaskExecutionEndDateTime, serverTask.isAutoCreation,
                                serverTask.reasonLookUpIdentifier, serverTask.serverId,
                                serverTask.barrackId, serverTask.taskExecutionResult, serverTask.otherTaskTypeLookUpIdentifier,serverTask.description)
                            insertNewTask(newTask)
                        }
                    }
                }
            }
        }
        return true
    }

    private fun insertNewTask(taskEntity: TaskEntity) {
        addDisposable(taskDao.insert(taskEntity)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
//                Timber.d("New Task Inserted...")
            }) { t: Throwable? -> Timber.e(t) })
    }

    private fun updateTaskStatus(status: Int, localTaskId: Int) {
        addDisposable(taskDao.updateTaskStatus(status, localTaskId)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("Status of Task is updated....")
            }) { t: Throwable? -> Timber.e(t) })
    }
}