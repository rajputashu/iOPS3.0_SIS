package com.sisindia.ai.android.features.taskcheck.clienthandshake.sheet

import android.app.Application
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.BaseNetworkResponse
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.models.AddClientApiBodyMO
import com.sisindia.ai.android.room.dao.CustomerContactDao
import com.sisindia.ai.android.room.dao.CustomerSiteContactDao
import com.sisindia.ai.android.room.entities.CustomerContactEntity
import com.sisindia.ai.android.room.entities.CustomerSiteContactEntity
import com.sisindia.ai.android.utils.IopsUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/9/2020.
 */
class AddClientViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var customerContactDao: CustomerContactDao

    @Inject
    lateinit var customerSiteContactDao: CustomerSiteContactDao

    var clientName = ObservableField<String>("")
    var clientDesignation = ObservableField<String>("")
    var clientNumber = ObservableField<String>("")
    var clientEmail = ObservableField<String>("")
    private var customerId: Int = 0

    fun onAddBtnClick(view: View) {
        if (clientName.get().toString().isEmpty() || clientName.get().toString().trim().isEmpty())
            showToast(app.resources.getString(R.string.valid_msg_client_name))
        else if (clientDesignation.get().toString().isEmpty() || clientDesignation.get().toString()
                .trim().isEmpty())
            showToast(app.resources.getString(R.string.valid_msg_client_designation))
        else if (clientNumber.get().toString().isEmpty() || clientNumber.get()
                .toString().length < 10)
            showToast(app.resources.getString(R.string.valid_msg_client_number))
        else if (clientEmail.get().toString().isNotEmpty() &&
            !Patterns.EMAIL_ADDRESS.matcher(clientEmail.get().toString()).matches()) {
            showToast(app.resources.getString(R.string.valid_msg_email))
        } else {
            addDisposable(customerContactDao.fetchCustomerId(Prefs.getInt(PrefConstants.CURRENT_SITE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //                    insertClientContactDetails(it)
                    customerId = it
                    updateAddedClientToServer(it)
                }, { throwable: Throwable? ->
                    this.onError(throwable!!)
                }))

            /* addDisposable(customerContactDao.isMobileNumberExists(clientNumber.get().toString())
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe({ count ->
                     if (count == 0)
                         fetchCustomerId()
                     else
                         showToast(app.resources.getString(R.string.valid_msg_client_number_exist))
                 }, { throwable: Throwable? ->
                     this.onError(throwable!!)
                 }))*/
        }
    }

    /*private fun fetchCustomerId() {
        addDisposable(customerContactDao.fetchCustomerId(Prefs.getInt(PrefConstants.CURRENT_SITE))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                insertClientContactDetails(it)
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))
    }*/

    private fun updateAddedClientToServer(customerId: Int) {
        setIsLoading(true)
        val addClientBody = AddClientApiBodyMO()
        addClientBody.clientFullName = clientName.get().toString()
        addClientBody.designation = clientDesignation.get().toString()
        addClientBody.clientPhoneNo = clientNumber.get().toString()
        addClientBody.clientMobileNo = clientNumber.get().toString()
        addClientBody.clientEmailId = clientEmail.get().toString()
        addClientBody.clientId = customerId
        addClientBody.siteId = Prefs.getInt(PrefConstants.CURRENT_SITE)
        addDisposable(coreApi.addClient(addClientBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onAddClientResponse) {
                setIsLoading(false)
                if (IopsUtil.isInternetAvailable(app))
                    showToast("Internal server error from API")
                else
                    showToast("Internet not available, please try again...")
            })
    }

    private fun onAddClientResponse(response: BaseNetworkResponse) {
        setIsLoading(false)
        if (response.statusCode == 200) {
            //            updateCustomerContactIsSynced() // Removed this method
            insertClientContactDetails() // Add customerId received from API response
        } else
            showToast(response.statusMessage)
    }

    private fun insertClientContactDetails() {
        setIsLoading(true)
        val contactEntity = CustomerContactEntity()
        contactEntity.title = ""
        contactEntity.contactFullName = clientName.get().toString()
        contactEntity.role = clientDesignation.get().toString()
        contactEntity.contactEmailId = clientEmail.get().toString()
        contactEntity.contactPhoneNo = clientNumber.get().toString()
        contactEntity.customerMobileNo = clientNumber.get().toString()
        val dateTime = LocalDateTime.now().toString()
        contactEntity.createdDateTime = dateTime
        contactEntity.updatedDateTime = dateTime
        contactEntity.customerId = customerId
        contactEntity.isActive = true
        //        contactEntity.isSynced = 0
        contactEntity.isSynced = 1
        addDisposable(customerContactDao.insert(contactEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setIsLoading(false)
                insertIntoCustomerSiteContact(it) // Inserting Locally to referenced table
                //                updateAddedClientToServer(it) // Inserting and updating to server
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))
    }

    private fun insertIntoCustomerSiteContact(customerContactId: Long) {
        val customerSiteContactEntity = CustomerSiteContactEntity()
        customerSiteContactEntity.siteId = Prefs.getInt(PrefConstants.CURRENT_SITE)
        customerSiteContactEntity.customerContactId = customerContactId.toInt()
        customerSiteContactEntity.isActive = true

        addDisposable(customerSiteContactDao.insert(customerSiteContactEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //Added this logic : after removing from previous logic
                triggerEventOnAddingClientSuccessfully()
            }, { throwable: Throwable? ->
                this.onError(throwable!!)
            }))
    }

    /* private fun updateCustomerContactIsSynced() {
         addDisposable(customerContactDao.updateCCEIsSyncedValue(generatedCustomerContactId)
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe({ rowId: Int? ->
                 if (rowId!! > 0)
                     triggerEventOnAddingClientSuccessfully()
             }, { throwable: Throwable? ->
                 this.onError(throwable!!)
             }))
     }*/

    private fun onError(throwable: Throwable) {
        setIsLoading(false)
        Toast.makeText(getApplication(), "Error in transaction", Toast.LENGTH_SHORT).show()
        Timber.e(throwable)
    }

    private fun triggerEventOnAddingClientSuccessfully() {
        message.what = NavigationConstants.ON_CLIENT_ADDED_SUCCESSFULLY
        liveData.postValue(message)
    }
}