package com.sisindia.ai.android.features.issues.grievance

import androidx.annotation.UiThread
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.work.Data
import com.sisindia.ai.android.IopsApplication
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.features.addgrievances.GrievanceItemListener
import com.sisindia.ai.android.features.addgrievances.GrievanceRecyclerAdapter
import com.sisindia.ai.android.models.GrievanceModel
import com.sisindia.ai.android.room.dao.GrievanceDao
import com.sisindia.ai.android.room.entities.GrievanceEntity
import com.sisindia.ai.android.uimodels.IssueSeverityModel
import com.sisindia.ai.android.workers.GrievanceWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import javax.inject.Inject

class GrievanceIssueManagementViewModel @Inject constructor(val appl: IopsApplication) :
    IopsBaseViewModel(appl) {

    var openGrievanceAdapter = GrievanceRecyclerAdapter()
    var closedGrievanceAdapter = GrievanceRecyclerAdapter()

    var openCount = ObservableInt(0)
    var closeCount = ObservableInt(0)

    var severity = ObservableField(IssueSeverityModel())

    var grievanceItemClickListener = object : GrievanceItemListener {

        override fun onGrievanceItemClick(item: GrievanceModel) {
            val status = GrievanceEntity.GrievanceStatus.of(item.grievanceStatus)
            if (status != null) {
                when (status) {
                    GrievanceEntity.GrievanceStatus.UNKNOWN, GrievanceEntity.GrievanceStatus.ASSIGNED, GrievanceEntity.GrievanceStatus.NEW, GrievanceEntity.GrievanceStatus.IN_PROGRESS -> {
                        message.what = NavigationConstants.ON_EDIT_GRIEVANCE
                        message.arg1 = item.grievanceId
                        liveData.postValue(message)
                    }
                    GrievanceEntity.GrievanceStatus.COMPLETED, GrievanceEntity.GrievanceStatus.CLOSED -> {
                        message.what = NavigationConstants.ON_VIEW_GRIEVANCE
                        liveData.postValue(message)
                    }
                }
            }
        }
    }

    @Inject
    lateinit var grievanceDao: GrievanceDao

    @UiThread
    fun initViewModel() {

        addDisposable(grievanceDao.fetchAllOpenGrievances()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list: List<GrievanceModel?> ->
                openGrievanceAdapter.clearAndSetItems(list)
                openCount.set(list.size)
            }, Timber::e
            ))

        addDisposable(grievanceDao.fetchAllClosedGrievances()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list: List<GrievanceModel?> ->
                closedGrievanceAdapter.clearAndSetItems(list)
                closeCount.set(list.size)
            }, Timber::e
            ))

        addDisposable(
            grievanceDao.fetchGrievanceSeverity(LocalDateTime.now().toString(),
                LocalDateTime.now().minusHours(48).toString(),
                LocalDateTime.now().minusDays(7).toString())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ severity.set(it) }, Timber::e))


        val inputData = Data.Builder()
            .putInt(GrievanceWorker::class.java.simpleName,
                GrievanceWorker.GrievanceWorkerType.SYNC_FROM_SERVER.workerType)
            .build()
        oneTimeWorkerWithInputData(GrievanceWorker::class.java, inputData)
    }


}