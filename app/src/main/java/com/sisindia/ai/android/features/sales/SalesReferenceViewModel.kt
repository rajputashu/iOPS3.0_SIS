package com.sisindia.ai.android.features.sales

import android.app.Application
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.sisindia.ai.android.base.BaseNetworkResponse
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.commons.SpinnersListener
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.features.performance.SortItemSelectionListener
import com.sisindia.ai.android.models.sales.RaisingSiteDetailsMO
import com.sisindia.ai.android.models.sales.SalesAddBodyMO
import com.sisindia.ai.android.models.sales.SalesReferenceMO
import com.sisindia.ai.android.models.sales.SectorMO
import com.sisindia.ai.android.room.dao.SiteDao
import com.sisindia.ai.android.utils.IopsUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/30/2021.
 */
class SalesReferenceViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var siteDao: SiteDao

    var obsRecommended = ObservableField("0")
    var obsRaised = ObservableField("0")
    var obsApproved = ObservableField("0")
    val filterItems = arrayListOf("This Month", "Last Month")

    // Used for Add/Update Sales Reference
    val obsHeadTitle = ObservableField("")
    val obsSectorOrStatusLabel = ObservableField("")
    val obsLeadOrUnitCodeLabel = ObservableField("")
    val obsLeadOrUnitCodeHint = ObservableField("")
    val obsDateLabel = ObservableField("")
    val obsButtonText = ObservableField("")
    var obsClientName = ObservableField("")
    var obsEnteredUnitCode = ObservableField("")
    var obsLeadOrUnitCode = ObservableField("")
    var obsContactPerson = ObservableField("")
    var obsSiteName = ObservableField("")

    //    var obsEnteredUnitCode = MutableLiveData("")
    var obsRecruitDOB = ObservableField("MM-DD-YY")
    var obsDateForAPI = ObservableField("")
    var obsIsDataAvailable = ObservableBoolean(true)
    var obsSectorOrStatusSpinnerList = ObservableField(arrayListOf(""))
    private lateinit var raisingSiteDetailsMO: RaisingSiteDetailsMO

    //    var obsSitesSpinnerList = ObservableField(arrayListOf(""))
    var obsContactsSpinnerList = ObservableField(arrayListOf(""))

    val obsSelectedSalesItemForUpdate = ObservableField<SalesReferenceMO>()
    var obsIsComingToAdd = ObservableBoolean(true)
    var obsIsContactsAvailable = ObservableBoolean(false)
//    var obsIsUnitCodeEnabled = ObservableBoolean(false)

    private var selectedItemPosFromSpinner: Int = 0

    //    private lateinit var siteListFromDB: List<SalesSitesMO>
    private var isRaisedCodeValidationRequested = false
    private var isRaisedCodeValidatedFromAPI = false

    /*private var selectedItemPosOfSites: Int = 0 */
