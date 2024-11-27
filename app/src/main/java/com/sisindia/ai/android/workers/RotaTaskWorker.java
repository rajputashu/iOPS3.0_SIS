package com.sisindia.ai.android.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import com.droidcommons.dagger.worker.AndroidWorkerInjection;
import com.sisindia.ai.android.base.BaseWorker;
import com.sisindia.ai.android.models.TableSyncResponse;
import com.sisindia.ai.android.models.rota.RotaResponse;
import com.sisindia.ai.android.room.dao.DailyTimeLineDao;
import com.sisindia.ai.android.room.dao.DayCheckDao;
import com.sisindia.ai.android.room.dao.TaskDao;
import com.sisindia.ai.android.room.entities.RotaTasksEntity;
import com.sisindia.ai.android.room.entities.TaskEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class RotaTaskWorker extends BaseWorker {

    @Inject
    TaskDao taskDao;

    @Inject
    DayCheckDao dayCheckDao;

    @Inject
    DailyTimeLineDao dailyTimeLineDao;

    @Inject
    public RotaTaskWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        AndroidWorkerInjection.inject(this);
    }

    @NonNull
    @Override
    public Result doWork() {

        int workType = getInputData().getInt(RotaTaskWorker.class.getSimpleName(), RotaTaskWorkerType.SYNC_TO_SERVER.workerType);

        if (workType == RotaTaskWorkerType.SYNC_FROM_SERVER.workerType) {
            syncFromServer();
        } else {
            syncToServer();
//            fetchAllNonSyncedItems();
        }
        return Result.success();
    }

    //For previous changes view RotaTaskWorker stored in desktop folder
    private synchronized void syncFromServer() {
        addDisposable(Single.zip(coreApi.getServerRotas(), taskDao.fetchAll(), (rotaResponse, localTasks) -> {
            //INSERTING ALL ROTA TASKS
            if (rotaResponse != null && rotaResponse.rota != null && rotaResponse.rota.rotaTasks != null) {
                addDisposable(taskDao.deleteRotaTasks()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(i -> insertAllRotaTasksToDB(rotaResponse.rota.rotaTasks), Timber::e));
//                        insertAllRotaTasksToDB(rotaResponse.rota.rotaTasks);
            }

            //INSERTING ALL TASKS {AUTO OR ADHOC}
            if (rotaResponse.rota != null && rotaResponse.rota.siteTasks != null) {
                for (RotaResponse.SiteTaskResponse serverTask : rotaResponse.rota.siteTasks) {
                    int taskPosition = isRotaAlreadyExist(serverTask.serverId, localTasks);

                    if (taskPosition != -1) {
                        //Task Already present in local {update LOCAL TASK respectively}
                        TaskEntity localTask = localTasks.get(taskPosition);
                        if (localTask.isSynced && localTask.taskStatus > serverTask.taskStatus) {
                            addDisposable(coreApi.addOrUpdateCreatedTask(localTask)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(task -> updateTaskTable(task.data, localTask), this::onApiError));
                        } else if (localTask.isSynced && localTask.taskStatus < serverTask.taskStatus) {
                            updateTaskStatus(serverTask.taskStatus, localTask.id);
                        } else
                            Timber.e("Condition : Everything is perfect, Don't do anything for god sake :)");

                    } else {
                        // inserting new ROTA {as it is not present in local DB}
                        TaskEntity newRotaTask = new TaskEntity(serverTask.siteId, serverTask.taskTypeId, serverTask.taskStatus, serverTask.estimatedTaskExecutionStartDateTime,
                                serverTask.actualTaskExecutionStartDateTime, serverTask.estimatedTaskExecutionEndDateTime, serverTask.actualTaskExecutionEndDateTime, serverTask.isAutoCreation,
                                serverTask.reasonLookUpIdentifier, serverTask.serverId, serverTask.barrackId, serverTask.taskExecutionResult, serverTask.otherTaskTypeLookUpIdentifier, serverTask.description);
                        insertTaskItem(newRotaTask);
                    }
                }
            }
            return true;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isDone -> Timber.d("All Server rota synced success"), this::onApiError));
    }

    private void insertAllRotaTasksToDB(List<RotaTasksEntity> rotaTasksEntities) {
        addDisposable(taskDao.insertAllRotaTasks(rotaTasksEntities)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rowId -> Timber.d("rotaTasksEntities Inserted"), Timber::e));
    }

    private int isRotaAlreadyExist(int serverRotaId, List<TaskEntity> localTableRotasList) {
        for (int i = 0; i < localTableRotasList.size(); i++) {
            if (localTableRotasList.get(i).serverId == serverRotaId)
                return i;
        }
        return -1;
    }

    private void insertTaskItem(TaskEntity item) {
        addDisposable(taskDao.insert(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rowId -> {
                }, Timber::e));
    }

    private void syncToServer() {
        addDisposable(taskDao.fetchAllNotSyncedTask(false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(unSyncedTaskList -> {
                    if (unSyncedTaskList.size() > 0)
                        uploadTaskToServerRecursively(unSyncedTaskList, 0);
                }, Timber::e));
    }

    /*private void uploadToServer(List<TaskEntity> tasks) {
        for (TaskEntity item : tasks) {
            //Changes made by AR
            if (item.serverId == 0 && item.taskStatus == 4) {
                item.taskStatus = 1;
                updateTaskStatusOfCompletedTask(item, false);
            } else {
                addDisposable(coreApi.addOrUpdateCreatedTask(item)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(task -> updateTaskTable(task.data, item), this::onApiError));
            }
        }
    }*/

    private void uploadTaskToServerRecursively(List<TaskEntity> tasks, int noOfCalls) {
        if (noOfCalls < tasks.size()) {
            addDisposable(coreApi.addOrUpdateCreatedTask(tasks.get(noOfCalls))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(task -> {
                        if (task.statusCode == 200)
                            updateTaskTable(task.data, tasks.get(noOfCalls));
                        uploadTaskToServerRecursively(tasks, noOfCalls + 1);
                    }, e -> uploadTaskToServerRecursively(tasks, noOfCalls + 1)));
        }
    }

    private void updateTaskTable(TableSyncResponse.TableSyncData data, TaskEntity item) {
        if (data != null && data.serverId != 0)
            addDisposable(taskDao.updateTaskOnServerSync(data.serverId, item.localId, true)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rowId -> Timber.d("updated in Rota task"), Timber::e));
    }

    private void updateTaskStatus(int updatedTaskStatus, int localTaskId) {
        addDisposable(taskDao.updateTaskStatus(updatedTaskStatus, localTaskId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rowId -> Timber.d("LOCAL TASK is updated in Rota task table"), Timber::e));
    }

    //Changes made by AR
    private void updateTaskStatusOfCompletedTask(TaskEntity item, Boolean isServerIdSynced) {
        addDisposable(coreApi.addOrUpdateCreatedTask(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(task -> {
                    if (!isServerIdSynced)
                        updateTaskTableAndReSync(task.data, item);
                    else
                        Timber.d("ServerId already synced nothing to do....");
                }, this::onApiError));
    }

    //Changes made by AR
    private void updateTaskTableAndReSync(TableSyncResponse.TableSyncData data, TaskEntity item) {
        if (data != null && data.serverId != 0)
            addDisposable(taskDao.updateTaskOnServerSync(data.serverId, item.localId, true)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rowId -> {
                        Timber.d("Rota is updated in Rota task and init reSync");
                        item.serverId = data.serverId;
                        item.taskStatus = 4;
                        updateTaskStatusOfCompletedTask(item, true);
                    }, Timber::e));
    }

    /*
     * Don't delete the code just
     * COMMENT BELOW CODE AS PER REQUIREMENT RAISED BY MANISH SIR
     */
    /* private void fetchAllNonSyncedItems() {
        addDisposable(dailyTimeLineDao.fetchAllNotSyncedItems(false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::uploadDailyTimeLineToServer, Timber::e));
    }
    private void uploadDailyTimeLineToServer(List<DailyTimeLineEntity> items) {
        for (DailyTimeLineEntity item : items) {
            addDisposable(coreApi.dailyTimeLine(item)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> updateDailyTimeLine(response.data, item), this::onApiError));
        }
    }
    private void updateDailyTimeLine(TableSyncResponse.TableSyncData data, DailyTimeLineEntity item) {
        if (data != null && data.serverId != 0) {
            addDisposable(dailyTimeLineDao.updateItemOnServerSync(data.serverId, item.localId, true)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rowId -> Timber.d("Duty status row id %s", rowId), Timber::e));
        }
    }*/

    public enum RotaTaskWorkerType {
        SYNC_FROM_SERVER(1),
        SYNC_TO_SERVER(2);

        private final int workerType;

        RotaTaskWorkerType(int workerType) {
            this.workerType = workerType;
        }

        public int getWorkerType() {
            return workerType;
        }
    }
}


/*
 * 1). Tasks from the server which are in status = 1, these task id's are not available in local, then insert those records in your local table
 * 2). Tasks from the server which are in status = 1, but those id's are available in local db, but with a status > 1, then you don't have to consider server data
 * 3). Tasks from the server which are in status = 3, inactive, if those id's are available in local db, with any other status other than 3, then mark those tasks as status = 3 in local db
 */