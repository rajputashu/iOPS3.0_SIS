package com.sisindia.ai.android.features.mask

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import com.google.gson.Gson
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.commons.SpinnersListener
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.models.mask.AddMaskRequestBodyMO
import com.sisindia.ai.android.models.mask.MaskAttachmentMetaDataMO
import com.sisindia.ai.android.room.dao.AttachmentDao
import com.sisindia.ai.android.room.dao.GuardKitRequestDao
import com.sisindia.ai.android.room.dao.SiteDao
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.room.entities.SiteEntity
import com.sisindia.ai.android.utils.FileUtils
import com.sisindia.ai.android.utils.IopsUtil
import com.sisindia.ai.android.workers.AttachmentsUploadWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import javax.inject.Inject

class MaskDistributionViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    val obsMaskDistributedCount = ObservableField("0")
    val maskAdapter = MaskAdapter()
    val obsGuardIdOrName = ObservableField("")
    private var selectedEmployeeNo: String = ""
    val obsSiteList = ObservableField(arrayListOf(""))
    private var selectedPos: Int = 0
    private lateinit var siteEntityList: List<SiteEntity>
    private var isSyncing: Boolean = false

    var obsPhotoAttachment =
        ObservableField(AttachmentEntity(AttachmentEntity.AttachmentSourceType.MASK_GUARD_PHOTO))

    @Inject
    lateinit var guardKitRequestDao: GuardKitRequestDao

    @Inject
    lateinit var siteDao: SiteDao

    @Inject
    lateinit var attachmentDao: AttachmentDao

    var listener: SpinnersListener = object : SpinnersListener {
        override fun onSpinnerOptionSelected(pos: Int) {
            selectedPos = pos
        }

        override fun onSpinnerOptionSelected(item: Any) {}
    }

    fun getAddedMaskDetailsFromAPI() {
        isLoading.set(View.VISIBLE)
        addDisposable(coreApi.maskDistributionList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.set(View.GONE)
                it?.apply {
                    data?.apply {
                        obsMaskDistributedCount.set(this.distributedMaskCount.toString())
                        if (!this.distributedMaskList.isNullOrEmpty())
                            maskAdapter.clearAndSetItems(this.distributedMaskList)
                    }
                }
            }, {
                isLoading.set(View.GONE)
                if (IopsUtil.isInternetAvailable(app))
                    showToast(app.resources.getString(R.string.string_server_error))
                else
                    showToast(app.resources.getString(R.string.string_no_internet))
            }))
    }

    fun onGuardQrScanned(rawData: String) {
        when {
            rawData.isEmpty() -> showToast("Qr code is empty..")
            rawData.contains("Company :") -> showToast("Post QR is not allowed, please scan valid guard QR")
            else -> {
                try {
                    selectedEmployeeNo = rawData.split("\n")[1].trim()
                    obsGuardIdOrName.set(selectedEmployeeNo + "/" + rawData.split("\n")[0].trim())
                } catch (e: Exception) {

                }
            }
        }
    }

    fun fetchAllSites() {
        addDisposable(siteDao.fetchAllActiveSitesForAdHoc(true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (!it.isNullOrEmpty()) {
                    siteEntityList = it
                    val modifiedList = arrayListOf("Select Site")
                    it.forEach { siteEntity ->
                        modifiedList.add(siteEntity.siteName)
                    }
                    obsSiteList.set(modifiedList)
                }
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
    }

    fun onViewClicks(view: View) {
        when (view.id) {
            R.id.headerMaskDistribution -> {
                message.what = NavigationConstants.OPEN_DASH_BOARD_DRAWER
                liveData.postValue(message)
            }
            R.id.addMaskRequest -> {
                message.what = NavigationConstants.OPEN_ADD_MASK_REQUEST
                liveData.postValue(message)
            }
            R.id.takeGuardPhotoLayout -> {
                message.what = NavigationConstants.ON_TAKE_PICTURE
                message.obj = obsPhotoAttachment.get()
                liveData.postValue(message)
            }
            R.id.scanGuardQRView -> {
                message.what = NavigationConstants.OPEN_LIVE_QR_SCANNER_SCREEN
                liveData.postValue(message)
            }
            R.id.addMaskDistributionButton -> validateRequest()
            R.id.sheetCloseButton -> validateRequest()
        }
    }

    private fun validateRequest() {
        if (obsGuardIdOrName.get().isNullOrEmpty())
            showToast("Scan guard QR to fetch ID and Name")
        else if (selectedPos == 0)
            showToast("Please select site")
        else if (obsPhotoAttachment.get() == null || obsPhotoAttachment.get()!!.localFilePath.isNullOrEmpty())
            showToast("Guard photo is mandatory")
        else {
            addMaskDistributionRequest()
        }
    }

    private fun addMaskDistributionRequest() {

        if (isSyncing) {
            showToast("Syncing mask distribution in progress, please wait...")
            return
        }

        isSyncing = true
        isLoading.set(View.VISIBLE)
        val maskBody = AddMaskRequestBodyMO()
        maskBody.createdDateTime = LocalDateTime.now().toString()
        maskBody.updatedDateTime = LocalDateTime.now().toString()
        maskBody.siteId = siteEntityList[selectedPos - 1].id
        maskBody.employeeNo = selectedEmployeeNo

        addDisposable(coreApi.addMaskDistribution(maskBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isSyncing = false
                if (it.statusCode == 200) {
                    isLoading.set(View.GONE)
                    addAttachmentsToDBAndSync()
                } else
                    showToast(it.statusMessage)
            }, {
                isSyncing = false
                isLoading.set(View.GONE)
                if (IopsUtil.isInternetAvailable(app))
                    showToast(app.resources.getString(R.string.string_server_error))
                else
                    showToast(app.resources.getString(R.string.string_no_internet))
            }))
    }

    private fun addAttachmentsToDBAndSync() {
        val photoAttachMO = obsPhotoAttachment.get()
        val optSiteId = siteEntityList[selectedPos - 1].id
        val optSiteCode = siteEntityList[selectedPos - 1].siteCode
        if (photoAttachMO != null && !photoAttachMO.localFilePath.isNullOrEmpty()) {
            addDisposable(guardKitRequestDao.maskGuardFile(photoAttachMO.attachmentSourceType,
                optSiteId, optSiteCode, selectedEmployeeNo, photoAttachMO.attachmentGuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it?.apply {
                        photoAttachMO.storagePath = this.storagePath + FileUtils.EXT_JPG
                        val metaData = MaskAttachmentMetaDataMO()
                        metaData.attachmentTypeId = 1
                        metaData.siteId = optSiteId
                        metaData.siteCode = optSiteCode
                        metaData.attachmentSourceTypeId = photoAttachMO.attachmentSourceType
                        metaData.uuid = photoAttachMO.attachmentGuid
                        metaData.fileName = this.fileName + FileUtils.EXT_JPG
                        metaData.fileSize = photoAttachMO.fileSize.toString()
                        metaData.storagePath = photoAttachMO.storagePath

                        photoAttachMO.attachmentMetaData = Gson().toJson(metaData)
                        photoAttachMO.isDone = true
                        insertAttachmentIntoDB(photoAttachMO)
                    }
                }) { t: Throwable? ->
                    Timber.e(t)
                    showToast("Unable to process the other task attachment...")
                })
        }
    }

    private fun insertAttachmentIntoDB(attachmentEntity: AttachmentEntity) {
        addDisposable(attachmentDao.insert(attachmentEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                oneTimeWorkerWithNetwork(AttachmentsUploadWorker::class.java)
                message.what = NavigationConstants.ON_MASK_ADDED_SUCCESSFULLY
                liveData.postValue(message)
            }) { t: Throwable? ->
                Timber.e(t)
                showToast("Unable to insert guard photo in DB")
            })
    }
}