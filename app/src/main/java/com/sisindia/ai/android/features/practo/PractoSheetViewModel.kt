package com.sisindia.ai.android.features.practo

import android.app.Application
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.droidcommons.preference.Prefs
import com.google.gson.Gson
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.features.billsubmit.RadioCheckedListener
import com.sisindia.ai.android.room.dao.AttachmentDao
import com.sisindia.ai.android.room.dao.DayCheckDao
import com.sisindia.ai.android.room.dao.TaskDao
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.room.entities.PractoQuestionEntity
import com.sisindia.ai.android.uimodels.PractoQuestionsMO
import com.sisindia.ai.android.uimodels.attachments.OtherTaskAttachmentMetaData
import com.sisindia.ai.android.utils.FileUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 13-07-2023
 */
class PractoSheetViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var taskDao: TaskDao

    @Inject
    lateinit var attachmentDao: AttachmentDao

    @Inject
    lateinit var dayCheckDao: DayCheckDao

    val obsIsAppInstalled = ObservableBoolean(true)
    val obsIsAskedToInstallApp = ObservableBoolean(true)
    val obsIsYesOpted1 = ObservableBoolean(true)
    val obsIsYesOpted2 = ObservableBoolean(false)
    val addRemarks = ObservableField("")

    var appInstalledAttachment =
        ObservableField(AttachmentEntity(AttachmentEntity.AttachmentSourceType.PRACTO_APP_INSTALL))
    var appNotInstalledAttachment =
        ObservableField(AttachmentEntity(AttachmentEntity.AttachmentSourceType.PRACTO_APP_NOT_INSTALL))

    val rgListener = object : RadioCheckedListener {
        override fun onRadioButtonChecked(value: String) {
            if (value == app.resources.getString(R.string.string_yes)) {
                obsIsAppInstalled.set(true)
                obsIsYesOpted1.set(true)
            } else if (value == app.resources.getString(R.string.string_no)) {
                obsIsAppInstalled.set(false)
                obsIsYesOpted1.set(false)
            }
        }
    }

    val rgConfirmListener = object : RadioCheckedListener {
        override fun onRadioButtonChecked(value: String) {
            if (value == app.resources.getString(R.string.string_yes)) {
                obsIsAskedToInstallApp.set(true)
                obsIsYesOpted2.set(true)
            } else if (value == app.resources.getString(R.string.string_no)) {
                obsIsAskedToInstallApp.set(false)
                obsIsYesOpted2.set(false)
            }
        }
    }

    fun onViewClicks(view: View) {

        when (view.id) {

            R.id.isAppInstalledPictureLayout -> {
                message.what = NavigationConstants.ON_CAPTURE_APP_INSTALL_IMAGE
                liveData.postValue(message)
            }

            R.id.askedToInstallAppPictureLayout -> {
                message.what = NavigationConstants.ON_CAPTURE_APP_NOT_INSTALL_IMAGE
                liveData.postValue(message)
            }

            R.id.crossButton -> {
                message.what = NavigationConstants.ON_CLOSE_SCREEN
                liveData.postValue(message)
            }

            R.id.doneButton -> {
                if (obsIsYesOpted1.get() && (appInstalledAttachment.get() == null || appInstalledAttachment.get()!!.localFilePath.isNullOrEmpty()))
                    showToast("Please click photo of app installed in guard's mobile")
                else if (!obsIsYesOpted1.get() && obsIsYesOpted2.get() && (appNotInstalledAttachment.get() == null || appNotInstalledAttachment.get()!!.localFilePath.isNullOrEmpty()))
                    showToast("Please click photo of app installed in guard's mobile by you")
                else if (!obsIsYesOpted1.get() && !obsIsYesOpted2.get() && addRemarks.get().isNullOrEmpty())
                    showToast("Please enter remarks")
                else {
                    val questionMO = PractoQuestionsMO(obsIsYesOpted1.get(),
                        obsIsYesOpted2.get(), addRemarks.get()!!)
                    val entity = PractoQuestionEntity()
                    entity.siteId = Prefs.getInt(PrefConstants.CURRENT_SITE)
                    entity.taskId = Prefs.getInt(PrefConstants.CURRENT_TASK)
                    entity.employeeId = Prefs.getInt(PrefConstants.SELECTED_EMPLOYEE_ID)
                    entity.employeeNo = Prefs.getString(PrefConstants.SELECTED_EMPLOYEE_NO)
                    entity.questionsJson = Gson().toJson(questionMO)

                    addDisposable(taskDao.insertAppTask(entity)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            updateCheckedGuardStatus()
                            if (obsIsYesOpted1.get())
                                updateAppInstalledAttachment(appInstalledAttachment.get()!!)
                            else if (obsIsYesOpted2.get())
                                updateAppInstalledAttachment(appNotInstalledAttachment.get()!!)

                            message.what = NavigationConstants.ON_PRACTO_QUESTION_COMPLETED
                            liveData.postValue(message)

                        }) { t: Throwable? ->
                            Timber.e(t)
                            showToast("Unable to process the other task attachment...")
                        })
                }
            }
        }
    }

    private fun updateAppInstalledAttachment(attachment: AttachmentEntity) {
        val siteId = Prefs.getInt(PrefConstants.CURRENT_SITE)
        addDisposable(taskDao.practoAttachmentFile(attachment.attachmentSourceType,
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
//                        oneTimeWorkerWithNetwork(AttachmentsUploadWorker::class.java)
                    }) { t: Throwable? ->
                        Timber.e(t)
                        showToast("Unable to process the other task attachment...")
                    })
            }, Timber::e))
    }

    private fun updateCheckedGuardStatus() {
        addDisposable(dayCheckDao.updateCheckedGuardViaPracto(
            Prefs.getInt(PrefConstants.CURRENT_TASK),
            Prefs.getInt(PrefConstants.SELECTED_EMPLOYEE_ID))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.e("Checked Guard Status updated")
            }) { t: Throwable? ->
                Timber.e("Unable to update PRACTO checked Guard Status ")
            })
    }
}