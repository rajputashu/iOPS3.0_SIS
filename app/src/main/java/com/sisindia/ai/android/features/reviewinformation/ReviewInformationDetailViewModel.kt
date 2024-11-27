package com.sisindia.ai.android.features.reviewinformation

import android.app.Application
import android.view.View
import androidx.databinding.ObservableInt
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.features.addgrievances.GrievanceRecyclerAdapter
import com.sisindia.ai.android.features.issues.complaints.ComplaintRecyclerAdapter
import com.sisindia.ai.android.room.dao.ComplaintDao
import com.sisindia.ai.android.room.dao.GrievanceDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class ReviewInformationDetailViewModel @Inject constructor(application: Application) :
    IopsBaseViewModel(application) {

    @Inject
    lateinit var complaintDao: ComplaintDao

    @Inject
    lateinit var grievanceDao: GrievanceDao

    var openGrievanceAdapter = GrievanceRecyclerAdapter()
    var closedGrievanceAdapter = GrievanceRecyclerAdapter()

    var openComplaintsAdapter = ComplaintRecyclerAdapter()
    var closedComplaintsAdapter = ComplaintRecyclerAdapter()

    var complaintOpenCount = ObservableInt(0)
    var complaintCloseCount = ObservableInt(0)
    var grievanceOpenCount = ObservableInt(0)
    var grievanceCloseCount = ObservableInt(0)

    var openGrievanceVisibility = ObservableInt(View.GONE)
    var closedGrievanceVisibility = ObservableInt(View.GONE)

    var noOpenGrievanceVisibility = ObservableInt(View.GONE)
    var noClosedGrievanceVisibility = ObservableInt(View.GONE)


    var openComplaintsVisibility = ObservableInt(View.GONE)
    var closedComplaintsVisibility = ObservableInt(View.GONE)

    var noOpenComplaintsVisibility = ObservableInt(View.GONE)
    var noClosedComplaintsVisibility = ObservableInt(View.GONE)

    fun initViewModelForGrievances(viewType: Int) {
        val siteId = Prefs.getInt(PrefConstants.CURRENT_SITE)
        when (viewType) {
            ReviewInformationGrievanceFragment.FRAGMENT_TYPE_OPEN -> {
                addDisposable(
                    grievanceDao.fetchAllOpenGrievanceBySiteId(siteId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            noOpenGrievanceVisibility.set(if (it.size == 0) View.VISIBLE else View.GONE)
                            openGrievanceVisibility.set(View.VISIBLE)
                            closedGrievanceVisibility.set(View.GONE)
                            grievanceOpenCount.set(it.size)
                            openGrievanceAdapter.clearAndSetItems(it)
                        }, Timber::e))
            }

            ReviewInformationGrievanceFragment.FRAGMENT_TYPE_CLOSED -> {
                addDisposable(
                    grievanceDao.fetchAllClosedGrievanceBySiteId(siteId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            noClosedGrievanceVisibility.set(if (it.size == 0) View.VISIBLE else View.GONE)
                            openGrievanceVisibility.set(View.GONE)
                            closedGrievanceVisibility.set(View.VISIBLE)
                            grievanceCloseCount.set(it.size)
                            closedGrievanceAdapter.clearAndSetItems(it)
                        }, Timber::e))
            }
        }
    }


    fun initViewModelForComplaints(viewType: Int) {
        val siteId = Prefs.getInt(PrefConstants.CURRENT_SITE)
        when (viewType) {
            ReviewInformationComplaintFragment.FRAGMENT_TYPE_OPEN -> {
                addDisposable(complaintDao.fetchAllPendingComplaintsBySite(siteId)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        noOpenComplaintsVisibility.set(if (it.size == 0) View.VISIBLE else View.GONE)
                        openComplaintsVisibility.set(View.VISIBLE)
                        closedComplaintsVisibility.set(View.GONE)
                        complaintOpenCount.set(it.size)
                        openComplaintsAdapter.clearAndSetItems(it)
                    }, Timber::e))
            }
            ReviewInformationComplaintFragment.FRAGMENT_TYPE_CLOSED -> {
                addDisposable(complaintDao.fetchAllClosedComplaintsBySite(siteId)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        noClosedComplaintsVisibility.set(if (it.size == 0) View.VISIBLE else View.GONE)
                        openComplaintsVisibility.set(View.GONE)
                        closedComplaintsVisibility.set(View.VISIBLE)
                        complaintCloseCount.set(it.size)
                        closedComplaintsAdapter.clearAndSetItems(it)
                    }, Timber::e))
            }
        }
    }
}