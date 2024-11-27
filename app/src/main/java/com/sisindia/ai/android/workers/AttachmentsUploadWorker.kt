package com.sisindia.ai.android.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.work.WorkerParameters
import com.droidcommons.dagger.worker.AndroidWorkerInjection
import com.droidcommons.preference.Prefs
import com.google.gson.Gson
import com.sisindia.ai.android.base.BaseWorker
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.rest.AttachmentUploadAPI
import com.sisindia.ai.android.room.dao.AttachmentDao
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.uimodels.akr.AKRAttachmentMetaDataMo
import com.sisindia.ai.android.uimodels.attachments.AKRAttachmentMetadata
import com.sisindia.ai.android.uimodels.attachments.SelfieAttachmentMetadata
import com.sisindia.ai.android.utils.WaterMarkUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber
import java.io.File
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/13/2020.
 */
class AttachmentsUploadWorker @Inject constructor(
    val context: Context, workerParameters: WorkerParameters) :
    BaseWorker(context, workerParameters) {

    init {
        AndroidWorkerInjection.inject(this)
    }

    private var isRefreshingSASToken = false

    @Inject
    lateinit var attachmentUploadApi: AttachmentUploadAPI

    @Inject
    lateinit var attachmentDao: AttachmentDao

    override fun doWork(): Result {
        addDisposable(attachmentDao.fetchAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ attachmentList ->
                if (attachmentList.isNotEmpty())
                    generateHeaderAndUploadFileRecursive(attachmentList, 0)
//                generateHeaderAndUploadFile(attachmentList)
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
        return Result.success()
    }

    private fun generateHeaderAndUploadFileRecursive(attachmentList: MutableList<AttachmentEntity>,
        index: Int) {
        if (index < attachmentList.size) {
            try {
                if (attachmentList[index].attachmentMetaData != null && attachmentList[index].attachmentMetaData.isNotEmpty()) {
                    val attachmentMO = attachmentList[index]
                    val metaDataJsonObject = JSONObject(attachmentMO.attachmentMetaData)
                    val headerMap = HashMap<String, Any>()
                    headerMap["x-ms-blob-type"] = "BlockBlob"
                    for (keys: String in metaDataJsonObject.keys()) {
                        if (!keys.equals("storagePath", ignoreCase = true))
                            headerMap["x-ms-meta-$keys"] = metaDataJsonObject.get(keys)
                    }
                    var employeeNumber = Prefs.getString(PrefConstants.AREA_INSPECTOR_CODE)

                    if (metaDataJsonObject.has("employeeNo"))
                        employeeNumber = metaDataJsonObject.getString("employeeNo")
                    val storagePath = metaDataJsonObject.getString("storagePath")

                    val file = File(Uri.parse(attachmentMO.localFilePath!!).path!!)
                    if (file.exists()) {
//                        if (file.extension.contains("3gp")) {
                        if (file.extension.contains("mp3")) {
                            //Without water mark
//                            val fileMimeType = "audio/mpeg"
                            val fileMimeType = "audio/mp3"
                            val requestBody = file.asRequestBody(fileMimeType.toMediaTypeOrNull())
                            var userContainerSAS =
                                Prefs.getString(PrefConstants.SAS_USER_CONTAINER_KEY)
                            userContainerSAS = userContainerSAS.replace("?", "/${storagePath}?")
                            addDisposable(attachmentUploadApi.uploadFileWithHeader(headerMap,
                                userContainerSAS, requestBody)!!
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    updateIdOfUploadedFile(attachmentMO.id, file)
                                    Timber.e("Image Response : AUDIO File Uploaded Successfully to AZURE BLOB")
                                    generateHeaderAndUploadFileRecursive(attachmentList, index + 1)
                                }, { throwable: Throwable? ->
                                    handleErrorCode(throwable!!)
                                    generateHeaderAndUploadFileRecursive(attachmentList, index + 1)
                                }))
                        } else {
                            val wmFile =
                                WaterMarkUtil.createWaterMark(BitmapFactory.decodeFile(file.path),
                                    attachmentMO, context,
                                    employeeNumber, attachmentMO.capturedDateTime)
                            wmFile?.apply {
                                val fileMimeType = "image/jpg"
                                val requestBody =
                                    this.asRequestBody(fileMimeType.toMediaTypeOrNull())
                                var userContainerSAS =
                                    Prefs.getString(PrefConstants.SAS_USER_CONTAINER_KEY)
                                userContainerSAS = userContainerSAS.replace("?", "/${storagePath}?")
                                addDisposable(attachmentUploadApi.uploadFileWithHeader(
                                    headerMap, userContainerSAS, requestBody)!!
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({
                                        Timber.e("Image Response : File Uploaded Successfully to AZURE BLOB")
                                        // UPLOADING OR UPDATING SELFIE DATA TO
                                        if (attachmentMO.attachmentSourceType == AttachmentEntity.AttachmentSourceType.SELFIE.sourceType)
                                            uploadMetaDataOfSelfie(attachmentMO, userContainerSAS)
                                        else if (attachmentMO.attachmentSourceType == AttachmentEntity.AttachmentSourceType.KIT_DISTRIBUTE_PHOTO.sourceType ||
                                            attachmentMO.attachmentSourceType == AttachmentEntity.AttachmentSourceType.KIT_VOUCHER_PHOTO.sourceType)
                                            uploadMetaDataOfAKR(attachmentMO, userContainerSAS)

                                        updateIdOfUploadedFile(attachmentMO.id, this, file)

                                        generateHeaderAndUploadFileRecursive(attachmentList,
                                            index + 1)

                                    }, { throwable: Throwable? ->
                                        handleErrorCode(throwable!!)
                                        generateHeaderAndUploadFileRecursive(attachmentList,
                                            index + 1)
                                    }))
                            }
                        }
                    } else
                        generateHeaderAndUploadFileRecursive(attachmentList, index + 1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                generateHeaderAndUploadFileRecursive(attachmentList, index + 1)
            }
        }
    }

    //Note : For previous code refer 'desktop/iOPS2.0/UploadWorkerBackup'
    /*private fun generateHeaderAndUploadFile(attachmentList: MutableList<AttachmentEntity>) {
        try {
            for (attachmentMO: AttachmentEntity in attachmentList) {
                if (attachmentMO.attachmentMetaData != null && attachmentMO.attachmentMetaData.isNotEmpty()) {
                    val metaDataJsonObject = JSONObject(attachmentMO.attachmentMetaData)
                    val headerMap = HashMap<String, Any>()
                    headerMap["x-ms-blob-type"] = "BlockBlob"
                    for (keys: String in metaDataJsonObject.keys()) {
                        if (!keys.equals("storagePath", ignoreCase = true))
                            headerMap["x-ms-meta-$keys"] = metaDataJsonObject.get(keys)
                    }
                    //                    Timber.e("Generated Header : $headerMap")
                    var employeeNumber = Prefs.getString(PrefConstants.AREA_INSPECTOR_CODE)
                    if (metaDataJsonObject.has("employeeNo"))
                        employeeNumber = metaDataJsonObject.getString("employeeNo")

                    startUploadingFileToServer(attachmentMO,
                        metaDataJsonObject.getString("storagePath"),
                        headerMap, employeeNumber)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startUploadingFileToServer(attachmentMO: AttachmentEntity,
        storagePath: String, headerMap: HashMap<String, Any>, employeeNumber: String) {
        val file = File(Uri.parse(attachmentMO.localFilePath!!).path!!)
        if (file.exists()) {
            if (file.extension.contains("3gp")) {
                //Without water mark
                val fileMimeType = "audio/mpeg"
                val requestBody = file.asRequestBody(fileMimeType.toMediaTypeOrNull())
                var userContainerSAS = Prefs.getString(PrefConstants.SAS_USER_CONTAINER_KEY)
                userContainerSAS = userContainerSAS.replace("?", "/${storagePath}?")
                addDisposable(attachmentUploadApi.uploadFileWithHeader(
                    headerMap, userContainerSAS, requestBody)!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        updateIdOfUploadedFile(attachmentMO.id, file)
                        Timber.e("Image Response : AUDIO File Uploaded Successfully to AZURE BLOB")
                    }, { throwable: Throwable? ->
                        handleErrorCode(throwable!!)
                    }))
            } else {

                val wmFile = WaterMarkUtil.createWaterMark(BitmapFactory.decodeFile(file.path),
                    attachmentMO, context, employeeNumber)
                wmFile?.apply {
                    val fileMimeType = "image/jpg"
                    val requestBody = this.asRequestBody(fileMimeType.toMediaTypeOrNull())
                    var userContainerSAS = Prefs.getString(PrefConstants.SAS_USER_CONTAINER_KEY)
                    userContainerSAS = userContainerSAS.replace("?", "/${storagePath}?")
                    addDisposable(attachmentUploadApi.uploadFileWithHeader(
                        headerMap, userContainerSAS, requestBody)!!
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Timber.e("Image Response : File Uploaded Successfully to AZURE BLOB")
                            // UPLOADING OR UPDATING SELFIE DATA TO
                            if (attachmentMO.attachmentSourceType == AttachmentEntity.AttachmentSourceType.SELFIE.sourceType)
                                uploadMetaDataOfSelfie(attachmentMO, userContainerSAS)
                            else if (attachmentMO.attachmentSourceType == AttachmentEntity.AttachmentSourceType.KIT_DISTRIBUTE_PHOTO.sourceType ||
                                attachmentMO.attachmentSourceType == AttachmentEntity.AttachmentSourceType.KIT_VOUCHER_PHOTO.sourceType)
                                uploadMetaDataOfAKR(attachmentMO, userContainerSAS)

                            updateIdOfUploadedFile(attachmentMO.id, this, file)

                        }, { throwable: Throwable? ->
                            handleErrorCode(throwable!!)
                        }))
                }
            }
        } else Timber.e("File Upload: File Not Found or Deleted by user ")
    }*/

    private fun uploadMetaDataOfSelfie(entity: AttachmentEntity, storagePath: String) {
        val metaData =
            Gson().fromJson(entity.attachmentMetaData, SelfieAttachmentMetadata::class.java)
        metaData.path = storagePath
        addDisposable(coreApi.pushDutyOnMetaDataAttachment(metaData)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.e("OfSelfie MetaData pushed successfully")
                updateStatusOfAttachmentAPI(entity.id, true)
            }, {
                updateStatusOfAttachmentAPI(entity.id, false)
            }))
    }

    private fun uploadMetaDataOfAKR(entity: AttachmentEntity, storagePath: String) {
        val metaData =
//            Gson().fromJson(entity.attachmentMetaData, AKRGsonMetadata::class.java)
            Gson().fromJson(entity.attachmentMetaData, AKRAttachmentMetaDataMo::class.java)
        val akrMetaData = AKRAttachmentMetadata()
        akrMetaData.title = metaData.fileName
        akrMetaData.path = storagePath
        akrMetaData.sourceId = metaData.kitId
        akrMetaData.sourceTypeId = metaData.attachmentSourceTypeId
        akrMetaData.sizeInKB = metaData.fileSize.toInt()
        akrMetaData.fileName = metaData.fileName

        addDisposable(coreApi.pushAKRMetaDataAttachment(akrMetaData)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.e("AKR MetaData pushed successfully")
//                updateStatusOfAttachmentAPI(entity.id, true)
            }, {
//                updateStatusOfAttachmentAPI(entity.id, false)
                Timber.e("ERRRO AKR MetaData pushed successfully")
            }))
    }

    private fun updateIdOfUploadedFile(attachmentId: Int,
        fileToDelete: File, oldFileToDelete: File? = null) {
        addDisposable(attachmentDao.updateIdOfUploadedFile(attachmentId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                try {
                    fileToDelete.delete()
                    oldFileToDelete?.delete()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            })
        )
    }

    private fun updateStatusOfAttachmentAPI(attachmentId: Int, apiStatus: Boolean) {
        addDisposable(attachmentDao.updateStatusOfAttachmentAPI(attachmentId, apiStatus)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ }, { }))
    }

    private fun handleErrorCode(throwable: Throwable) {
        throwable.printStackTrace()
        if (throwable is HttpException) {
            when (throwable.code()) {
                401 -> Timber.e("Not Upload May be 401")
                402 -> Timber.e("Not Upload May be 402")
                403 -> {
                    if (!isRefreshingSASToken) {
                        Timber.e("Coming to refresh SAS Token....403")
                        isRefreshingSASToken = true
                        updateSASTokenFromAPI()
                    }
                }

                404 -> {
                    Timber.e("Not Upload May be 404")
                    if (!isRefreshingSASToken) {
                        Timber.e("Coming to refresh SAS Token....404")
                        isRefreshingSASToken = true
                        updateSASTokenFromAPI()
                    }
                }

                else -> Timber.e("Not Upload May be ELSE Exception........")
            }
        }
    }

    private fun updateSASTokenFromAPI() {
        addDisposable(coreApi.azureSasUserContainerToken
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ (statusCode, _, data) ->
                if (statusCode == 200 && data != null)
                    Prefs.putString(PrefConstants.SAS_USER_CONTAINER_KEY, data.sasToken)
            }) {
                Timber.e("updateSASTokenFromAPI() Error is coming....")
            })
    }
}