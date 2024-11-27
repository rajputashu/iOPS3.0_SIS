package com.sisindia.ai.android.features.taskcheck.clienthandshake.frags

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.constants.PrefConstants.CURRENT_TASK
import com.sisindia.ai.android.features.issues.complaints.ComplaintRecyclerAdapter
import com.sisindia.ai.android.features.taskcheck.clienthandshake.ClientHandshakeListener
import com.sisindia.ai.android.room.dao.ComplaintDao
import com.sisindia.ai.android.room.dao.CustomerContactDao
import com.sisindia.ai.android.room.dao.DayCheckDao
import com.sisindia.ai.android.room.entities.CacheClientHandshakeEntity
import com.sisindia.ai.android.room.entities.CustomerContactEntity
import com.sisindia.ai.android.uimodels.handshake.ClientDetailsMO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/10/2020.
 */
class SummaryHandshakeViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var contactDao: CustomerContactDao

    @Inject
    lateinit var dayCheckDao: DayCheckDao

    @Inject
    lateinit var complaintDao: ComplaintDao

    var openComplaintsAdapter = ComplaintRecyclerAdapter()

    var spannedUnitName = ObservableField<String>("")
    var spannedClientName = ObservableField<String>("")
    var spannedClientNumber = ObservableField<String>("")
    var questionsList = ObservableField<String>("")
    var isComplaintAvailable = ObservableField<Boolean>(false)
    var labelComplaintsCount = ObservableField<String>("00")

    private fun spannedString(value: String, label: String): String {
        return app.resources.getString(R.string.dynamic_string2, value, label)
    }

    val clientListener = object : ClientHandshakeListener {

        override fun onClientSelected(clientId: CustomerContactEntity) {

        }

        override fun onContinueAddFeedbackClicked() {
        }

        override fun onFinalSummaryHandshakeView(optedQuestions: String) {
        }

        override fun onCompleteClientHandshake() {
            message.what = NavigationConstants.ON_COMPLETE_CLIENT_HANDSHAKE
            liveData.postValue(message)
        }
    }

    fun fetchClientDetails() {
        val siteId = Prefs.getInt(PrefConstants.CURRENT_SITE)
        val clientId = Prefs.getInt(PrefConstants.SELECTED_CLIENT_ID)
        addDisposable(contactDao.fetchClientDetails(siteId, clientId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ model: ClientDetailsMO ->
                model.apply {
                    spannedUnitName.set(spannedString(siteName, "Unit Name"))
                    spannedClientName.set(spannedString(title + " " + clientName, "Client Name"))
                    spannedClientNumber.set(spannedString(clientNo!!, "Mobile Number"))
                }
            },
                { throwable: Throwable? ->
                    Timber.e(throwable)
                    showToast("Error while fetching client contact details")
                }))
    }

    fun fetchAddedComplaintsFromDB() {
        addDisposable(complaintDao.fetchAllPendingComplaintsBySite(Prefs.getInt(PrefConstants.CURRENT_SITE))
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isEmpty())
                    isComplaintAvailable.set(false)
                else {
                    isComplaintAvailable.set(true)
                    openComplaintsAdapter.clearAndSetItems(it)
                    labelComplaintsCount.set(it.size.toString())
                }
            }, Timber::e))
    }

    fun onCompleteClientHandshake(view: View) {
        updateFeedbackInClientHandshake()
    }

    fun onAddComplaint(view: View) {
        message.what = NavigationConstants.ON_ADDING_CLIENT_HANDSHAKE_COMPLAINT
        liveData.postValue(message)
    }

    private fun updateFeedbackInClientHandshake() {
        addDisposable(dayCheckDao.updateHandshakeTaskStatusOnComplete(Prefs.getInt(CURRENT_TASK),
            CacheClientHandshakeEntity.ClientHandShakeStatus.COMPLETED.statusType)
            .compose(transformSingle())
            .subscribe({ clientListener.onCompleteClientHandshake() }, {}))
    }
}