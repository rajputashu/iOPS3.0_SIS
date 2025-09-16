package com.sisindia.ai.android.features.dynamictask

import android.app.Application
import android.net.Uri
import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.work.Data
import com.droidcommons.preference.Prefs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.commons.TaskControllerType.AUDIO
import com.sisindia.ai.android.commons.TaskControllerType.CHECKBOX
import com.sisindia.ai.android.commons.TaskControllerType.DATETIMEPICKER
import com.sisindia.ai.android.commons.TaskControllerType.LABEL
import com.sisindia.ai.android.commons.TaskControllerType.PICTURE
import com.sisindia.ai.android.commons.TaskControllerType.QRCODE
import com.sisindia.ai.android.commons.TaskControllerType.SPINNER
import com.sisindia.ai.android.commons.TaskControllerType.STATICSPINNER
import com.sisindia.ai.android.commons.TaskControllerType.TEXT
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.features.dynamictask.models.DynamicAudioMO
import com.sisindia.ai.android.features.dynamictask.models.DynamicCheckBoxMO
import com.sisindia.ai.android.features.dynamictask.models.DynamicDateTimePickerMO
import com.sisindia.ai.android.features.dynamictask.models.DynamicEditTextMO
import com.sisindia.ai.android.features.dynamictask.models.DynamicLabel
import com.sisindia.ai.android.features.dynamictask.models.DynamicPictureMO
import com.sisindia.ai.android.features.dynamictask.models.DynamicScanQrMO
import com.sisindia.ai.android.features.dynamictask.models.DynamicSpinnerMO
import com.sisindia.ai.android.features.dynamictask.models.DynamicStaticSpinnerMO
import com.sisindia.ai.android.features.dynamictask.models.DynamicTaskResultMO
import com.sisindia.ai.android.features.dynamictask.models.QuestionsMO
import com.sisindia.ai.android.features.nudges.DynamicTaskParserV2
import com.sisindia.ai.android.models.AudioRecordState
import com.sisindia.ai.android.room.dao.AttachmentDao
import com.sisindia.ai.android.room.dao.DynamicTaskDao
import com.sisindia.ai.android.room.dao.TaskDao
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.room.entities.TaskEntity
import com.sisindia.ai.android.uimodels.attachments.OtherTaskAttachmentMetaData
import com.sisindia.ai.android.uimodels.tasks.TimeElapsedMO
import com.sisindia.ai.android.utils.FileUtils
import com.sisindia.ai.android.workers.AttachmentsUploadWorker
import com.sisindia.ai.android.workers.RotaTaskWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu_Rajput on 6/2/2021.
 */
