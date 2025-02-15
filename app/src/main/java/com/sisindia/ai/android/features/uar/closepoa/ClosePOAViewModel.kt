package com.sisindia.ai.android.features.uar.closepoa

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.databinding.ObservableField
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.commons.CaptureImageType
import com.sisindia.ai.android.commons.POAStatus
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.features.uar.dialog.DialogListener
import com.sisindia.ai.android.room.dao.AttachmentDao
import com.sisindia.ai.android.room.dao.AttachmentMetadataDefinitionDao
import com.sisindia.ai.android.room.dao.SiteRiskPoaDao
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.uimodels.collection.CollectionAttachmentMO
import com.sisindia.ai.android.workers.AtRiskPoaWorker
import com.sisindia.ai.android.workers.AttachmentsUploadWorker
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.threeten.bp.LocalDate
import java.util.*
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/4/2020.
 */
class ClosePOAViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var attachmentDao: AttachmentDao

    @Inject
    lateinit var riskPoaDao: SiteRiskPoaDao

    @Inject
    lateinit var metadataDefinitionDao: AttachmentMetadataDefinitionDao

    var poaId = ObservableField<Int>()
    var closeDate = ObservableField<LocalDate>(LocalDate.now())
    var addRemarks = ObservableField<String>("")
    var photoImageUri = ObservableField<Uri>()
    var closePoaImageMetaData = ObservableField<String>("")
    private lateinit var generatedUUID: String
    var poaPicAttachmentEntity = ObservableField<AttachmentEntity>()

    val listener = object : DialogListener {
        override fun onCrossButtonClick() {
        }

        override fun onViewAllPOAClick() {
            message.what = NavigationConstants.ON_VIEW_ALL_POA
            liveData.postValue(message)
        }

        override fun onYesButtonClicked() {
        }

        override fun onNoButtonClicked() {
        }
    }

    fun onDatePickerClick(view: View) {
        message.what = NavigationConstants.OPEN_DATE_PICKER
        liveData.postValue(message)
    }

    fun onConfirmByPhotoClick(view: View) {
        message.what = NavigationConstants.OPEN_POA_CONFIRM_BY_PHOTO
        liveData.postValue(message)
        //        generateFileNameAndStoragePath()
    }

    fun onClosePOABtnClick(view: View) {
        when {
            addRemarks.get().toString()
                .isEmpty() -> showToast(app.resources.getString(R.string.string_msg_remarks))
            //            photoImageUri.get() == null -> showToast(app.resources.getString(R.string.string_msg_capture_photo))
            else -> {
                if (photoImageUri.get() != null)
                    insertPoaImageToDB()
                updatePoaClosureDetails()
            }
        }
    }

    private fun insertPoaImageToDB() {
        val attachmentMO =
            AttachmentEntity(photoImageUri.get().toString(), closePoaImageMetaData.get())
        addDisposable(attachmentDao.insert(attachmentMO).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
            oneTimeWorkerWithNetwork(AttachmentsUploadWorker::class.java)
        }, { }))
    }

    private fun updatePoaClosureDetails() {
        addDisposable(riskPoaDao.updateOnClosingPOA(closeDate.get().toString(),
            addRemarks.get().toString(), POAStatus.CLOSED.status, poaId.get()!!.toInt())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                syncClosedPOAToServer()
                message.what = NavigationConstants.ON_POA_INSERTED_SUCCESSFULLY
                liveData.postValue(message)
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
    }

    private fun syncClosedPOAToServer() {
        val request = OneTimeWorkRequest.Builder(AtRiskPoaWorker::class.java).build()
        WorkManager.getInstance(getApplication()).enqueue(request)
    }

    private fun generateFileNameAndStoragePath() {
        generatedUUID = UUID.randomUUID().toString()
        addDisposable(Single.zip(riskPoaDao.getAttachmentDataForPoaClose(CaptureImageType.CLOSE_POA.attachmentSourceTypeId,
            Prefs.getInt(PrefConstants.CURRENT_SITE), 2, 3, poaId.get()!!.toInt(), generatedUUID),
            metadataDefinitionDao.fetchMetaDataJsonFormat(CaptureImageType.BILL_COLLECTION.attachmentSourceTypeId),
            BiFunction<CollectionAttachmentMO, String, Boolean> { fileNameAndStoragePath, metaDataModel ->
                return@BiFunction onResultFetch(fileNameAndStoragePath, metaDataModel)
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                it.printStackTrace()
            }))
    }

    private fun onResultFetch(fileDetails: CollectionAttachmentMO, metaDataJson: String): Boolean {
        val finalStoragePath = fileDetails.storagePath + "/" + fileDetails.fileName
        val jsonObjectMO = JSONObject(metaDataJson)
        jsonObjectMO.put("uuid", generatedUUID)
        jsonObjectMO.put("storagePath", finalStoragePath)
        jsonObjectMO.put("siteId", Prefs.getInt(PrefConstants.CURRENT_SITE))
        jsonObjectMO.put("riskId", 0)
        jsonObjectMO.put("poaId", poaId.get()!!.toInt())
        jsonObjectMO.put("fileName", fileDetails.fileName)

        val attachmentEntity = AttachmentEntity()
        attachmentEntity.localFileName = fileDetails.fileName
        attachmentEntity.storagePath = finalStoragePath
        attachmentEntity.attachmentMetaData = jsonObjectMO.toString()
        attachmentEntity.isAttachmentSync = false

        poaPicAttachmentEntity.set(attachmentEntity)

        message.what = NavigationConstants.OPEN_POA_CONFIRM_BY_PHOTO
        liveData.postValue(message)
        return true
    }
}