//    private lateinit var contactListFromDB: List<String>

    val salesReferenceAdapter = SalesReferenceAdapter()

    var filterSelectionListener =
        SortItemSelectionListener { month -> getSalesReferenceFromAPI(month) }

    var listener: SpinnersListener = object : SpinnersListener {
        override fun onSpinnerOptionSelected(pos: Int) {
            selectedItemPosFromSpinner = pos
        }

        override fun onSpinnerOptionSelected(item: Any) {}
    }

    lateinit var contactsList: ArrayList<String>
    var contactSpinnerListener: SpinnersListener = object : SpinnersListener {
        override fun onSpinnerOptionSelected(pos: Int) {
            if (pos > 0)
                obsContactPerson.set(obsContactsSpinnerList.get()!![pos])
            else
                obsContactPerson.set("")
        }

        override fun onSpinnerOptionSelected(item: Any) {}
    }

    /*var siteSpinnerListener: SpinnersListener = object : SpinnersListener {
        override fun onSpinnerOptionSelected(pos: Int) {
            if (pos > 0 && !siteListFromDB.isNullOrEmpty()) {
                addDisposable(siteDao.fetchContacts(siteListFromDB[pos - 1].siteId!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (!it.isNullOrEmpty()) {
                            obsIsContactsAvailable.set(true)
//                            contactListFromDB = it
                            contactsList = arrayListOf("Select Contact")
                            contactsList.addAll(it)
                            obsContactsSpinnerList.set(contactsList)
                        } else {
                            obsIsContactsAvailable.set(false)
                            *//*selectedItemPosOfContacts = 0
                            if (!contactsList.isNullOrEmpty())
                                contactsList.clear()
                            //                            contactsList.add("Contacts N.A.")
                            obsContactsSpinnerList.set(arrayListOf("Not Available"))*//*
                        }
                    }, { throwable: Throwable? ->
                        throwable!!.printStackTrace()
                    }))
            }
        }

        override fun onSpinnerOptionSelected(item: Any) {}
    }*/

    val salesListener: SalesListener = object : SalesListener {
        override fun onItemSelected(item: Any) {
            message.what = NavigationConstants.ON_UPDATE_SALES_REFERENCE
            message.obj = item
            liveData.postValue(message)
        }
    }

    fun getSalesReferenceFromAPI(filterMonth: Int) {
        isLoading.set(View.VISIBLE)
        //        addDisposable(coreApi.getSalesReferenceData(LocalDate.now().monthValue, LocalDate.now().year)
        addDisposable(coreApi.getSalesReferenceData(filterMonth, LocalDate.now().year)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.set(View.GONE)
                if (it.statusCode == 200) {
                    it?.apply {
                        data?.apply {
                            obsRecommended.set(data.recommended.toString())
                            obsRaised.set(data.raised.toString())
                            obsApproved.set(data.approved.toString())

                            if (!this.salesRefList.isNullOrEmpty()) {
                                obsIsDataAvailable.set(true)
                                salesReferenceAdapter.clearAndSetItems(this.salesRefList)
                            } else {
                                obsIsDataAvailable.set(false)
                                salesReferenceAdapter.clearAndSetItems(arrayListOf())
                            }
                        }
                    }
                } else
                    showToast(it?.statusMessage)
            }, {
                isLoading.set(View.GONE)
                if (IopsUtil.isInternetAvailable(app))
                    showToast("Internal server error from API")
                else
                    showToast("Internet not available, please try again...")
            }))
    }

    fun initAddOrUpdateUI(isComingToAdd: Boolean) {
        obsIsComingToAdd.set(isComingToAdd)
        if (isComingToAdd) {
            getSectorListFromAPI()
            obsHeadTitle.set("Add Sales Reference")
            obsSectorOrStatusLabel.set("Sector")
            obsLeadOrUnitCodeLabel.set("Lead given to")
            obsLeadOrUnitCodeHint.set("Name of sales person/BH")
            obsDateLabel.set("Date of recommendation")
            obsButtonText.set("Add")

            /*addDisposable(siteDao.fetchSitesWithCode()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (!it.isNullOrEmpty()) {
                        siteListFromDB = it
                        val siteArray = arrayListOf("Select Site")
                        for (siteMO: SalesSitesMO in it) {
                            siteArray.add(siteMO.siteName!! + " (" + siteMO.siteCode + ")")
                        }
                        obsSitesSpinnerList.set(siteArray)
                    }
                }, { throwable: Throwable? ->
                    throwable!!.printStackTrace()
                }))*/

        } else {
            obsSectorOrStatusSpinnerList.set(arrayListOf("Select Status", "Recommended",
                "Raised", "Approved"))
            obsHeadTitle.set("Update Status of sales reference")
            obsSectorOrStatusLabel.set("Status")
            obsLeadOrUnitCodeLabel.set("Newly Raised Unit Code")
            obsLeadOrUnitCodeHint.set("Enter unit code")
            obsDateLabel.set("Date of raising")
            obsButtonText.set("Update")
            if (obsSelectedSalesItemForUpdate.get() != null) {
                obsClientName.set(obsSelectedSalesItemForUpdate.get()!!.name)
                val statusValue: String
                when (obsSelectedSalesItemForUpdate.get()!!.status) {
                    1 -> {
                        statusValue = "Recommended"
                        obsSectorOrStatusSpinnerList.set(arrayListOf("Select Status",
                            "Recommended", "Raised"))
//                        obsLeadOrUnitCode.set("N.A.")
                    }
                    2 -> {
                        statusValue = "Raised"
                        obsSectorOrStatusSpinnerList.set(arrayListOf("Select Status",
                            "Raised", "Approved"))
                        obsLeadOrUnitCode.set(obsSelectedSalesItemForUpdate.get()!!.raisingCode)
//                        obsIsUnitCodeEnabled.set(true)
                    }
                    else -> {
                        statusValue = "Approved"
                        obsSectorOrStatusSpinnerList.set(arrayListOf("Select Status", "Approved"))
                        obsLeadOrUnitCode.set(obsSelectedSalesItemForUpdate.get()!!.raisingCode)
//                        obsIsUnitCodeEnabled.set(true)
                    }
                }
                message.what = NavigationConstants.ON_UPDATING_SPINNER_POSITION
                selectedItemPosFromSpinner =
                    obsSectorOrStatusSpinnerList.get()?.indexOf(statusValue)!!
                message.arg1 = obsSectorOrStatusSpinnerList.get()?.indexOf(statusValue)!!
                liveData.postValue(message)
            }
        }
    }

    private fun getSectorListFromAPI() {
        isLoading.set(View.VISIBLE)
        addDisposable(coreApi.salesRefSectors
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.set(View.GONE)
                if (it.statusCode == 200) {
                    val modifiedList = arrayListOf("Select Sector")
                    it.sectorList?.apply {
                        for (sectorMo: SectorMO in this) {
                            modifiedList.add(sectorMo.sectorName!!)
                        }
                    }
                    obsSectorOrStatusSpinnerList.set(modifiedList)
                } else
                    showToast(it?.statusMessage)
            }, {
                isLoading.set(View.GONE)
                showToast("Internet not available, please try again...")
            }))
    }

    fun ivRotaDrawerClick(view: View) {
        message.what = NavigationConstants.OPEN_DASH_BOARD_DRAWER
        liveData.postValue(message)
    }

    fun onAddSalesReference(view: View) {
        message.what = NavigationConstants.ON_ADD_SALES_REFERENCE
        liveData.postValue(message)
    }

    fun onGetSiteDetailsViaCode(view: View) {
        if (obsEnteredUnitCode.get().toString().isEmpty() ||
            obsEnteredUnitCode.get().toString().trim().isEmpty() ||
            obsEnteredUnitCode.get().toString().length < 11) {
            showToast("Please enter valid site code")
            return
        }

        isLoading.set(View.VISIBLE)
        addDisposable(coreApi.getSitesViaSiteCode(obsEnteredUnitCode.get().toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.set(View.GONE)
                if (it.statusCode == 200) {
                    raisingSiteDetailsMO = it.siteDetail!!
                    obsSiteName.set(it.siteDetail.siteName)
                    fetchContactsFromDBViaSiteId(it.siteDetail.siteId!!)
                } else
                    showToast(it?.statusMessage)
            }, {
                isLoading.set(View.GONE)
                showToast("Internet not available, please try again...")
            }))
    }

    fun onValidatingRaisedCode(view: View) {
        if (obsLeadOrUnitCode.get().toString().isEmpty() ||
            obsLeadOrUnitCode.get().toString().trim().isEmpty() ||
            obsLeadOrUnitCode.get().toString().length < 11) {
            showToast("Please enter valid site raised code")
            return
        }

        isLoading.set(View.VISIBLE)
        addDisposable(coreApi.validateRaisedCode(obsLeadOrUnitCode.get().toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.set(View.GONE)
                if (it.statusCode == 200) {
                    isRaisedCodeValidationRequested = true
                    isRaisedCodeValidatedFromAPI = true
                    showToast("Newly raised unit code is valid")
                } else
                    showToast(it?.statusMessage)
            }, {
                isLoading.set(View.GONE)
                showToast("Internet not available, please try again...")
            }))
    }

    private fun fetchContactsFromDBViaSiteId(siteId: Int) {
        addDisposable(siteDao.fetchContacts(siteId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (!it.isNullOrEmpty()) {
                    obsIsContactsAvailable.set(true)
                    contactsList = arrayListOf("Select Contact")
                    contactsList.addAll(it)
                    obsContactsSpinnerList.set(contactsList)
                } else {
                    obsIsContactsAvailable.set(false)
                }
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
    }

    fun onDatePickerClicked(view: View) {
        message.what = NavigationConstants.OPEN_DATE_PICKER
        liveData.postValue(message)
    }

    fun onAddBtnClick(view: View) {
        if (obsClientName.get().toString().isEmpty() ||
            obsClientName.get().toString().trim().isEmpty())
            showToast("Please enter valid client name")
        else if (selectedItemPosFromSpinner == 0 && obsIsComingToAdd.get())
            showToast("Please select valid sector")
        else if (obsEnteredUnitCode.get().isNullOrEmpty() && obsIsComingToAdd.get())
            showToast("Please enter valid site code")
        else if (obsIsComingToAdd.get() && obsSiteName.get().isNullOrEmpty())
            showToast("Please valid site code to fetch site name")
        else if (obsContactPerson.get().isNullOrEmpty() && obsIsComingToAdd.get())
            showToast("Please enter valid contact person name")
        else if (selectedItemPosFromSpinner == 0 && !obsIsComingToAdd.get())
            showToast("Please select valid status")
        else if (obsIsComingToAdd.get() && (obsLeadOrUnitCode.get().toString().isEmpty() ||
                    obsLeadOrUnitCode.get().toString().trim().isEmpty()))
            showToast("Please enter valid name of sales person/BH")
        else if (!obsIsComingToAdd.get() && (obsLeadOrUnitCode.get().toString().isEmpty() ||
                    obsLeadOrUnitCode.get().toString().trim().isEmpty()))
            showToast("Please enter valid newly raising code")
        else if (!obsIsComingToAdd.get() && !isRaisedCodeValidationRequested)
            showToast("Please click on 'validate' to validate newly raised code")
        else if (!obsIsComingToAdd.get() && isRaisedCodeValidationRequested && !isRaisedCodeValidatedFromAPI)
            showToast("Entered newly raised unit code is not valid")
        else if (obsRecruitDOB.get().toString().equals("MM-DD-YY", ignoreCase = true))
            showToast("Please select valid date of recommendation from calendar")
        else
            addOrUpdateSalesReference()
    }

    private fun addOrUpdateSalesReference() {
        isLoading.set(View.VISIBLE)
//        val salesUpdateRefBody = SalesUpdateBodyMO()
        val apiCall: Single<BaseNetworkResponse>
        if (obsIsComingToAdd.get()) {
            val salesAddRefBody = SalesAddBodyMO()
            salesAddRefBody.name = obsClientName.get().toString()
            salesAddRefBody.sector =
                obsSectorOrStatusSpinnerList.get()!![selectedItemPosFromSpinner]
            salesAddRefBody.leadGivenTo = obsLeadOrUnitCode.get().toString()
            salesAddRefBody.dateOfRecommendation = obsDateForAPI.get().toString()
            salesAddRefBody.dateOfReporting = obsDateForAPI.get().toString()
            if (::raisingSiteDetailsMO.isInitialized) {
                salesAddRefBody.siteId = raisingSiteDetailsMO.siteId
                salesAddRefBody.siteCode = raisingSiteDetailsMO.siteCode
                salesAddRefBody.siteName = raisingSiteDetailsMO.siteName
            }
            salesAddRefBody.employeeName = obsContactPerson.get().toString()
            salesAddRefBody.referBy = obsContactPerson.get().toString()
            apiCall = coreApi.addSalesReference(salesAddRefBody)
            /*if (!siteListFromDB.isNullOrEmpty() && selectedItemPosOfSites > 0) {
                salesRefBody.siteId = siteListFromDB[selectedItemPosOfSites - 1].siteId
                salesRefBody.siteCode = siteListFromDB[selectedItemPosOfSites - 1].siteCode
                salesRefBody.siteName = siteListFromDB[selectedItemPosOfSites - 1].siteName
            }
            if (!contactsList.isNullOrEmpty() && selectedItemPosOfContacts > 0)
                salesRefBody.employeeName = contactsList[selectedItemPosOfContacts]*/

        } else {
//            salesUpdateRefBody.name = obsClientName.get().toString()
//            salesUpdateRefBody.sector = obsSectorOrStatusSpinnerList.get()!![selectedItemPosFromSpinner]
//            salesUpdateRefBody.siteCode = obsLeadOrUnitCode.get().toString()

            /*salesUpdateRefBody.id = obsSelectedSalesItemForUpdate.get()?.id
            salesUpdateRefBody.status = selectedItemPosFromSpinner
            salesUpdateRefBody.raisedUnitCode = obsLeadOrUnitCode.get().toString()*/
            apiCall = coreApi.updateSalesReference(obsSelectedSalesItemForUpdate.get()?.id!!,
                selectedItemPosFromSpinner, obsLeadOrUnitCode.get().toString())
        }

//        val apiCall = if (obsIsComingToAdd.get()) coreApi.addSalesReference(salesAddRefBody) else
//            coreApi.updateSalesReference(salesUpdateRefBody)

        addDisposable(apiCall
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.set(View.GONE)
                if (it.statusCode == 200) {
                    showToast("Sales Reference Added/Updated Successfully")
                    message.what = NavigationConstants.ON_SALES_REF_ADDED_UPDATED_SUCCESSFULLY
                    liveData.postValue(message)
                } else
                    showToast(it?.statusMessage)
            }, {
                isLoading.set(View.GONE)
                showToast("Internet not available, please try again...")
            }))
    }
}