package com.sisindia.ai.android.features.moninput

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.features.moninput.adapter.MonInputCardAdapter
import com.sisindia.ai.android.models.rota.RotaResponse
import com.sisindia.ai.android.room.dao.TaskDao
import com.sisindia.ai.android.room.entities.TaskEntity
import com.sisindia.ai.android.uimodels.moninput.MonInputCardDetailMO
import com.sisindia.ai.android.uimodels.moninput.MonInputCountMO
import com.sisindia.ai.android.utils.TimeUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 6/2/2020.
 */
class MonInputViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    val obsPendingCount = ObservableField("0")
    val obsCompletedCount = ObservableField("0")
    val obsOverdueCount = ObservableField("0")

    @Inject
    lateinit var taskDao: TaskDao

    val monInputAdapter = MonInputCardAdapter()
    val monInputListener = object : MonInputListener {
        override fun onMonInputSelected(selectedTaskId: Int) {
            Prefs.putInt(PrefConstants.CURRENT_TASK, selectedTaskId)
            message.what = NavigationConstants.OPEN_MONINPUT_TASK
            liveData.postValue(message)
        }
    }

    fun getMonInputDetails() {
        addDisposable(Single.zip(taskDao.fetchMonInputCount(),
            taskDao.fetchPendingMonInputDetails(), taskDao.fetchCompletedMonInputDetails(),
            Function3<MonInputCountMO, List<MonInputCardDetailMO>, List<MonInputCardDetailMO>, ArrayList<Any>> { monInputCount, pendingBillsList, completedBillsList ->
                return@Function3 onBillCollectionDetailsFetch(monInputCount,
                    pendingBillsList, completedBillsList)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                monInputAdapter.updateMonInputData(it)
            }, {
                it.printStackTrace()
            }))
    }

    private fun onBillCollectionDetailsFetch(countsDetails: MonInputCountMO, pendingList: List<MonInputCardDetailMO>,
        completedList: List<MonInputCardDetailMO>): ArrayList<Any> {

        val finalList = arrayListOf<Any>()
        finalList.clear()

        countsDetails.apply {
            obsPendingCount.set(pendingCount)
            obsCompletedCount.set(completedCount)
            obsOverdueCount.set(overdueCount)
        }
        finalList.add("PENDING")
        if (pendingList.isNotEmpty())
            finalList.addAll(pendingList)
        finalList.add("COMPLETED")
        if (completedList.isNotEmpty())
            finalList.addAll(completedList)
        return finalList
    }

    //    fun getMonInputTasksFromServer(weekType: String) {
    fun getMonInputTasksFromServer() {
        /*var weekNumber = TimeUtils.getCurrentWeekOfYear()
        if (weekType == "LAST_WEEK")
            weekNumber -= 1*/

        isLoading.set(View.VISIBLE)
        //        addDisposable(Single.zip(taskDao.fetchAllAutoMonInputRotas(), coreApi.getWeeklyMonInputRotas(weekNumber),
        val currentDate = TimeUtils.YYYY_MM_DD_FORMAT(LocalDate.now())
        addDisposable(Single.zip(taskDao.fetchAllAutoMonInputRotas(), coreApi.getMonthlyMonInputRotas(currentDate),
            BiFunction<List<TaskEntity>, RotaResponse, Boolean> { localMITasksList, rotaResponse ->
                return@BiFunction onResultFetch(localMITasksList, rotaResponse)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message.what = NavigationConstants.ON_UPDATING_MON_INPUT_UI
                liveData.value = message
            }, {
                isLoading.set(View.GONE)
                it.printStackTrace()
            }))
    }

    private fun onResultFetch(monInputLocalRotasList: List<TaskEntity>, miRotasResponse: RotaResponse): Boolean {
        isLoading.set(View.GONE)
        if (miRotasResponse.statusCode == 200 && miRotasResponse.rota != null && miRotasResponse.rota!!.siteTasks != null)
            for (task in miRotasResponse.rota!!.siteTasks!!) {
                if (!monInputLocalRotasList.isNullOrEmpty() && isMonInputRotaExists(task.serverId, monInputLocalRotasList)) {
                    //                    Will handle further cases if required...
                    Timber.e("MonInput : Task Already exists")
                } else {
                    val serverTask = TaskEntity(task.siteId, task.taskTypeId, task.taskStatus, task.description,
                        task.estimatedTaskExecutionStartDateTime, task.actualTaskExecutionStartDateTime,
                        task.estimatedTaskExecutionEndDateTime, task.actualTaskExecutionEndDateTime, task.isAutoCreation,
                        task.reasonLookUpIdentifier, task.serverId, task.barrackId, task.taskExecutionResult,
                        task.otherTaskTypeLookUpIdentifier)
                    insertMonInputRotas(serverTask)
                }
            }
        return true
    }

    private fun isMonInputRotaExists(serverMIRotaId: Int, monInputLocalRotasList: List<TaskEntity>): Boolean {
        for (localTask in monInputLocalRotasList) {
            if (localTask.serverId == serverMIRotaId)
                return true
        }
        return false
    }

    private fun insertMonInputRotas(task: TaskEntity) {
        addDisposable(taskDao.insert(task)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, { t: Throwable? ->
                Timber.e(t)
            }))
    }
}