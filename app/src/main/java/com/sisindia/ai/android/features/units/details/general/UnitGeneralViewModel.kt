package com.sisindia.ai.android.features.units.details.general

import android.app.Application
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.room.dao.CustomerContactDao
import com.sisindia.ai.android.room.dao.SiteDao
import com.sisindia.ai.android.room.entities.CustomerContactEntity
import com.sisindia.ai.android.uimodels.mysite.SiteGeneralMO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 3/20/2020.
 */
class UnitGeneralViewModel @Inject constructor(app: Application) : IopsBaseViewModel(app) {

    val generalClientContact: ClientContactAdapter = ClientContactAdapter()
    val isInformationAvailable: ObservableInt = ObservableInt(GONE)
    var siteCodeUI = ObservableField<String>("NA")
    var siteNameUI = ObservableField<String>("")
    var siteAddressUI = ObservableField<String>("NA")
    var siteBillSubmission = ObservableField<String>("NA")
    var siteBillCollection = ObservableField<String>("NA")
    var siteWageUI = ObservableField<String>("NA")
    var obsSiteGeoCode = ObservableField<String>("")
    var showNavigationIcon = ObservableInt(GONE)

    @Inject
    lateinit var customerContactDao: CustomerContactDao

    @Inject
    lateinit var siteDao: SiteDao

    fun fetchGeneralSiteDetails() {
        setIsLoading(true)
        val selectedSiteId = Prefs.getInt(PrefConstants.CURRENT_SITE)
        addDisposable(siteDao.fetchSiteGeneralDetails(selectedSiteId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ generalMO: SiteGeneralMO ->
                setIsLoading(true)
                generalMO.apply {
                    if (siteCode!!.isNotEmpty()) siteCodeUI.set(siteCode)
                    if (siteName!!.isNotEmpty()) siteNameUI.set(siteName)
                    if (siteAddress != null && (siteAddress!!.isNotEmpty() || siteAddress!!.trim().isNotEmpty()))
                        siteAddressUI.set(siteAddress)
                    if (billSubmission != null && billSubmission!!.isNotEmpty())
                        siteBillSubmission.set(billSubmission)
                    if (billCollection != null && billCollection!!.isNotEmpty())
                        siteBillCollection.set(billCollection)
                    if (wage != null && wage!!.isNotEmpty())
                        siteWageUI.set(wage)
                    if (siteGeoCode != null && siteGeoCode!!.isNotEmpty()) {
                        showNavigationIcon.set(VISIBLE)
                        obsSiteGeoCode.set(siteGeoCode)
                    }
                }
            },
                { throwable: Throwable? ->
                    setIsLoading(false)
                    Timber.e(throwable)
                }))
    }

    fun fetchClientDetails() {
        setIsLoading(true)
        val siteId = Prefs.getInt(PrefConstants.CURRENT_SITE)
        addDisposable(customerContactDao.fetchCustomerInfo(siteId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list: List<CustomerContactEntity> ->
                if (list.isNotEmpty())
                    generalClientContact.updateContactInfoList(list)
                else
                    isInformationAvailable.set(VISIBLE)
            },
                { throwable: Throwable? ->
                    setIsLoading(false)
                    Timber.e(throwable)
                    showToast("Error while fetching client contact details")
                }))
    }
}