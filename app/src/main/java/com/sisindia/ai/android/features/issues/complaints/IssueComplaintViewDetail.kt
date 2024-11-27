package com.sisindia.ai.android.features.issues.complaints

import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableField
import androidx.work.Data
import com.sisindia.ai.android.IopsApplication
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.room.dao.ComplaintDao
import com.sisindia.ai.android.room.entities.ComplaintEntity
import com.sisindia.ai.android.uimodels.ComplaintModel
import com.sisindia.ai.android.workers.ComplaintWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import timber.log.Timber
import javax.inject.Inject

class IssueComplaintViewDetail @Inject constructor(val appln: IopsApplication) :
    IopsBaseViewModel(appln) {

    var complaint = ObservableField(ComplaintModel())
    var additionalRemarks = ObservableField("")

    var pendingText = ObservableField("")

    @Inject
    lateinit var complaintDao: ComplaintDao

    fun fetchComplaintDetail(complaintId: Int) {
        addDisposable(complaintDao.fetchComplaintById(complaintId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                complaint.set(it)
                updatePending(it)
            }, Timber::e))
    }

    private fun updatePending(item: ComplaintModel) {
        val status = ComplaintEntity.ComplaintStatus.of(item.status)
        if (status != null) {
            when (status) {
                ComplaintEntity.ComplaintStatus.CREATED, ComplaintEntity.ComplaintStatus.PENDING -> {

                    val today = LocalDateTime.now()
                    val targetDate =
                        LocalDateTime.parse(item.targetCompletionDate)
                    val days =
                        ChronoUnit.DAYS.between(targetDate, today)

                    if (days > 0) {
                        val suffixStr: String =
                            appln.getString(R.string.text_issues_days_pending_for)
                        val prefixStr: String = TextUtils.concat(days.toString(),
                            appln.getString(R.string.text_issues_days))
                            .toString()
                        pendingText.set(prefixStr + suffixStr)
                    }
                }
                else -> {}
            }
        }
    }


    fun onCloseComplaint(view: View) {

        val remarks = additionalRemarks.get()
        val complaintItem = complaint.get() as ComplaintModel

        if (TextUtils.isEmpty(remarks)) {
            showToast("Please provide remarks")
            return
        }

        addDisposable(complaintDao.updateComplaintWithCloseRemarks(complaintItem.id,
            remarks,
            ComplaintEntity.ComplaintStatus.CLOSED.status,
            false)
            .compose(transformSingle()).subscribe({
                val inputData = Data.Builder()
                    .putInt(ComplaintWorker::class.java.simpleName,
                        ComplaintWorker.ComplaintWorkerType.SYNC_TO_SERVER.workerType)
                    .build()
                oneTimeWorkerWithInputData(ComplaintWorker::class.java, inputData)
                message.what = NavigationConstants.ON_COMPLAINT_CLOSED
                message.arg1 = it
                liveData.postValue(message)
            }, Timber::e))

    }
}