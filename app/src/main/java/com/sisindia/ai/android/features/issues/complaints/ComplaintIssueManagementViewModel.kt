package com.sisindia.ai.android.features.issues.complaints

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.work.Data
import com.sisindia.ai.android.IopsApplication
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.room.dao.ComplaintDao
import com.sisindia.ai.android.room.entities.ComplaintEntity
import com.sisindia.ai.android.uimodels.ComplaintModel
import com.sisindia.ai.android.uimodels.IssueSeverityModel
import com.sisindia.ai.android.workers.ComplaintWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import javax.inject.Inject

class ComplaintIssueManagementViewModel @Inject constructor(val appl: IopsApplication) :
    IopsBaseViewModel(appl) {

    var openComplaintsAdapter =
        ComplaintRecyclerAdapter()
    var closedComplaintsAdapter =
        ComplaintRecyclerAdapter()

    var openCount = ObservableInt(0)
    var closeCount = ObservableInt(0)

    var severity = ObservableField(IssueSeverityModel())

    @Inject
    lateinit var complaintDao: ComplaintDao


    var complaintItemListener = object : ComplaintItemListener {

        override fun onComplaintItemClick(item: ComplaintModel) {
            val status = ComplaintEntity.ComplaintStatus.of(item.status)
            if (status != null) {
                when (status) {
                    ComplaintEntity.ComplaintStatus.CREATED, ComplaintEntity.ComplaintStatus.PENDING -> {
                        message.what = NavigationConstants.ON_EDIT_COMPLAINT
                        message.arg1 = item.id
                        liveData.postValue(message)
                    }
                    ComplaintEntity.ComplaintStatus.CLOSED, ComplaintEntity.ComplaintStatus.COMPLETED -> {
                        toast("completed")
                    }
                }
            }
        }
    }


    fun initViewModel() {
        addDisposable(complaintDao.fetchAllPendingComplaints()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                openComplaintsAdapter.clearAndSetItems(it)
                openCount.set(it.size)
            }, Timber::e))

        addDisposable(complaintDao.fetchAllClosedComplaints()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                closedComplaintsAdapter.clearAndSetItems(it)
                closeCount.set(it.size)
            }, Timber::e))

        addDisposable(
            complaintDao.fetchComplaintsSeverity(LocalDateTime.now().toString(),
                LocalDateTime.now().minusHours(48).toString(),
                LocalDateTime.now().minusDays(7).toString())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ severity.set(it) }, Timber::e))


        val inputData = Data.Builder()
            .putInt(ComplaintWorker::class.java.simpleName,
                ComplaintWorker.ComplaintWorkerType.SYNC_FROM_SERVER.workerType)
            .build()
        oneTimeWorkerWithInputData(ComplaintWorker::class.java, inputData)
    }
}