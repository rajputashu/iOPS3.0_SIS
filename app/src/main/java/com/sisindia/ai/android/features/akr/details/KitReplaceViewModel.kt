package com.sisindia.ai.android.features.akr.details

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.CompoundButton
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.droidcommons.preference.Prefs
import com.google.gson.Gson
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.commons.KitOTP
import com.sisindia.ai.android.commons.SpinnersListener
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.features.akr.adapters.KitItemsAdapter
import com.sisindia.ai.android.room.dao.AttachmentDao
import com.sisindia.ai.android.room.dao.GuardKitRequestDao
import com.sisindia.ai.android.room.dao.KitItemDao
import com.sisindia.ai.android.room.dao.LookUpDao
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.room.entities.AttachmentEntity.AttachmentSourceType
import com.sisindia.ai.android.room.entities.LookUpEntity
import com.sisindia.ai.android.uimodels.akr.AKRAttachmentMetaDataMo
import com.sisindia.ai.android.uimodels.akr.KitToReplaceItemsMO
import com.sisindia.ai.android.utils.FileUtils
import com.sisindia.ai.android.workers.AttachmentsUploadWorker
import com.sisindia.ai.android.workers.KitDistributionWorker
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/17/2020.
 */
class KitReplaceViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var guardKitRequestDao: GuardKitRequestDao

    @Inject
    lateinit var kitItemsDao: KitItemDao

    @Inject
    lateinit var attachmentDao: AttachmentDao

    @Inject
    lateinit var lookUpDao: LookUpDao

    var spannedGuardName = ObservableField(spannedString("", "Guard Name"))
    var spannedUnitName = ObservableField(spannedString("", "Unit Name"))

    var signAttachmentObs =
        ObservableField(AttachmentEntity(AttachmentSourceType.KIT_DISTRIBUTE_SIGNATURE))
    var photoAttachmentObs =
        ObservableField(AttachmentEntity(AttachmentSourceType.KIT_DISTRIBUTE_PHOTO))
    var voucherPhotoAttachmentObs =
        ObservableField(AttachmentEntity(AttachmentSourceType.KIT_VOUCHER_PHOTO))

    var kitItemsAdapter = KitItemsAdapter()
    val obsIsUnPaid = ObservableBoolean(false)
    var nonIssueReasonId: Int = 0
    private lateinit var nonIssueReasonList: List<LookUpEntity>
    private var selectedKitId = 0
    var selectedOtpTypeId = 1
    var guardMobileNumber = ""
    val obsIsDataAvailable = ObservableBoolean(false)
    private var selectedNonIssueReasonPos = 0

    private fun spannedString(value: String, label: String): String {
        return app.resources.getString(R.string.dynamic_string2, value, label)
    }

    val unPaidReasonsList = arrayListOf("Select Non Issue Reason")
    val spinnersListener = object : SpinnersListener {
        override fun onSpinnerOptionSelected(pos: Int) {
            selectedNonIssueReasonPos = pos
            if (pos > 0 && ::nonIssueReasonList.isInitialized) {
                nonIssueReasonId = nonIssueReasonList[pos - 1].lookupIdentifier
            }
        }

        override fun onSpinnerOptionSelected(item: Any) {
        }
    }

    fun onUpPaidSwitch(view: CompoundButton, isChecked: Boolean) {
        obsIsUnPaid.set(isChecked)
    }

    fun fetchReasonsAndKitItemsToDistribute() {
//        val ids = intArrayOf(1, 2, 3, 4, 5, 6, 7)
        isLoading.set(View.VISIBLE)
        addDisposable(Single.zip(guardKitRequestDao.fetchKitToReplaceDetails(Prefs.getInt(
            PrefConstants.AKR_SITE_ID), Prefs.getInt(PrefConstants.AKR_GUARD_ID),
            Prefs.getString(PrefConstants.AKR_KIT_ISSUE_NO)),
            lookUpDao.fetchAllReasons(62),
            BiFunction<List<KitToReplaceItemsMO>, List<LookUpEntity>, List<KitToReplaceItemsMO>> { kitItemsList, reasonsList ->
                return@BiFunction onResultFetch(kitItemsList, reasonsList)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.set(View.GONE)
                if (!it.isNullOrEmpty()) {
                    obsIsDataAvailable.set(true)
                    kitItemsAdapter.clearAndSetItems(it)
                } else
                    obsIsDataAvailable.set(false)
            }, {
                isLoading.set(View.GONE)
                it.printStackTrace()
            }))

        /*addDisposable(guardKitRequestDao.fetchKitToReplaceDetails(Prefs.getInt(PrefConstants.AKR_SITE_ID),
            Prefs.getInt(PrefConstants.AKR_GUARD_ID))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ kitItemsList ->
                isLoading.set(View.GONE)
                if (!kitItemsList.isNullOrEmpty()) {
                    kitItemsList[0]!!.apply {
                        spannedGuardName.set(spannedString(guardName.toString(), "Guard Name"))
                        spannedUnitName.set(spannedString(siteName.toString(), "Unit Name"))
                    }
                    kitItemsAdapter.clearAndSetItems(kitItemsList)
                }
            }, { throwable: Throwable? ->
                isLoading.set(View.GONE)
                Timber.e(throwable)
            }))*/
    }

    private fun onResultFetch(kitItemsList: List<KitToReplaceItemsMO>,
                              reasonList: List<LookUpEntity>): List<KitToReplaceItemsMO> {
        if (kitItemsList.isNotEmpty()) {
            kitItemsList[0].apply {
                selectedKitId = this.id
                selectedOtpTypeId = if (this.otpTypeId == null) 1 else this.otpTypeId!!
                guardMobileNumber = this.recipientPhoneNo!!
                spannedGuardName.set(spannedString(guardName.toString(), "Guard Name"))
                spannedUnitName.set(spannedString(siteName.toString(), "Unit Name"))
            }
        }

        if (reasonList.isNotEmpty()) {
            nonIssueReasonList = reasonList
            for (lookUpEntity: LookUpEntity in reasonList) {
                unPaidReasonsList.add(lookUpEntity.displayName)
            }
        }
        return kitItemsList
    }

    fun onClickViews(view: View) {
        when (view.id) {
            R.id.takeGuardPhotoLayout -> {
                val photoAttachment = photoAttachmentObs.get()
                message.what = NavigationConstants.ON_TAKE_PICTURE
                message.obj = photoAttachment
                liveData.postValue(message)
            }

            R.id.takeGuardSignatureLayout -> {
                val signAttachment = signAttachmentObs.get()
                message.what = NavigationConstants.ON_TAKE_SIGNATURE
                message.obj = signAttachment
                liveData.postValue(message)
            }

            R.id.takeVoucherPhotoLayout -> {
                val signAttachment = voucherPhotoAttachmentObs.get()
                message.what = NavigationConstants.ON_KIT_VOUCHER_CLICK
                message.obj = signAttachment
                liveData.postValue(message)
            }

            R.id.onKitDistributionDone -> {
                if (obsIsUnPaid.get()) {
                    if (selectedNonIssueReasonPos == 0)
                        showToast("Please select valid non issue reason")
                    else
                        updateUnPaidKitDistributionItems()
                } else {
                    if (!validateItemsToReplaceSelected()) {
                        showToast("Select at least one item to replace")
                        return
                    }
                    val photoAttach = photoAttachmentObs.get()
                    if (photoAttach == null || TextUtils.isEmpty(photoAttach.localFilePath)) {
                        showToast("Its mandatory to take photo of guard")
                        return
                    }
                    val signAttach = signAttachmentObs.get()
                    if (signAttach == null || TextUtils.isEmpty(signAttach.localFilePath)) {
                        showToast("Its mandatory to take signature of guard")
                        return
                    }
                    val voucherAttach = voucherPhotoAttachmentObs.get()
                    if (voucherAttach == null || TextUtils.isEmpty(voucherAttach.localFilePath)) {
                        showToast("Its mandatory to take photo of voucher")
                        return
                    }
                    requestToOpenOtpSheetOrNot()
                }
            }
        }
    }

    private fun requestToOpenOtpSheetOrNot() {

        when (selectedOtpTypeId) {
            KitOTP.OTP_NOT_REQUIRED.kitOtpType -> {
                updateKitDistributionItems()
            }

            KitOTP.VERIFY_SKIP_OTP.kitOtpType -> {
                message.what = NavigationConstants.ON_KIT_OTP_REQUEST
                liveData.postValue(message)
            }

            KitOTP.MANDATORY_OTP.kitOtpType -> {
                message.what = NavigationConstants.ON_KIT_OTP_REQUEST
                liveData.postValue(message)
            }
        }
    }

    private fun validateItemsToReplaceSelected(): Boolean {
        for (kitItems: KitToReplaceItemsMO in kitItemsAdapter.items) {
            if (kitItems.isSelected)
                return true
        }
        return false
    }

    private fun updateUnPaidKitDistributionItems() {
        isLoading.set(View.VISIBLE)
        val kitIdList = arrayListOf<Int>()
        for (kitItems: KitToReplaceItemsMO in kitItemsAdapter.items) {
            kitIdList.add(kitItems.kitId!!)
        }
        val updatedDateTime = LocalDateTime.now().toString()
        addDisposable(kitItemsDao.updateUnPaidKitItems(updatedDateTime, nonIssueReasonId, kitIdList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                updateKitDistributionListStatus(updatedDateTime, 5)
                syncDistributedKitsToServer(true)
                isLoading.set(View.GONE)
                message.what = NavigationConstants.ON_KIT_REPLACE_DONE
                liveData.postValue(message)
            }, { throwable: Throwable? ->
                isLoading.set(View.GONE)
                Timber.e(throwable)
            }))
    }

    fun updateKitDistributionItems() {
        isLoading.set(View.VISIBLE)
        val kitIdList = arrayListOf<Int>()
        val completeItemsListCount = kitItemsAdapter.items.size
        for (kitItems: KitToReplaceItemsMO in kitItemsAdapter.items) {
            if (kitItems.isSelected)
                kitIdList.add(kitItems.kitId!!)
        }
        val updatedDateTime = LocalDateTime.now().toString()
        addDisposable(kitItemsDao.updateKitDistributedItems(updatedDateTime, kitIdList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.set(View.GONE)
                if (completeItemsListCount == kitIdList.size) {
                    updateKitDistributionListStatus(updatedDateTime, 3)
                    syncDistributedKitsToServer(true)
                } else {
                    updateKitDistributionListStatus(updatedDateTime, 3)
                    syncDistributedKitsToServer(false)
                }

                createMetaDataForAttachments()

            }, { throwable: Throwable? ->
                isLoading.set(View.GONE)
                Timber.e(throwable)
            }))
    }

    private fun updateKitDistributionListStatus(updatedDateTime: String, statusCode: Int) {
        addDisposable(kitItemsDao.updateDistributedKitStatus(updatedDateTime,
            kitItemsAdapter.items[0].id, statusCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.e("Updated Kit List Status : If all Kit Items are distributed")
            }, { throwable: Throwable? ->
                Timber.e(throwable)
            }))
    }

    private fun syncDistributedKitsToServer(isAllItemsDistributed: Boolean) {
        val data = Data.Builder()
        data.putBoolean(IntentConstants.IS_ALL_KIT_ITEMS_DISTRIBUTED, isAllItemsDistributed)
        val request = OneTimeWorkRequest.Builder(KitDistributionWorker::class.java)
            .setInputData(data.build()).build()
        WorkManager.getInstance(getApplication()).enqueue(request)
    }

    private fun createMetaDataForAttachments() {
        val signatureAttachMO = signAttachmentObs.get()
        val photoAttachMO = photoAttachmentObs.get()
        val voucherAttachMO = voucherPhotoAttachmentObs.get()
        val attachmentsList = arrayListOf<AttachmentEntity>()

        if (signatureAttachMO != null && !signatureAttachMO.localFilePath.isNullOrEmpty()) {
            addDisposable(guardKitRequestDao
                .distributedKitSignatureFile(signatureAttachMO.attachmentSourceType,
                    Prefs.getInt(PrefConstants.AKR_SITE_ID), selectedKitId,
                    Prefs.getInt(PrefConstants.AKR_GUARD_ID), signatureAttachMO.attachmentGuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("Attachment Added")
                    it?.apply {
                        signatureAttachMO.storagePath = this.storagePath + FileUtils.EXT_JPG
                        val metaData = AKRAttachmentMetaDataMo()
                        metaData.attachmentTypeId = 1 //for image
                        metaData.kitId = selectedKitId
                        metaData.employeeId = Prefs.getInt(PrefConstants.AKR_GUARD_ID)
                        metaData.siteId = Prefs.getInt(PrefConstants.AKR_SITE_ID)
                        metaData.attachmentSourceTypeId = signatureAttachMO.attachmentSourceType
                        metaData.uuid = signatureAttachMO.attachmentGuid
                        metaData.fileName = this.fileName + FileUtils.EXT_JPG
                        metaData.fileSize = signatureAttachMO.fileSize.toString()
                        metaData.storagePath = signatureAttachMO.storagePath

                        signatureAttachMO.attachmentMetaData = Gson().toJson(metaData)
                        signatureAttachMO.isDone = true
                        attachmentsList.add(signatureAttachMO)
                    }

                }) { t: Throwable? ->
                    Timber.e(t)
                    showToast("Unable to process the other task attachment...")
                })
        }

        if (photoAttachMO != null && !photoAttachMO.localFilePath.isNullOrEmpty()) {
            addDisposable(guardKitRequestDao
                .distributedKitSignatureFile(photoAttachMO.attachmentSourceType,
                    Prefs.getInt(PrefConstants.AKR_SITE_ID), selectedKitId,
                    Prefs.getInt(PrefConstants.AKR_GUARD_ID), photoAttachMO.attachmentGuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it?.apply {
                        photoAttachMO.storagePath = this.storagePath + FileUtils.EXT_JPG
                        val metaData = AKRAttachmentMetaDataMo()
                        metaData.attachmentTypeId = 1 //for image
                        metaData.kitId = selectedKitId
                        metaData.employeeId = Prefs.getInt(PrefConstants.AKR_GUARD_ID)
                        metaData.siteId = Prefs.getInt(PrefConstants.AKR_SITE_ID)
                        metaData.attachmentSourceTypeId = photoAttachMO.attachmentSourceType
                        metaData.uuid = photoAttachMO.attachmentGuid
                        metaData.fileName = this.fileName + FileUtils.EXT_JPG
                        metaData.fileSize = photoAttachMO.fileSize.toString()
                        metaData.storagePath = photoAttachMO.storagePath

                        photoAttachMO.attachmentMetaData = Gson().toJson(metaData)
                        photoAttachMO.isDone = true
                        attachmentsList.add(photoAttachMO)
                    }
                }) { t: Throwable? ->
                    Timber.e(t)
                    showToast("Unable to process the other task attachment...")
                })
        }

        if (voucherAttachMO != null && !voucherAttachMO.localFilePath.isNullOrEmpty()) {
            addDisposable(guardKitRequestDao
                .distributedKitSignatureFile(voucherAttachMO.attachmentSourceType,
                    Prefs.getInt(PrefConstants.AKR_SITE_ID), selectedKitId,
                    Prefs.getInt(PrefConstants.AKR_GUARD_ID), voucherAttachMO.attachmentGuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("Attachment Added")
                    it?.apply {
                        voucherAttachMO.storagePath = this.storagePath + FileUtils.EXT_JPG
                        val metaData = AKRAttachmentMetaDataMo()
                        metaData.attachmentTypeId = 1 //for image
                        metaData.kitId = selectedKitId
                        metaData.employeeId = Prefs.getInt(PrefConstants.AKR_GUARD_ID)
                        metaData.siteId = Prefs.getInt(PrefConstants.AKR_SITE_ID)
                        metaData.attachmentSourceTypeId = voucherAttachMO.attachmentSourceType
                        metaData.uuid = voucherAttachMO.attachmentGuid
                        metaData.fileName = this.fileName + FileUtils.EXT_JPG
                        metaData.fileSize = voucherAttachMO.fileSize.toString()
                        metaData.storagePath = voucherAttachMO.storagePath

                        voucherAttachMO.attachmentMetaData = Gson().toJson(metaData)
                        voucherAttachMO.isDone = true
                        attachmentsList.add(voucherAttachMO)
                    }
                }) { t: Throwable? ->
                    Timber.e(t)
                    showToast("Unable to process the other task attachment...")
                })
        }

        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                if (!attachmentsList.isNullOrEmpty() && attachmentsList.size > 0) {
                    insertAttachmentToDB(attachmentsList)
                }
            }
        }
    }

    private fun insertAttachmentToDB(attachments: List<AttachmentEntity>) {
        addDisposable(attachmentDao.insertAll(attachments)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                oneTimeWorkerWithNetwork(AttachmentsUploadWorker::class.java)
                message.what = NavigationConstants.ON_KIT_REPLACE_DONE
                liveData.postValue(message)

            }) { t: Throwable? ->
                Timber.e(t)
                showToast("Unable to insert AKR photos in DB")
            })
    }
}