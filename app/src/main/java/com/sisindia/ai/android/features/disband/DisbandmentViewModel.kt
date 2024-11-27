package com.sisindia.ai.android.features.disband

import android.app.Application
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.droidcommons.preference.Prefs
import com.google.gson.Gson
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.commons.SpinnersListener
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.room.dao.AttachmentDao
import com.sisindia.ai.android.room.dao.LookUpDao
import com.sisindia.ai.android.room.dao.SiteDao
import com.sisindia.ai.android.room.dao.TaskDao
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.uimodels.attachments.OtherTaskAttachmentMetaData
import com.sisindia.ai.android.utils.FileUtils
import com.sisindia.ai.android.workers.AttachmentsUploadWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 30-03-2023
 */
class DisbandmentViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    var obsAttachment =
        ObservableField(AttachmentEntity(AttachmentEntity.AttachmentSourceType.DISBANDMENT_IMAGE))
    var obsRecommended = ObservableField("00")
    var obsApproved = ObservableField("00")
    val siteListAdapter = DisbandmentSitesAdapter()
    val dashboardAdapter = DisbandDashboardAdapter()

    //    val filterItems = arrayListOf("This Month", "Last Month")
    val obsHeadTitle = ObservableField("Add Disbandment")
    var obsRemarks = ObservableField("")
    var obsEnteredUnitCode = ObservableField("")
    var obsDate = ObservableField("MM-DD-YY")
    var obsDateForAPI = ObservableField("")
    var obsIsDataAvailable = ObservableBoolean(true)
    var obsReasonList = ObservableField(arrayListOf(""))
    private var selectedItemPosFromSpinner: Int = 0

    @Inject
    lateinit var siteDao: SiteDao

    @Inject
    lateinit var lookUpDao: LookUpDao

    @Inject
    lateinit var taskDao: TaskDao

    @Inject
    lateinit var attachmentDao: AttachmentDao

    /* var filterSelectionListener = SortItemSelectionListener { month ->
 //            getSalesReferenceFromAPI(month)
     }*/

    var listener: SpinnersListener = object : SpinnersListener {
        override fun onSpinnerOptionSelected(pos: Int) {
            selectedItemPosFromSpinner = pos
        }

        override fun onSpinnerOptionSelected(item: Any) {}
    }

    fun getDisbandSitesFromAPI() {
        isLoading.set(View.VISIBLE)
        addDisposable(coreApi.getSiteDisbandmentDetails(Prefs.getInt(PrefConstants.AREA_INSPECTOR_ID))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.set(View.GONE)
                it?.data?.apply {
                    obsRecommended.set(this.requested.toString())
                    obsApproved.set(this.approved.toString())
                    this.siteData?.apply {
                        dashboardAdapter.clearAndSetItems(this)
                    }
                }
            }, { throwable: Throwable? ->
                isLoading.set(View.GONE)
                throwable!!.printStackTrace()
            }))
    }

    fun onViewClicks(view: View) {
        when (view.id) {
            R.id.headerDisbandment -> {
                message.what = NavigationConstants.OPEN_DASH_BOARD_DRAWER
                liveData.postValue(message)
            }

            R.id.addDisbandmentButton -> {
                message.what = NavigationConstants.OPEN_ADD_RECRUITMENT_SHEET
                liveData.postValue(message)
            }

            R.id.siteCodeButton -> {
                onGetSiteDetailsViaCode()
            }

            R.id.disbandDatePicker -> {
                message.what = NavigationConstants.OPEN_DATE_PICKER
                liveData.postValue(message)
            }

            R.id.addDisbandFormButton -> {
                validateUIFields()
            }

            R.id.uploadImagePDF -> {
                message.what = NavigationConstants.ON_CAPTURE_DOCUMENT
                liveData.postValue(message)
            }
        }
    }

    fun initUI() {
        addDisposable(lookUpDao.fetchDisbandmentReason()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list: List<String> ->
                if (list.isNotEmpty()) {
                    val localList = ArrayList<String>()
                    localList.add("Select Reason")
                    localList.addAll(list)
                    obsReasonList.set(localList)
                }
            }) {
                showToast("No disbandment reason found...")
            })
    }

    private fun onGetSiteDetailsViaCode() {
        if (obsEnteredUnitCode.get().toString().isEmpty() ||
            obsEnteredUnitCode.get().toString().trim().isEmpty() ||
            obsEnteredUnitCode.get().toString().length < 11) {
            showToast("Please enter valid site code")
            return
        }

        isLoading.set(View.VISIBLE)
        addDisposable(siteDao.fetchDisbandSites(obsEnteredUnitCode.get().toString())
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list: List<DisbandmentSitesMO> ->
                isLoading.set(View.GONE)
                if (list.isNotEmpty())
                    siteListAdapter.clearAndSetItems(list)
                else
                    showToast("Associated or operational sites not found")
            }) {
                showToast("Sites not found or failed to execute")
            })
    }

    private fun validateUIFields() {
        if (obsEnteredUnitCode.get().isNullOrEmpty())
            showToast("Please enter valid site code")
        else if (obsEnteredUnitCode.get().toString().length < 11)
            showToast("Please enter minimum 11 length of site code")
        else if (!isAnySiteListChecked())
            showToast("Please select minimum 1 site from site list")
        else if (selectedItemPosFromSpinner == 0)
            showToast("Please select reason of disbandment")
        else if (obsDate.get().toString().equals("MM-DD-YY", ignoreCase = true))
            showToast("Please select valid date of disbandment from calendar")
        else if (obsRemarks.get().isNullOrEmpty())
            showToast("Please enter valid remarks")
        else if (obsAttachment.get() == null || obsAttachment.get()!!.localFilePath.isNullOrEmpty())
            showToast("Please click photo")
        else {
            isLoading.set(View.VISIBLE)

            if (siteListAdapter.items.isNotEmpty()) {

                val bodyList = arrayListOf<AddDisbandmentBodyMO>()

                siteListAdapter.items.forEach {
                    val body = AddDisbandmentBodyMO()
                    body.siteId = it.siteId
                    body.disbandmentDate = obsDateForAPI.get()
                    body.disbandmentReason = obsReasonList.get()!![selectedItemPosFromSpinner]
                    body.disbandmentRemarks = obsRemarks.get()
                    bodyList.add(body)
                }

                addDisposable(coreApi.addDisbandmentSite(bodyList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        isLoading.set(View.GONE)

                        updateOtherAttachment(obsAttachment.get()!!)

                        message.what = NavigationConstants.ON_ADDING_DISBANDMENT_SITE
                        liveData.postValue(message)

                        showToast("Disbandment site added successfully")

                    }, { throwable: Throwable? ->
                        isLoading.set(View.GONE)
                        throwable!!.printStackTrace()
                    }))

            }
        }
    }

    private fun isAnySiteListChecked(): Boolean {
        siteListAdapter.items.forEach {
            if (it.isBoxChecked)
                return true
        }
        return false
    }

    private fun updateOtherAttachment(attachment: AttachmentEntity) {
        val siteId = Prefs.getInt(PrefConstants.CURRENT_SITE)
        addDisposable(taskDao.disbandmentAttachmentFile(attachment.attachmentSourceType,
            siteId, Prefs.getInt(PrefConstants.SELECTED_EMPLOYEE_ID).toString(),
            attachment.attachmentGuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                attachment.storagePath = it.storagePath + FileUtils.EXT_JPG
                val metaData = OtherTaskAttachmentMetaData()
                metaData.attachmentTypeId = 1 //for image
                metaData.attachmentSourceTypeId = attachment.attachmentSourceType
                metaData.uuid = attachment.attachmentGuid
                metaData.fileName = it.fileName + FileUtils.EXT_JPG
                metaData.fileSize = attachment.fileSize.toString()
                metaData.storagePath = attachment.storagePath
                metaData.siteId = siteId
                attachment.attachmentMetaData = Gson().toJson(metaData)
                attachment.isDone = true
                addDisposable(attachmentDao.insert(attachment)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Timber.d("Attachment Added")
                        oneTimeWorkerWithNetwork(AttachmentsUploadWorker::class.java)
                    }
                    ) { t: Throwable? ->
                        Timber.e(t)
                        showToast("Unable to process the other task attachment...")
                    })
            }, Timber::e))
    }
}