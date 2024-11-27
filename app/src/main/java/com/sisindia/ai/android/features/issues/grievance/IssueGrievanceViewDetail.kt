package com.sisindia.ai.android.features.issues.grievance

import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableField
import androidx.work.Data
import com.sisindia.ai.android.IopsApplication
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.models.GrievanceModel
import com.sisindia.ai.android.room.dao.GrievanceDao
import com.sisindia.ai.android.room.entities.GrievanceEntity
import com.sisindia.ai.android.workers.GrievanceWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import timber.log.Timber
import javax.inject.Inject

class IssueGrievanceViewDetail @Inject constructor(val appln: IopsApplication) :
    IopsBaseViewModel(appln) {

    var grievance = ObservableField(GrievanceModel())
    var additionalRemarks = ObservableField("")

    var pendingText = ObservableField("")

    @Inject
    lateinit var grievanceDao: GrievanceDao

    fun fetchGrievanceDetail(grievanceId: Int) {
        addDisposable(grievanceDao.fetchGrievanceById(grievanceId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                grievance.set(it)
                updatePending(it)
            }, Timber::e))
    }

    private fun updatePending(item: GrievanceModel) {
        val status = GrievanceEntity.GrievanceStatus.of(item.grievanceStatus)
        if (status != null) {
            when (status) {
                GrievanceEntity.GrievanceStatus.UNKNOWN, GrievanceEntity.GrievanceStatus.ASSIGNED, GrievanceEntity.GrievanceStatus.NEW, GrievanceEntity.GrievanceStatus.IN_PROGRESS -> {

                    val today = LocalDateTime.now()
                    val targetDate =
                        LocalDateTime.parse(item.targetDateTime)
                    val days =
                        ChronoUnit.DAYS.between(targetDate, today)

                    if (days > 0) {
                        val suffixStr: String =
                            appln.getString(R.string.text_grievance_days_pending_for)
                        val prefixStr: String = TextUtils.concat(days.toString(),
                            appln.getString(R.string.text_grievance_days))
                            .toString()
                        pendingText.set(prefixStr + suffixStr)
                    }
                }
                else -> {}
            }
        }
    }


    fun onCloseGrievance(view: View) {

        val remarks = additionalRemarks.get()
        val grievanceItem = grievance.get() as GrievanceModel

        if (TextUtils.isEmpty(remarks)) {
            showToast("Please provide remarks")
            return
        }

        addDisposable(grievanceDao.updateGrievanceWithCloseRemarks(grievanceItem.grievanceId,
            remarks,
            GrievanceEntity.GrievanceStatus.COMPLETED.status,
            false)
            .compose(transformSingle()).subscribe({

                val inputData = Data.Builder()
                    .putInt(GrievanceWorker::class.java.simpleName,
                        GrievanceWorker.GrievanceWorkerType.SYNC_TO_SERVER.workerType)
                    .build()
                oneTimeWorkerWithInputData(GrievanceWorker::class.java, inputData)
                message.what = NavigationConstants.ON_GRIEVANCE_CLOSED
                message.arg1 = it
                liveData.postValue(message)
            }, Timber::e))

    }
}