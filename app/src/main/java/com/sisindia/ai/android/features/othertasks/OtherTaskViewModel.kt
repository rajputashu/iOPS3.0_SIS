package com.sisindia.ai.android.features.othertasks

import android.app.Application
import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableField
import androidx.work.Data
import com.droidcommons.base.timer.CountUpTimer
import com.droidcommons.preference.Prefs
import com.google.gson.Gson
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.room.dao.*
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.room.entities.DailyTimeLineEntity
import com.sisindia.ai.android.room.entities.TaskEntity
import com.sisindia.ai.android.uimodels.attachments.OtherTaskAttachmentMetaData
import com.sisindia.ai.android.uimodels.tasks.OtherActivity
import com.sisindia.ai.android.uimodels.tasks.OthersTaskExecutionResultMO
import com.sisindia.ai.android.utils.FileUtils
import com.sisindia.ai.android.utils.GsonUtils
import com.sisindia.ai.android.workers.AttachmentsUploadWorker
import com.sisindia.ai.android.workers.RotaTaskWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 3/12/2020.
 */
class OtherTaskViewModel @Inject constructor(app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var timer: CountUpTimer

    @Inject
    lateinit var metadataDefinitionDao: AttachmentMetadataDefinitionDao

    @Inject
    lateinit var taskDao: TaskDao

    @Inject
    lateinit var lookupDao: LookUpDao

    @Inject
    lateinit var attachmentDao: AttachmentDao


    @Inject
    lateinit var dailyTimeLineDao: DailyTimeLineDao

    var otherTask = ObservableField(TaskEntity())
    var otherTaskType = ObservableField("")
    var reason = ObservableField("")
    var description = ObservableField("")

    var otherAttachment =
        ObservableField(AttachmentEntity(AttachmentEntity.AttachmentSourceType.OTHER_TASK))

    fun updateTaskExecutionStartDetails() {
        addDisposable(taskDao.updateTaskOnStartDayCheck(Prefs.getInt(PrefConstants.CURRENT_TASK),
            LocalDateTime.now().toString(), TaskEntity.TaskStatus.IN_PROGRESS.taskStatus)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ triggerRotaTaskWorker() }, { }))
    }

    fun getOtherTaskDetails() {
        addDisposable(taskDao.fetchOtherTaskDetails(Prefs.getInt(PrefConstants.CURRENT_TASK))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                otherTask.set(it)
                when (it.taskTypeId) {
                    8 -> otherTaskType.set("Non Unit")
                    9 -> otherTaskType.set("BranchTask")
                    else -> {
                        addDisposable(lookupDao.fetchDisplayName(it.otherTaskTypeLookUpIdentifier,
                            51)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(otherTaskType::set, Timber::e))
                    }
                }

                addDisposable(lookupDao.fetchDisplayName(it.reasonLookUpIdentifier, 19)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(reason::set, Timber::e))
            }, Timber::e))
    }

    fun onPhotoClick(view: View) {
        message.what = NavigationConstants.ON_TAKE_PICTURE
        message.obj = otherAttachment.get()
        liveData.postValue(message)
    }

    fun onTaskCompleteBtnClick(view: View?) {
        val desc = description.get()
        var uuid = ""
        val task = otherTask.get()
        val attachment = otherAttachment.get()

        if (TextUtils.isEmpty(desc)) {
            showToast("Please enter description")
            return
        }

        if (attachment != null && !TextUtils.isEmpty(attachment.localFilePath)) {
            updateOtherAttachment(attachment)
        }

        val actualExecutionEndDT = LocalDateTime.now().toString()
        val otherTaskExecutionResult = OthersTaskExecutionResultMO()

        if (task == null) {
            showToast("Unable to process the task...")
            return
        }

        if (attachment != null && !TextUtils.isEmpty(attachment.localFilePath)) {
            uuid = attachment.attachmentGuid
        }

        otherTaskExecutionResult.otherActivity =
            OtherActivity(uuid, description.get().toString(), task.reasonLookUpIdentifier)

        val executionResult = GsonUtils.toJsonWithoutExopse().toJson(otherTaskExecutionResult)
        val location = Prefs.getDouble(PrefConstants.LATITUDE).toString() + ", " + Prefs.getDouble(
            PrefConstants.LONGITUDE).toString()

        addDisposable(taskDao.updateTaskOnFinish(Prefs.getInt(PrefConstants.CURRENT_TASK),
            actualExecutionEndDT, executionResult, location)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                addTimeLine()

                triggerRotaTaskWorker()
                message.what = NavigationConstants.ON_OTHER_TASK_COMPLETE
                liveData.postValue(message)

            }, {
                Timber.e(it)
                showToast("Unable to process the task...")
            }))
    }

    private fun triggerRotaTaskWorker() {
        val inputData = Data.Builder().putInt(RotaTaskWorker::class.java.simpleName,
            RotaTaskWorker.RotaTaskWorkerType.SYNC_TO_SERVER.workerType).build()
        oneTimeWorkerWithInputData(RotaTaskWorker::class.java, inputData)
    }

    private fun updateOtherAttachment(attachment: AttachmentEntity) {
        val taskId = Prefs.getInt(PrefConstants.CURRENT_TASK)
        val task = otherTask.get()
        addDisposable(
            taskDao.getAttachmentTypeForOther(attachment.attachmentSourceType,
                attachment.attachmentGuid, taskId)
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
                    metaData.siteId = task!!.siteId
                    metaData.taskId = it.serverId!!
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

    private fun addTimeLine() {
        val dailyTimeline = DailyTimeLineEntity(otherTaskType.get(), "")
        addDisposable(dailyTimeLineDao.insert(dailyTimeline)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("Time Line added")
            }, { t: Throwable? ->
                Timber.e(t)
            }))
    }
}