class DynamicTaskViewModel @Inject constructor(app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var dynamicTaskDao: DynamicTaskDao

    @Inject
    lateinit var taskDao: TaskDao

    @Inject
    lateinit var attachmentDao: AttachmentDao

    private var tik = TaskTimer(0)

    val obsDynamicTaskName = ObservableField("")
    val obsTaskTypeId = ObservableInt(1)

    var isRecordedObs = ObservableField(AudioRecordState.NOT_RECORDED)
    private var audioAttachment: AttachmentEntity =
        AttachmentEntity(AttachmentEntity.AttachmentSourceType.DYNAMIC_AUDIO)
    var imageAttachment =
        ObservableField(AttachmentEntity(AttachmentEntity.AttachmentSourceType.DYNAMIC_IMAGE))

    val dynamicTaskAdapter = DynamicTaskAdapter()
    var selectedCaptureImagePos = -1
    var selectedAudioPos = -1
    var selectedQRCodePos = -1

    val taskListener = object : DynamicTasksListener {
        override fun onClickPicture(picturePos: Int) {
            selectedCaptureImagePos = picturePos
            message.what = NavigationConstants.ON_TAKE_PICTURE
            message.obj = imageAttachment.get()
            liveData.postValue(message)
        }

        override fun onAddingAudio(position: Int) {
            selectedAudioPos = position
            message.what = NavigationConstants.ON_ADD_AUDIO_CLIP_CLICK
            liveData.postValue(message)
        }

        override fun onScanningQR(position: Int) {
            selectedQRCodePos = position
            message.what = NavigationConstants.OPEN_LIVE_QR_SCANNER_SCREEN
            liveData.postValue(message)
        }

        override fun onCheckBoxClicked(position: Int, checkedValue: Boolean) {
            (dynamicTaskAdapter.getItem(position) as DynamicCheckBoxMO).cbIsChecked = checkedValue
        }

        override fun onSpinnerSelected(position: Int, selectedValue: String) {
            if (dynamicTaskAdapter.getItem(position) is DynamicSpinnerMO) {
                (dynamicTaskAdapter.getItem(position) as DynamicSpinnerMO).selectedSpinnerValue =
                    selectedValue
            } else if (dynamicTaskAdapter.getItem(position) is DynamicStaticSpinnerMO) {
                (dynamicTaskAdapter.getItem(position) as DynamicStaticSpinnerMO).selectedSpinnerValue =
                    selectedValue
            }
        }
    }

    fun updateTaskExecutionStartDetails() {
        addDisposable(taskDao.updateTaskOnStartDayCheck(Prefs.getInt(PrefConstants.CURRENT_TASK),
            LocalDateTime.now().toString(), TaskEntity.TaskStatus.IN_PROGRESS.taskStatus)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
//                triggerRotaTaskWorker()
            }, { }))
    }

    fun updateImageCaptureStatus() {
        if (selectedCaptureImagePos != -1) {
            val uri = Uri.parse(imageAttachment.get()!!.localFilePath)
            (dynamicTaskAdapter.items[selectedCaptureImagePos] as DynamicPictureMO)
                .isImageCaptured = true
            (dynamicTaskAdapter.items[selectedCaptureImagePos] as DynamicPictureMO)
                .imageUri = uri
            dynamicTaskAdapter.notifyDataSetChanged()
        }
    }

    fun updateScannedQR(qrCode: String) {
        if (qrCode.isNotEmpty() && selectedQRCodePos != -1) {
            (dynamicTaskAdapter.items[selectedQRCodePos] as DynamicScanQrMO).scannedQrCode = qrCode
            showToast("QR Scanned successfully")
        }
    }

    /*fun fetchJsonFormViaId() {
        addDisposable(dynamicTaskDao.fetchDynamicForm(obsTaskTypeId.get())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ dynamicTaskForm ->
                dynamicTaskForm?.apply {
                    obsDynamicTaskName.set(this.taskName)
                    if (!this.form.isNullOrEmpty()) {
                        val listIntroType = object : TypeToken<List<DynamicTaskParser>>() {}.type
                        val taskParser: List<DynamicTaskParser> =
                            Gson().fromJson(this.form, listIntroType)
                        createDynamicTaskViews(taskParser)
                    }
                }
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
    }*/

    fun fetchJsonFormViaId() {
        addDisposable(dynamicTaskDao.fetchDynamicForm(obsTaskTypeId.get())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ dynamicTaskForm ->
                dynamicTaskForm?.apply {
                    obsDynamicTaskName.set(this.taskName)
                    if (!this.form.isNullOrEmpty()) {
                        val listIntroType = object : TypeToken<List<DynamicTaskParserV2>>() {}.type
                        val taskParser: List<DynamicTaskParserV2> =
                            Gson().fromJson(this.form, listIntroType)
                        createDynamicTaskViewsV2(taskParser)
                    }
                }
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
    }

    /*private fun createDynamicTaskViews(entryList: List<DynamicTaskParser>) {

        val taskParser: DynamicTaskParser = entryList[0]

        if (!taskParser.controllerList.isNullOrEmpty()) {
            val completeUiList = ArrayList<Any>()

            for (controls: MainControlMO in taskParser.controllerList!!) {

                when (controls.contentType) {
                    LABEL.name -> {
                        val widgetData = controls.data
                        completeUiList.add(DynamicLabel(label = widgetData?.title,
                            isMandatory = widgetData?.isMandatory))
                    }
                    CHECKBOX.name -> {
                        val widgetData = controls.data
                        completeUiList.add(DynamicCheckBoxMO(
                            controllerId = controls.ControlID,
                            controllerName = controls.contentType,
                            isMandatory = widgetData?.isMandatory!!.toBoolean(),
                            cbLabel = widgetData.title))
                    }
                    TEXT.name -> {
                        val widgetData = controls.data
                        completeUiList.add(DynamicEditTextMO(
                            controllerId = controls.ControlID,
                            controllerName = controls.contentType,
                            isMandatory = widgetData?.isMandatory!!.toBoolean(),
                            label = widgetData.title,
                            hint = widgetData.hint))
                    }
                    PICTURE.name -> {
                        val widgetData = controls.data
                        completeUiList.add(DynamicPictureMO(
                            controllerId = controls.ControlID,
                            controllerName = controls.contentType,
                            isMandatory = widgetData?.isMandatory!!.toBoolean(),
                            takePicCount = widgetData.count!!))
                    }
                    SPINNER.name -> {
                        val widgetData = controls.data
                        completeUiList.add(DynamicSpinnerMO(
                            controllerId = controls.ControlID,
                            controllerName = controls.contentType,
                            isMandatory = widgetData?.isMandatory!!.toBoolean(),
                            label = widgetData.title!!))
                    }
                    STATICSPINNER.name -> {
                        val widgetData = controls.data
                        val spinnerItems = widgetData?.dataValue!!
                        spinnerItems.add(0, "Select Option")
                        completeUiList.add(DynamicStaticSpinnerMO(
                            controllerId = controls.ControlID,
                            controllerName = controls.contentType,
                            isMandatory = widgetData.isMandatory!!.toBoolean(),
                            label = widgetData.title!!,
                            spinnerList = spinnerItems))
                    }
                    AUDIO.name -> {
                        val widgetData = controls.data
                        completeUiList.add(DynamicAudioMO(
                            controllerId = controls.ControlID,
                            controllerName = controls.contentType,
                            isMandatory = widgetData?.isMandatory!!.toBoolean(),
                            label = widgetData.title!!))
                    }
                    QRCODE.name -> {
                        val widgetData = controls.data
                        completeUiList.add(DynamicScanQrMO(
                            controllerId = controls.ControlID,
                            controllerName = controls.contentType,
                            isMandatory = widgetData?.isMandatory!!.toBoolean(),
                            label = widgetData.title!!))
                    }
                }
            }
            dynamicTaskAdapter.clearAndSetItems(completeUiList)
        }
    }*/

    private fun createDynamicTaskViewsV2(entryList: List<DynamicTaskParserV2>) {

        if (entryList.isNotEmpty()) {
            val completeUiList = ArrayList<Any>()

            for (controls: DynamicTaskParserV2 in entryList) {

                when (controls.contentType) {
                    LABEL.name -> {
                        completeUiList.add(DynamicLabel(label = controls.title,
                            isMandatory = controls.isMandatory))
                    }

                    CHECKBOX.name -> {
                        //Title
                        completeUiList.add(DynamicLabel(label = controls.title))
                        controls.dataValue?.forEach {
                            completeUiList.add(DynamicCheckBoxMO(
                                controllerId = controls.ControlID,
                                controllerName = controls.contentType,
                                isMandatory = controls.isMandatory!!.toBoolean(),
                                cbLabel = it))
                        }
                    }

                    TEXT.name -> {
//                        val widgetData = controls.data
                        completeUiList.add(DynamicEditTextMO(
                            controllerId = controls.ControlID,
                            controllerName = controls.contentType,
                            isMandatory = controls.isMandatory!!.toBoolean(),
                            label = controls.title,
                            hint = controls.hint))
                    }

                    PICTURE.name -> {
                        completeUiList.add(DynamicPictureMO(
                            controllerId = controls.ControlID,
                            controllerName = controls.contentType,
                            isMandatory = controls.isMandatory!!.toBoolean(),
                            takePicCount = controls.count!!))
                    }

                    SPINNER.name -> {
                        completeUiList.add(DynamicSpinnerMO(
                            controllerId = controls.ControlID,
                            controllerName = controls.contentType,
                            isMandatory = controls.isMandatory!!.toBoolean(),
                            label = controls.title!!))
                    }

                    STATICSPINNER.name -> {
                        val spinnerItems = controls.dataValue!!
                        spinnerItems.add(0, "Select Option")
                        completeUiList.add(DynamicStaticSpinnerMO(
                            controllerId = controls.ControlID,
                            controllerName = controls.contentType,
                            isMandatory = controls.isMandatory!!.toBoolean(),
                            label = controls.title!!,
                            spinnerList = spinnerItems))
                    }

                    AUDIO.name -> {
                        completeUiList.add(DynamicAudioMO(
                            controllerId = controls.ControlID,
                            controllerName = controls.contentType,
                            isMandatory = controls.isMandatory!!.toBoolean(),
                            label = controls.title!!))
                    }

                    /*TaskControllerType.HEADCOUNTTEXT.name -> {
                        completeUiList.add(DynamicHeadCountMO(
                            controllerId = controls.ControlID,
                            controllerName = controls.contentType,
                            isMandatory = controls.isMandatory!!.toBoolean(),
                            label = controls.title,
                            hint = controls.hint))
                    }*/

                    QRCODE.name -> {
                        completeUiList.add(DynamicScanQrMO(
                            controllerId = controls.ControlID,
                            controllerName = controls.contentType,
                            isMandatory = controls.isMandatory!!.toBoolean(),
                            label = controls.title!!))
                    }

                    /*RATING.name -> {
                        completeUiList.add(DynamicRatingMO(
                            controllerId = controls.ControlID,
                            controllerName = controls.contentType,
                            isMandatory = controls.isMandatory!!.toBoolean(),
                            label = controls.title!!))
                    }*/

                    DATETIMEPICKER.name -> {
                        completeUiList.add(DynamicDateTimePickerMO(
                            controllerId = controls.ControlID,
                            controllerName = controls.contentType,
                            isMandatory = controls.isMandatory!!.toBoolean(),
                            label = controls.title!!))
                    }
                }
            }
            dynamicTaskAdapter.clearAndSetItems(completeUiList)
        }
    }

    fun onTaskValidation(view: View) {
        var isMandatoryViewsDone = true
        for (viewsMO: Any in dynamicTaskAdapter.items) {
//            if (viewsMO is DynamicStaticSpinnerMO && viewsMO.selectedSpinnerValue.equals("Select Option")) {
            if (viewsMO is DynamicStaticSpinnerMO && viewsMO.selectedSpinnerPosition == 0) {
                isMandatoryViewsDone = false
                showToast("Please select option for ${viewsMO.label}")
                break
            } else if (viewsMO is DynamicPictureMO && viewsMO.isMandatory && !viewsMO.isImageCaptured) {
                isMandatoryViewsDone = false
                showToast("Picture is mandatory, please click and upload")
                break
            } else if (viewsMO is DynamicEditTextMO && viewsMO.isMandatory && viewsMO.enteredValue.isNullOrEmpty()) {
                isMandatoryViewsDone = false
                showToast("Please enter valid ${viewsMO.label}")
                break
            } else if (viewsMO is DynamicScanQrMO && viewsMO.isMandatory && viewsMO.scannedQrCode.isNullOrEmpty()) {
                isMandatoryViewsDone = false
                showToast("Please scan QR Code")
                break
            } else if (viewsMO is DynamicAudioMO && viewsMO.isMandatory && viewsMO.audioPath.isNullOrEmpty()) {
                isMandatoryViewsDone = false
                showToast("Audio is mandatory, please record and save")
                break
            }
        }

        if (isMandatoryViewsDone) {
            isLoading.set(View.VISIBLE)
            val taskExecutionResult = DynamicTaskResultMO()
            taskExecutionResult.siteId = Prefs.getInt(PrefConstants.CURRENT_SITE)
            taskExecutionResult.taskId = Prefs.getInt(PrefConstants.TASK_SERVER_ID)
            taskExecutionResult.taskTypeId = obsTaskTypeId.get()
            val questionList = arrayListOf<QuestionsMO>()
            for (viewsMO: Any in dynamicTaskAdapter.items) {
                when (viewsMO) {
                    is DynamicEditTextMO -> questionList.add(QuestionsMO(
                        controlId = viewsMO.controllerId, controlName = viewsMO.controllerName,
                        question = viewsMO.label, response = viewsMO.enteredValue))
                    is DynamicPictureMO -> questionList.add(QuestionsMO(
                        controlId = viewsMO.controllerId, controlName = viewsMO.controllerName,
                        question = "TakeImage", response = imageAttachment.get()?.attachmentGuid))
                    is DynamicSpinnerMO -> questionList.add(QuestionsMO(
                        controlId = viewsMO.controllerId, controlName = viewsMO.controllerName,
                        question = viewsMO.label, response = viewsMO.selectedSpinnerValue))
                    is DynamicStaticSpinnerMO -> questionList.add(QuestionsMO(
                        controlId = viewsMO.controllerId, controlName = viewsMO.controllerName,
                        question = viewsMO.label, response = viewsMO.selectedSpinnerValue))
                    is DynamicAudioMO -> questionList.add(QuestionsMO(
                        controlId = viewsMO.controllerId, controlName = viewsMO.controllerName,
                        question = viewsMO.label, response = audioAttachment.attachmentGuid))
                    is DynamicScanQrMO -> questionList.add(QuestionsMO(
                        controlId = viewsMO.controllerId, controlName = viewsMO.controllerName,
                        question = viewsMO.label, response = viewsMO.scannedQrCode))
                    is DynamicCheckBoxMO -> {
                        questionList.add(QuestionsMO(question = viewsMO.cbLabel,
                            controlId = viewsMO.controllerId, controlName = viewsMO.controllerName,
                            response = if (viewsMO.cbIsChecked) "1" else "0"))
                    }
                }
            }
            taskExecutionResult.question = questionList
            val executionResult: String = Gson().toJson(taskExecutionResult)
//            Timber.e("REsult JSON $executionResult")
            updateTaskAndInsertAttachment(executionResult)
        }
    }

    private fun updateTaskAndInsertAttachment(executionResult: String) {
        val attachment = imageAttachment.get()

        if (attachment != null && !TextUtils.isEmpty(attachment.localFilePath))
            updateOtherAttachment(attachment)

        val actualExecutionEndDT = LocalDateTime.now().toString()
        val location = Prefs.getDouble(PrefConstants.LATITUDE).toString() + ", " +
                Prefs.getDouble(PrefConstants.LONGITUDE).toString()

        addDisposable(taskDao.updateTaskOnFinish(Prefs.getInt(PrefConstants.CURRENT_TASK),
            actualExecutionEndDT, executionResult, location)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.set(View.GONE)
                triggerRotaTaskWorker()
                oneTimeWorkerWithNetwork(AttachmentsUploadWorker::class.java)

                message.what = NavigationConstants.ON_DYNAMIC_TASK_COMPLETE
                liveData.postValue(message)
                showToast("Task saved successfully")

            }, {
                isLoading.set(View.GONE)
                showToast("Unable to save the task...")
            }))
    }

    private fun triggerRotaTaskWorker() {
        val inputData = Data.Builder().putInt(RotaTaskWorker::class.java.simpleName,
            RotaTaskWorker.RotaTaskWorkerType.SYNC_TO_SERVER.workerType).build()
        oneTimeWorkerWithInputData(RotaTaskWorker::class.java, inputData)
    }

    fun onAudioRecorded(audioFile: String) {
        if (TextUtils.isEmpty(audioFile)) {
            showToast("unable to save audio.. please record again.")
            isRecordedObs.set(AudioRecordState.NOT_RECORDED)
            return
        }

        isRecordedObs.set(AudioRecordState.RECORDED)
        val uri = Uri.fromFile(FileUtils.getFileByPath(audioFile))
        audioAttachment.fileSize = audioFile.length.toLong()
        audioAttachment.localFilePath = uri.toString()

        if (selectedAudioPos != -1) {
            val audioMO = dynamicTaskAdapter.items[selectedAudioPos] as DynamicAudioMO
            audioMO.audioPath = uri.toString()
        }

        addDisposable(
            taskDao.getAttachmentTypeForOther(audioAttachment.attachmentSourceType,
                audioAttachment.attachmentGuid, Prefs.getInt(PrefConstants.CURRENT_TASK))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    audioAttachment.storagePath = it.storagePath + FileUtils.THREE_GPP
                    val metaData = OtherTaskAttachmentMetaData()
                    metaData.attachmentTypeId = 2 //for Audio
                    metaData.attachmentSourceTypeId = audioAttachment.attachmentSourceType
                    metaData.uuid = audioAttachment.attachmentGuid
                    metaData.fileName = it.fileName + FileUtils.THREE_GPP
                    metaData.fileSize = audioAttachment.fileSize.toString()
                    metaData.storagePath = audioAttachment.storagePath
                    metaData.siteId = Prefs.getInt(PrefConstants.CURRENT_SITE)
                    metaData.taskId = it.serverId!!
                    audioAttachment.attachmentMetaData = Gson().toJson(metaData)
                    audioAttachment.isDone = true
                    insertAttachmentToDB(audioAttachment)
                }, Timber::e))
    }

    private fun updateOtherAttachment(attachment: AttachmentEntity) {
        addDisposable(
            taskDao.getAttachmentTypeForOther(attachment.attachmentSourceType,
                attachment.attachmentGuid, Prefs.getInt(PrefConstants.CURRENT_TASK))
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
                    metaData.siteId = Prefs.getInt(PrefConstants.CURRENT_SITE)
                    metaData.taskId = it.serverId!!
                    attachment.attachmentMetaData = Gson().toJson(metaData)
                    attachment.isDone = true

                    insertAttachmentToDB(attachment)
                }, Timber::e))
    }

    private fun insertAttachmentToDB(attachment: AttachmentEntity) {
        addDisposable(attachmentDao.insert(attachment)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("Attachment Added")
            }) { t: Throwable? ->
                Timber.e(t)
                showToast("Unable to process the other task attachment...")
            })
    }

    fun startTimer() {
        val taskId: Int = Prefs.getInt(PrefConstants.CURRENT_TASK)
        addDisposable(taskDao.fetchTimeSpentViaTaskId(taskId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ model: TimeElapsedMO ->
                tik = TaskTimer(model.timeElapsed - findTimeDifference(model.lastElapsedTime))
                tik.start()
            }, { t: Throwable? -> Timber.e(t) }))
    }

    fun stopTimer() {
        val taskId: Int = Prefs.getInt(PrefConstants.CURRENT_TASK)
        addDisposable(taskDao.updateTimeSpentByTaskId(taskId, tik.lastTik, currentMilliOfTheDay)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ tik.cancel() }, { t: Throwable? -> Timber.e(t) }))
    }
}