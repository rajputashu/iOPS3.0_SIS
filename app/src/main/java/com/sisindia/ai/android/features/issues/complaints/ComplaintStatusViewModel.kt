package com.sisindia.ai.android.features.issues.complaints

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.room.dao.ComplaintDao
import com.sisindia.ai.android.uimodels.ClosedComplaintModel
import com.sisindia.ai.android.uimodels.ComplaintModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class ComplaintStatusViewModel @Inject constructor(val appln: Application) :
    IopsBaseViewModel(appln) {

    var addedComplaint = ObservableField(ComplaintModel())
    var closedComplaint = ObservableField(ClosedComplaintModel())

    @Inject
    lateinit var complaintDao: ComplaintDao


    fun fetchComplaintDetail(complaintId: Int) {
        addDisposable(complaintDao.fetchComplaintById(complaintId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                addedComplaint.set(it)
            }, Timber::e))
    }

    fun onClosedComplaintDetail(complaintId: Int) {
        addDisposable(complaintDao.fetchDetailOnClosedClose(complaintId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                closedComplaint.set(it)
            }, Timber::e))
    }

    fun onAddedComplaintContinue(view: View) {
        message.what = NavigationConstants.ON_COMPLAINT_ADDED_CONTINUE
        liveData.postValue(message)
    }

}