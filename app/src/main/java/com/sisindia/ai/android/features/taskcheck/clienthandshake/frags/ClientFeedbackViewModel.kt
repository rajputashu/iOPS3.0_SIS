package com.sisindia.ai.android.features.taskcheck.clienthandshake.frags

import android.app.Application
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.features.taskcheck.clienthandshake.ClientHandshakeListener
import com.sisindia.ai.android.features.taskcheck.clienthandshake.adapters.FeedbackQuestionAdapter
import com.sisindia.ai.android.room.dao.ClientHandShakeQuestionDao
import com.sisindia.ai.android.room.dao.ComplaintDao
import com.sisindia.ai.android.room.dao.CustomerContactDao
import com.sisindia.ai.android.room.dao.DayCheckDao
import com.sisindia.ai.android.room.entities.CacheClientHandshakeEntity
import com.sisindia.ai.android.room.entities.CustomerContactEntity
import com.sisindia.ai.android.uimodels.handshake.ClientDetailsMO
import com.sisindia.ai.android.uimodels.handshake.RatingQuestionsMO
import com.sisindia.ai.android.utils.GsonUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/9/2020.
 */
class ClientFeedbackViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var clientHandShakeQuestionDao: ClientHandShakeQuestionDao

    @Inject
    lateinit var contactDao: CustomerContactDao

    @Inject
    lateinit var dayCheckDao: DayCheckDao

    @Inject
    lateinit var complaintDao: ComplaintDao

    var isRatingGiven = ObservableInt(GONE)
    var spannedUnitName = ObservableField("")
    var spannedClientName = ObservableField("")
    var spannedClientNumber = ObservableField("")
    private var clientRating = ObservableField("")
    var isFocusable = ObservableField(false)
    var feedbackSummaryLabel = ObservableField("")
    val adapter = FeedbackQuestionAdapter()
    val clientNumber = ObservableField("")
    var isComplaintAddedSuccessfully = ObservableField<Boolean>(false)
    val obsNotGreatExpMsgVisibility = ObservableInt(GONE)

    private fun spannedString(value: String, label: String): String {
        return app.resources.getString(R.string.dynamic_string2, value, label)
    }

    val clientListener = object : ClientHandshakeListener {

        override fun onClientSelected(client: CustomerContactEntity) {

        }

        override fun onContinueAddFeedbackClicked() {
        }

        override fun onFinalSummaryHandshakeView(optedQuestions: String) {
            message.what = NavigationConstants.OPEN_FINAL_SUMMARY_HANDSHAKE
            message.obj = optedQuestions
            liveData.postValue(message)
        }

        override fun onCompleteClientHandshake() {
            message.what = NavigationConstants.ON_COMPLETE_CLIENT_HANDSHAKE
            liveData.postValue(message)
        }
    }

    val ratingListener = OnRatingBarChangeListener { ratingBar, rating, fromUser ->
        if (rating > 0.4) {
            isRatingGiven.set(VISIBLE)
            isFocusable.set(true)
            val lockedRating = rating.toInt()
            clientRating.set(lockedRating.toString())
            fetchFeedbackQuestionsFromDB(lockedRating)
        } else {
            isFocusable.set(false)
            isRatingGiven.set(GONE)
        }

        when (rating.toInt()) {
            1 -> {
                feedbackSummaryLabel.set("VERY BAD SERVICE EXPERIENCE")
                obsNotGreatExpMsgVisibility.set(VISIBLE)
            }

            2 -> {
                feedbackSummaryLabel.set("BAD SERVICE EXPERIENCE")
                obsNotGreatExpMsgVisibility.set(VISIBLE)
            }

            3 -> {
                feedbackSummaryLabel.set("GOOD SERVICE EXPERIENCE")
                obsNotGreatExpMsgVisibility.set(VISIBLE)
            }

            4 -> {
                feedbackSummaryLabel.set("VERY GOOD SERVICE EXPERIENCE")
                obsNotGreatExpMsgVisibility.set(GONE)
            }

            5 -> {
                feedbackSummaryLabel.set("EXCELLENT SERVICE EXPERIENCE")
                obsNotGreatExpMsgVisibility.set(GONE)
            }
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
                    spannedClientName.set(spannedString("$title $clientName", "Client Name"))
                    spannedClientNumber.set(spannedString(clientNo!!, "Mobile Number"))
                    clientNumber.set(clientNo)
                }
            },
                { throwable: Throwable? ->
                    Timber.e(throwable)
                    showToast("Error while fetching client contact details")
                }))
    }

    private fun fetchFeedbackQuestionsFromDB(rating: Int) {
        setIsLoading(true)
        addDisposable(clientHandShakeQuestionDao.fetchRatingQuestions(rating)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list: List<RatingQuestionsMO> ->
                setIsLoading(false)
                if (list.isNotEmpty())
                    adapter.clearAndSetItems(list)
                else
                    adapter.clear()
            },
                { throwable: Throwable? ->
                    setIsLoading(false)
                    Timber.e(throwable)
                    showToast("Error while fetching client contact details")
                }))
    }

    fun fetchAddedComplaintsFromDB() {
        showToast("Complaint added successfully...")
        isComplaintAddedSuccessfully.set(true)
        /*addDisposable(complaintDao.fetchAllPendingComplaintsBySite(Prefs.getInt(PrefConstants.CURRENT_SITE))
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                openComplaintsAdapter.clearAndSetItems(it)
                openCount.set(it.size)
            }, Timber::e))*/
    }

    fun onAddComplaint(view: View) {
        if (isComplaintAddedSuccessfully.get()!!)
            showToast("Complaint already added")
        else {
            message.what = NavigationConstants.ON_CLIENT_HANDSHAKE_COMPLAINT
            liveData.postValue(message)
        }
    }

    fun onValidatingOTP(view: View) {
//        getCacheClientHandshakeDetails()
        message.what = NavigationConstants.OPEN_OTP_BOTTOM_SHEET
        liveData.postValue(message)
    }

    fun getCacheClientHandshakeDetails() {
        addDisposable(dayCheckDao.isCacheClientHandshake(Prefs.getInt(PrefConstants.CURRENT_TASK))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ clientEntity: CacheClientHandshakeEntity ->
                clientEntity.feedbackStar = clientRating.get().toString()
                clientEntity.updatedDateTime = LocalDateTime.now().toString()

                //UPDATING OPTED QUESTION BY CLIENT
                val optedQuestionList = ArrayList<String>()
                for (questionMO: RatingQuestionsMO in adapter.items) {
                    if (questionMO.isOpted)
                        optedQuestionList.add(questionMO.question!!)
                }
                clientEntity.questions = GsonUtils.toJsonWithoutExopse().toJson(optedQuestionList)
                updateFeedbackInClientHandshake(clientEntity)
            }, { }))
    }

    private fun updateFeedbackInClientHandshake(clientHandshakeEntity: CacheClientHandshakeEntity) {
        addDisposable(dayCheckDao.updateClientHandshakeTaskDetails(clientHandshakeEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                /*message.what = NavigationConstants.OPEN_OTP_BOTTOM_SHEET
                liveData.postValue(message)*/

                val optedQuestions = adapter.getSelectedQuestion()
                clientListener.onFinalSummaryHandshakeView(optedQuestions)

            }, {
                showToast("Error occurred while updating feedback during client handshake")
            }))
    }

    /*private fun getSelectedQuestionOnRating() {
        val optedQuestions = adapter.getSelectedQuestion()
        clientListener.onFinalSummaryHandshakeView(optedQuestions)
    }*/
}