package com.sisindia.ai.android.features.taskcheck.clienthandshake.frags

import android.app.Application
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.features.taskcheck.clienthandshake.ClientHandshakeListener
import com.sisindia.ai.android.features.taskcheck.clienthandshake.adapters.HandshakeClientsAdapter
import com.sisindia.ai.android.room.dao.CustomerContactDao
import com.sisindia.ai.android.room.dao.DayCheckDao
import com.sisindia.ai.android.room.entities.CacheClientHandshakeEntity
import com.sisindia.ai.android.room.entities.CustomerContactEntity
import com.sisindia.ai.android.uimodels.tasks.ClientDetailsResponseMO
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/8/2020.
 */
class HandshakeFragViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var customerContactDao: CustomerContactDao

    @Inject
    lateinit var dayCheckDao: DayCheckDao

    lateinit var tabLists: List<TextView>
    var detailLayoutVisibility = ObservableInt(GONE)
    var isInformationAvailable = ObservableInt(GONE)
    var showEditButton = ObservableInt(GONE)
    var isYesBtnEnabled = ObservableField<Boolean>()
    val adapter = HandshakeClientsAdapter()
    var reasonNotMetClient = ObservableField("")
    private var isListLoadedWithData: Boolean = false
    lateinit var customerContactList: List<CustomerContactEntity>
    lateinit var clientHandshakeEntity: CacheClientHandshakeEntity
    var selectedClient = ObservableField(CustomerContactEntity())
    val companyId = Prefs.getInt(PrefConstants.COMPANY_ID, 1)

    val clientListener = object : ClientHandshakeListener {

        /*override fun onClientSelected(clientId: Int, position: Int) {
            Prefs.putInt(SELECTED_CLIENT_ID, clientId)
            selectedClientPosition = position
            isTaskBtnEnabled.set(true)
        }*/

        override fun onClientSelected(client: CustomerContactEntity) {
            Prefs.putInt(PrefConstants.SELECTED_CLIENT_ID, client.id)
            Prefs.putInt(PrefConstants.SELECTED_CLIENT_CUSTOMER_ID, client.customerId)
            selectedClient.set(client)
        }

        override fun onContinueAddFeedbackClicked() {
            message.what = NavigationConstants.ON_CONTINUE_ADD_FEEDBACK
            liveData.postValue(message)
        }

        override fun onFinalSummaryHandshakeView(optedQuestions: String) {
        }

        override fun onCompleteClientHandshake() {
            message.what = NavigationConstants.ON_COMPLETE_CLIENT_HANDSHAKE
            liveData.postValue(message)
        }
    }

    fun isClientHandshakeTaskAlreadyStarted() {
        addDisposable(dayCheckDao.isCacheClientHandshake(Prefs.getInt(PrefConstants.CURRENT_TASK))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ clientEntity: CacheClientHandshakeEntity ->
                clientHandshakeEntity = clientEntity
            }, { insertClientHandshakeTaskToCache() }))
    }

    private fun insertClientHandshakeTaskToCache() {
        addDisposable(dayCheckDao.insertClientHandshake(CacheClientHandshakeEntity(Prefs.getInt(
            PrefConstants.CURRENT_TASK)))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ isClientHandshakeTaskAlreadyStarted() }, {}))
    }

    fun fetchClientDetails() {
        setIsLoading(true)
        val siteId = Prefs.getInt(PrefConstants.CURRENT_SITE)
        addDisposable(customerContactDao.fetchCustomerInfo(siteId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list: List<CustomerContactEntity> ->
                setIsLoading(false)
                if (list.isNotEmpty()) {
                    customerContactList = list
                    isListLoadedWithData = true
                    adapter.clearAndSetItems(list)
                    isInformationAvailable.set(GONE)
                } else {
                    isInformationAvailable.set(VISIBLE)
                    isListLoadedWithData = false
                }
            }, { throwable: Throwable? ->
                setIsLoading(false)
                Timber.e(throwable)
            }))
    }

    fun onYesButtonClick(view: View) {
        if (isListLoadedWithData)
            isInformationAvailable.set(GONE)
        else
            isInformationAvailable.set(VISIBLE)
        detailLayoutVisibility.set(VISIBLE)
        isYesBtnEnabled.set(true)
        if (companyId != 1)
            showEditButton.set(VISIBLE)
        updateSelectedTV(view)
    }

    fun onNoButtonClick(view: View) {
        detailLayoutVisibility.set(VISIBLE)
        isYesBtnEnabled.set(false)
        updateSelectedTV(view)
        callToOpenReasonBottomSheet()
        isInformationAvailable.set(GONE)

        if (companyId != 1) {
            if (reasonNotMetClient.get().toString().isEmpty())
                showEditButton.set(GONE)
            else
                showEditButton.set(VISIBLE)
        }
    }

    fun onAddEditClient(view: View) {
        when (view.id) {
            R.id.refreshClientButton -> {
                getClientListFromApi()
            }
            R.id.editOrAddClientButton -> {
                if (isYesBtnEnabled.get()!!) {
                    message.what = NavigationConstants.OPEN_ADD_CLIENT_BOTTOM_SHEET
                    liveData.postValue(message)
                } else
                    callToOpenReasonBottomSheet()
            }
        }
    }

    private fun getClientListFromApi() {
        /*addDisposable(coreApi.clientDetails
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (!it.data?.isEmpty()!!)
                    updateCustomerContactTable(it.data)
                else
                    showToast(it.statusMessage)
            }, this::onApiError))*/

        setIsLoading(true)
        val siteId = Prefs.getInt(PrefConstants.CURRENT_SITE)
        addDisposable(Single.zip(customerContactDao.fetchAllClientListIds(siteId),
            coreApi.getClientDetails(siteId),
            BiFunction<List<Int>, ClientDetailsResponseMO, Boolean> { listIds, clientResponse ->
                return@BiFunction onResultFetch(listIds, clientResponse)
            })
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ }, {
                setIsLoading(false)
                Timber.e("Something is fail via api")
            }))
    }

    private fun onResultFetch(ids: List<Int>, clientResponse: ClientDetailsResponseMO): Boolean {
        var isClientInserted = false
        if (clientResponse.statusCode == 200 && !clientResponse.data.isNullOrEmpty()) {
            setIsLoading(false)
            if (ids.isEmpty()) {
                isClientInserted = true
                updateCustomerContactTable(clientResponse.data)
            } else {
                val localList = arrayListOf<CustomerContactEntity>()
                for (task in clientResponse.data) {
                    val index = ids.indexOfFirst { localId -> localId == task.id }
                    if (index == -1)
                        localList.add(task)
                }
                if (localList.isNotEmpty()) {
                    isClientInserted = true
                    updateCustomerContactTable(localList)
                }
            }
        }
        return isClientInserted
    }

    private fun updateCustomerContactTable(list: List<CustomerContactEntity>) {
        addDisposable(customerContactDao.insertAll(list)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ rowId ->
                Timber.e("Client added to list $rowId")
                //Refresh list again
                fetchClientDetails()
                showToast("Client list updated successfully")
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
    }

    private fun callToOpenReasonBottomSheet() {
        message.what = NavigationConstants.OPEN_BOTTOM_SHEET
        liveData.postValue(message)
    }

    fun onTaskCompleteBtnClick(view: View) {
        if (isYesBtnEnabled.get()!!) {
            val model = selectedClient.get()
            if (model != null) {
                clientHandshakeEntity.clientEmail = model.contactEmailId
                clientHandshakeEntity.clientName = model.contactFullName
                clientHandshakeEntity.clientId = model.customerId.toString()
                clientHandshakeEntity.contactNo = model.contactPhoneNo
                clientHandshakeEntity.designation = model.role

                clientHandshakeEntity.isMetClient = true
                clientHandshakeEntity.updatedDateTime = LocalDateTime.now().toString()
                addDisposable(dayCheckDao.updateClientHandshakeTaskDetails(clientHandshakeEntity)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ clientListener.onContinueAddFeedbackClicked() }, {}))
            }
        } else {
            //Adding captured data to local DB
            addDisposable(dayCheckDao.updateClientHandshakeTaskDetails(Prefs.getInt(PrefConstants.CURRENT_TASK),
                reasonNotMetClient.get().toString(), LocalDateTime.now().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ clientListener.onCompleteClientHandshake() }, {}))
        }
    }

    private fun updateSelectedTV(tabTextView: View) {
        for (tabView: TextView in tabLists) {
            if (tabView.id == tabTextView.id) {
                tabView.apply {
                    setTextColor(ContextCompat.getColor(app, R.color.colorWhite))
                    background = ContextCompat.getDrawable(app, R.drawable.border_red_body)
                }
            } else {
                tabView.apply {
                    setTextColor(ContextCompat.getColor(app, R.color.textColorBlackBold))
                    background = ContextCompat.getDrawable(app, R.drawable.border)
                }
            }
        }
    }
}