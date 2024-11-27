package com.sisindia.ai.android.features.barracks.inspection.frags

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import androidx.work.Data
import com.droidcommons.base.timer.CountUpTimer
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.commons.NavigationUiRecyclerAdapter
import com.sisindia.ai.android.commons.NavigationViewListeners
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.room.dao.BarrackDao
import com.sisindia.ai.android.room.dao.DailyTimeLineDao
import com.sisindia.ai.android.room.dao.TaskDao
import com.sisindia.ai.android.room.entities.BarrackEntity
import com.sisindia.ai.android.room.entities.CacheBarrackInspectionEntity
import com.sisindia.ai.android.room.entities.DailyTimeLineEntity
import com.sisindia.ai.android.services.GeoLocationService
import com.sisindia.ai.android.uimodels.NavigationUIModel
import com.sisindia.ai.android.uimodels.NavigationUIModel.NavigationUiViewType.*
import com.sisindia.ai.android.uimodels.barracks.*
import com.sisindia.ai.android.utils.GsonUtils
import com.sisindia.ai.android.workers.RotaTaskWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ashu Rajput on 4/20/2020.
 */
class BarrackInspectionHomeViewModel @Inject constructor(val app: Application) :
    IopsBaseViewModel(app) {

    @Inject
    lateinit var timer: CountUpTimer

    @Inject
    lateinit var barrackDao: BarrackDao

    @Inject
    lateinit var taskDao: TaskDao

    @Inject
    @Named("BarrackInspectionNavigation")
    lateinit var navigationUiModelList: ArrayList<NavigationUIModel>

    @Inject
    lateinit var dailyTimeLineDao: DailyTimeLineDao

    val isBarrackInspectionCompleted = ObservableField(false)
    var obsBarrackUnit = ObservableField("")
    var obsBarrackCode = ObservableField("")
    var obsBarrackAddress = ObservableField("")
    val barrackActionAdapter = NavigationUiRecyclerAdapter()
    lateinit var cacheBarrackInspectionEntity: CacheBarrackInspectionEntity

    var viewListeners = NavigationViewListeners { model: NavigationUIModel? ->
        when (model!!.viewType.navigationViewType) {
            BARRACK_INSPECTION_STRENGTH.navigationViewType ->
                message.what = NavigationConstants.OPEN_BARRACK_INSPECTION_STRENGTH
            BARRACK_INSPECTION_SPACE.navigationViewType ->
                message.what = NavigationConstants.OPEN_BARRACK_INSPECTION_SPACE
            BARRACK_INSPECTION_OTHERS.navigationViewType ->
                message.what = NavigationConstants.OPEN_BARRACK_INSPECTION_OTHERS
            BARRACK_INSPECTION_LANDLORD.navigationViewType ->
                message.what = NavigationConstants.OPEN_BARRACK_INSPECTION_LANDLORD
        }
        liveData.postValue(message)
    }

    fun updateBarrackActionList() {
        barrackActionAdapter.updateAdapter(navigationUiModelList)
    }

    fun updateBarrackCommonDetails() {
        addDisposable(barrackDao.fetchBarrackDetails(Prefs.getInt(PrefConstants.CURRENT_BARRACK_ID))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ entity: BarrackEntity ->
                entity.apply {
                    obsBarrackUnit.set(name)
                    obsBarrackCode.set(barrackCode)
                    obsBarrackAddress.set(barrackAddress)
                }
            }, { t: Throwable? ->
                t!!.printStackTrace()
                insertCacheBITask()
            }))
    }

    fun checkTaskAlreadyCached() {
        addDisposable(barrackDao.fetchCacheData(Prefs.getInt(PrefConstants.CURRENT_TASK))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ cacheBIEntity: CacheBarrackInspectionEntity ->
                cacheBarrackInspectionEntity = cacheBIEntity
                updateBarrackNavigationUIIfTaskIsCached()

                // If barrack inspection task is done then only updating cache table
                if (isBarrackInspectionCompleted.get()!!)
                    updateBarrackTaggingTable()

            }, {
                //                t!!.printStackTrace()
                insertCacheBITask()
            }))
    }

    //BELOW METHOD IS USED TO UPDATE NAVIGATION ICONS [BARRACK CHECK ACTION] IF BI TASK ALREADY CACHED
    private fun updateBarrackNavigationUIIfTaskIsCached() {
        if (::cacheBarrackInspectionEntity.isInitialized) {
            if (!cacheBarrackInspectionEntity.strengthJson.isNullOrEmpty())
                navigationUiModelList[0].isCompleted = true
            if (!cacheBarrackInspectionEntity.spaceJson.isNullOrEmpty())
                navigationUiModelList[1].isCompleted = true
            if (!cacheBarrackInspectionEntity.othersJson.isNullOrEmpty())
                navigationUiModelList[2].isCompleted = true
            if (!cacheBarrackInspectionEntity.metLandlordJson.isNullOrEmpty())
                navigationUiModelList[3].isCompleted = true

            barrackActionAdapter.updateAdapter(navigationUiModelList)

            //CHECKING IF ALL ACTION IS COMPLETED WILL ENABLE COMPLETE BI BUTTON
            if (navigationUiModelList.isNotEmpty()) {
                for (model: NavigationUIModel in navigationUiModelList) {
                    if (!model.isCompleted)
                        return
                }
                isBarrackInspectionCompleted.set(true)
            }
        }
    }

    private fun insertCacheBITask() {
        addDisposable(barrackDao.insertCacheBIData(CacheBarrackInspectionEntity(Prefs.getInt(
            PrefConstants.CURRENT_TASK)))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ id: Long ->
            }, { t: Throwable? ->
                Timber.e("Error while fetching cache data")
            }))
    }

    fun isAllBarrackTaskCompleted(navigationType: Int): Boolean {
        when (navigationType) {
            BARRACK_INSPECTION_STRENGTH.navigationViewType ->
                navigationUiModelList[0].isCompleted = true
            BARRACK_INSPECTION_SPACE.navigationViewType ->
                navigationUiModelList[1].isCompleted = true
            BARRACK_INSPECTION_OTHERS.navigationViewType ->
                navigationUiModelList[2].isCompleted = true
            BARRACK_INSPECTION_LANDLORD.navigationViewType ->
                navigationUiModelList[3].isCompleted = true
        }
        barrackActionAdapter.updateAdapter(navigationUiModelList)
        for (model: NavigationUIModel in navigationUiModelList) {
            if (!model.isCompleted)
                return false
        }
        return true
    }

    fun onTaskCompleteBtnClick(view: View) {
        if (isBarrackInspectionCompleted.get()!!)
            checkTaskAlreadyCached() // Calling this method to get the already done task {Strength,Others etc}
//            updateBarrackTaggingTable()
    }

    private fun updateBarrackTaggingTable() {
//        checkTaskAlreadyCached()

        if (::cacheBarrackInspectionEntity.isInitialized) {
            val actualExecutionEndDT = LocalDateTime.now().toString()

            val biTaskResultMO = BarrackInspectionTaskResultMO()
            biTaskResultMO.strengthJson = GsonUtils.toJsonWithoutExopse()
                .fromJson(cacheBarrackInspectionEntity.strengthJson, BIStrengthMO::class.java)
            biTaskResultMO.spaceJson = GsonUtils.toJsonWithoutExopse()
                .fromJson(cacheBarrackInspectionEntity.spaceJson, BISpaceMO::class.java)
            biTaskResultMO.othersJson = GsonUtils.toJsonWithoutExopse()
                .fromJson(cacheBarrackInspectionEntity.othersJson, BIOthersMO::class.java)
            biTaskResultMO.metLandlordJson = GsonUtils.toJsonWithoutExopse()
                .fromJson(cacheBarrackInspectionEntity.metLandlordJson, BILandlordsMO::class.java)

            val executionResult = GsonUtils.toJsonWithoutExopse().toJson(biTaskResultMO)

            val loc = Prefs.getDouble(PrefConstants.LATITUDE).toString() + ", " + Prefs.getDouble(
                PrefConstants.LONGITUDE).toString()

            addDisposable(taskDao.updateTaskOnFinish(Prefs.getInt(PrefConstants.CURRENT_TASK),
                actualExecutionEndDT, executionResult, loc)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    addTimeLine()

                    val inputData = Data.Builder().putInt(RotaTaskWorker::class.java.simpleName,
                        RotaTaskWorker.RotaTaskWorkerType.SYNC_TO_SERVER.workerType).build()
                    oneTimeWorkerWithInputData(RotaTaskWorker::class.java, inputData)
                    message.what = NavigationConstants.OPEN_DASH_BOARD
                    liveData.postValue(message)
                }, { throwable: Throwable? ->
                    Timber.e(throwable)
                }))
        }
    }


    private fun addTimeLine() {

        val dailyTimeline =
            DailyTimeLineEntity("BarrackInspection", "")
        addDisposable(dailyTimeLineDao.insert(dailyTimeline)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ rowId: Long? ->
                Timber.d("Time Line added")
            }, { t: Throwable? ->
                Timber.e(t)
            }))
    }
}