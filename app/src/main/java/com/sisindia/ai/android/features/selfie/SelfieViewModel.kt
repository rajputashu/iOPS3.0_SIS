package com.sisindia.ai.android.features.selfie

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import com.droidcommons.preference.Prefs
import com.google.gson.Gson
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.room.dao.AttachmentDao
import com.sisindia.ai.android.room.dao.TaskDao
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.uimodels.attachments.SelfieAttachmentMetadata
import com.sisindia.ai.android.utils.FileUtils
import com.sisindia.ai.android.workers.AzureFileStorageWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 11/27/2020.
 */
class SelfieViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var attachmentDao: AttachmentDao

    @Inject
    lateinit var taskDao: TaskDao

    val photoAttachmentObs =
        ObservableField(AttachmentEntity(AttachmentEntity.AttachmentSourceType.SELFIE))

    fun updateSefieAttachment() {
        val attachment = photoAttachmentObs.get()
        val builder = StringBuilder()
        builder.append(attachment!!.attachmentSourceType)
        builder.append("_")
        builder.append(Prefs.getInt(PrefConstants.AREA_INSPECTOR_ID))
        builder.append("_")
        builder.append(attachment.attachmentGuid)
        builder.append(FileUtils.EXT_JPG)
        attachment.storagePath = builder.toString()

        val metaData = SelfieAttachmentMetadata()

        metaData.attachmentTypeId = 1
        metaData.attachmentSourceTypeId = attachment.attachmentSourceType
        metaData.uuid = attachment.attachmentGuid
        metaData.fileName = builder.toString()
        metaData.fileSize = attachment.fileSize.toString()
        metaData.storagePath = "Selfie/" + attachment.storagePath

        //additional tags
        metaData.title = builder.toString()
        metaData.fileName = builder.toString()
        metaData.path = attachment.localFilePath
        metaData.sourceId = Prefs.getInt(PrefConstants.AREA_INSPECTOR_ID)
        metaData.extension = FileUtils.EXT_JPG
        metaData.sizeInKB = attachment.fileSize.toInt()
        metaData.attachmentGuid = attachment.attachmentGuid

        attachment.attachmentMetaData = Gson().toJson(metaData)
        attachment.isDone = true

        addDisposable(attachmentDao.insert(attachment)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
//                if (!BuildConfig.DEBUG) // Comment If Required
                oneTimeWorkerWithNetwork(AzureFileStorageWorker::class.java)
                /*if (Prefs.getString(PrefConstants.SAS_USER_CONTAINER_KEY, "").isNullOrEmpty()) {
                    val data = Data.Builder().putBoolean(AzureFileStorageWorker::class.java.simpleName, true).build()
                    oneTimeWorkerWithInputData(AzureFileStorageWorker::class.java, data)
                } else {
                    // oneTimeWorkerWithNetwork(AttachmentsUploadWorker::class.java)
                    val data = Data.Builder().putBoolean(AttachmentsUploadWorker::class.java.simpleName, true).build()
                    oneTimeWorkerWithInputData(AttachmentsUploadWorker::class.java, data)
                }*/
            }) { t: Throwable? -> Timber.e(t) })
        message.what = NavigationConstants.ON_SELFIE_ATTACHMENT_INSERTED
        liveData.postValue(message)
    }

    fun onViewClicks(view: View) {
        if (view.id == R.id.selfieRetakeButton) {
            message.what = NavigationConstants.ON_IMAGE_RETAKE_V2
            liveData.postValue(message)
        } else if (view.id == R.id.selfieConfirmButton) {
            val item: AttachmentEntity = photoAttachmentObs.get()!!
            item.isFileSaved = true
            message.obj = item
            message.what = NavigationConstants.ON_IMAGE_CONFIRM_V2
            liveData.postValue(message)
        }
    }

    //NOTE:
    // WE CAN ALSO DO ONE THING {CALL AZUREFILESTORAGE WORKER EVERYTIME WHEN SELFIE TAKEN AND
    // AFTER RECEIVING TOKEN CALL ATTACHEMENTUPLOAD WORKER FOR SELFIE UPLOAD AND NEXT API}
    // AND REMOVE CALL OF AzureFileStorageWorker FROM DASHBOARD VIEW MODEL @LINE NO 196
}