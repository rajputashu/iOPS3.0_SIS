package com.sisindia.ai.android.features.issues.grievance

import android.app.Application
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.widget.DatePicker
import androidx.databinding.ObservableField
import androidx.work.Data
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.features.issues.ActionPlanListener
import com.sisindia.ai.android.features.issues.SiteListListeners
import com.sisindia.ai.android.models.AudioPlayState
import com.sisindia.ai.android.models.AudioRecordState
import com.sisindia.ai.android.models.LookUpType
import com.sisindia.ai.android.room.dao.*
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.room.entities.GrievanceEntity
import com.sisindia.ai.android.room.entities.SiteEntity
import com.sisindia.ai.android.uimodels.ActionPlanModel
import com.sisindia.ai.android.uimodels.GrievanceCategory
import com.sisindia.ai.android.uimodels.GuardSuggestionItem
import com.sisindia.ai.android.utils.FileUtils
import com.sisindia.ai.android.workers.GrievanceWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.*
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.TemporalAdjusters
import timber.log.Timber
import java.io.IOException
import java.util.*
import javax.inject.Inject

class CreateGrievanceIssueViewModel @Inject constructor(val appln: Application) :
    IopsBaseViewModel(appln) {

    @Inject
    lateinit var siteDao: SiteDao

    @Inject
    lateinit var employeeSiteDao: EmployeeSiteDao

    @Inject
    lateinit var lookUpDao: LookUpDao

    @Inject
    lateinit var actionPlanDao: ActionPlanDao

    @Inject
    lateinit var attachmentDao: AttachmentDao

    @Inject
    lateinit var grievanceDao: GrievanceDao

    var sites = ObservableField<List<SiteEntity>>(ArrayList())
    var selectedSiteObs = ObservableField(SiteEntity(0, "Select unit name"))
    var allGuards = ObservableField<List<GuardSuggestionItem>>(ArrayList())
    var selectedGuardObs = ObservableField(GuardSuggestionItem(0))
    var isRecordedObs = ObservableField(AudioRecordState.NOT_RECORDED)
    var btnPlayState = ObservableField(AudioPlayState.PAUSE)
    var categoryObs = ObservableField<List<GrievanceCategory>>(ArrayList())
    var selectedCategoryObs = ObservableField(GrievanceCategory(0))
    var actionPlanObs = ObservableField<List<ActionPlanModel>>(ArrayList())
    var description = ObservableField("")
    var selectedActionPlanObs = ObservableField(ActionPlanModel())
    var targetDate = ObservableField<LocalDateTime>()

    private var audioAttachment: AttachmentEntity? = null
    private var mediaPlayer: MediaPlayer? = null

    var viewListeners = object : GuardSuggestionViewListeners {

        override fun onGuardSelected(item: GuardSuggestionItem) {
            setSelectedGuard(item)
        }

        override fun fetchGuardFromServer(employeeNo: String) {

        }
    }

    var siteListListener = object : SiteListListeners {
        override fun onSiteSelected(item: SiteEntity) {
            setSelectedSite(item)
        }
    }

    var actionPlanListener = object : ActionPlanListener {
        override fun onActionPlanSelected(item: ActionPlanModel) {
            setSelectedActionPlan(item)
        }
    }

    private fun setSelectedActionPlan(item: ActionPlanModel) {
        selectedActionPlanObs.set(item)
        if (item == null || item.id == 0) {
            return
        }

        targetDate.set(Instant.now()
            .plus(item.closureDays.toLong(), ChronoUnit.DAYS)
            .atZone(ZoneId.systemDefault()).toLocalDateTime())
    }

    fun onCheckedChange(chipGroup: ChipGroup, check: Int) {
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.isChecked) {
                val category = chip.tag as GrievanceCategory
                if (category != null) {
                    selectedCategoryObs.set(category)
                    getAllActionPlan(category.lookupIdentifier)
                }
                return
            } else {
                selectedCategoryObs.set(GrievanceCategory())
            }
        }
    }

    private fun getAllActionPlan(categoryId: Int) {
        //fetch all action plans
        actionPlanObs.set(ArrayList<ActionPlanModel>())
        addDisposable(actionPlanDao.fetchAllForGrievance(categoryId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isNullOrEmpty()) {
                    showToast("No Action plan found")
                } else {
                    setSelectedActionPlan(it[0])
                }
            }, Timber::e))
    }

    private fun setSelectedGuard(item: GuardSuggestionItem) {
        selectedGuardObs.set(item)
    }

    private fun setSelectedSite(item: SiteEntity) {
        selectedSiteObs.set(item)
        addDisposable(employeeSiteDao.fetchAllGuardsBySiteId(item.id)
            .compose(transformSingle())
            .subscribe({
                allGuards.set(it)
            }, Timber::e))
    }

    fun initViewModel() {
        addDisposable(siteDao.fetchAllActiveSites(true).compose(transformSingle())
            .subscribe({
                it.add(0, SiteEntity(0, "Select unit name"))
                sites.set(it)
            }, Timber::e))


        //fetch all category from lookUp
        addDisposable(lookUpDao.fetchAllCategoryForGrievance(LookUpType.GRIEVANCE_CATEGORY.typeId)
            .compose(transformSingle())
            .subscribe({
                categoryObs.set(it)
            }, Timber::e))
    }

    fun onAudioRecorded(audioFile: String) {
        if (TextUtils.isEmpty(audioFile)) {
            showToast("unable to save audio.. please record again.")
            isRecordedObs.set(AudioRecordState.NOT_RECORDED)
            return
        }
        isRecordedObs.set(AudioRecordState.RECORDED)
        val uri =
            Uri.fromFile(FileUtils.getFileByPath(audioFile))
        prepareMediaPlayer(audioFile)
        audioAttachment = AttachmentEntity(uri.toString())
    }

    private fun prepareMediaPlayer(audioFile: String) {
        mediaPlayer = MediaPlayer()
        try {
            mediaPlayer!!.setDataSource(audioFile)
            mediaPlayer!!.prepare()
            mediaPlayer!!.setOnCompletionListener {
                onPlayMedia(null)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun onPlayMedia(view: View?) {
        val state = btnPlayState.get()
        if (state != null) {
            when (state) {
                AudioPlayState.PAUSE -> {
                    btnPlayState.set(AudioPlayState.PLAY)
                    mediaPlayer?.start()
                }
                AudioPlayState.PLAY -> {
                    btnPlayState.set(AudioPlayState.PAUSE)
                    if (mediaPlayer != null) {
                        mediaPlayer!!.pause()
                    }
                }
            }
        }
    }

    fun onAddAudioClick(view: View) {
        message.what = NavigationConstants.ON_ADD_AUDIO_CLIP_CLICK
        liveData.postValue(message)
    }


    fun onScanGuardQrClick(view: View?) {
        message.what = NavigationConstants.ON_SCAN_GUARD_QR_CLICK
        liveData.postValue(message)
    }

    fun onGuardQrScanned(qrRawData: String) {
        // QR code find the guard and site details
        Timber.e("QRCODE : $qrRawData")
    }

    fun onEnterGuardClick(view: View) {

        val site = selectedSiteObs.get() as SiteEntity
        if (site == null || site.id == 0) {
            showToast("Please Select Site")
            return
        }
    }

    fun onSetTargetDateClick(view: View) {
        val selectedActionPlan = selectedActionPlanObs.get()
        if (selectedActionPlan == null || selectedActionPlan.id == 0) {
            showToast("Please Select Action Plan")
            return
        }

        val selectedYear = LocalDate.now().year
        val selectedMonth = LocalDate.now().monthValue - 1
        val selectedDayOfMonth = LocalDate.now().dayOfMonth
        val currentSunday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
        val nextSunday = currentSunday.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
        val maxDate = Calendar.getInstance()
        maxDate[Calendar.YEAR] = nextSunday.year
        maxDate[Calendar.MONTH] = nextSunday.monthValue - 1
        maxDate[Calendar.DAY_OF_MONTH] = nextSunday.dayOfMonth

        val datePickerDialog = DatePickerDialog(view.context,
            R.style.DatePickerTheme,
            OnDateSetListener { datePickerView: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                val currentDateTime =
                    LocalDateTime.now()
                targetDate.set(LocalDateTime.of(year,
                    month + 1,
                    dayOfMonth,
                    currentDateTime.hour,
                    currentDateTime.minute,
                    currentDateTime.second))
            }, selectedYear, selectedMonth, selectedDayOfMonth)
        datePickerDialog.datePicker.minDate = Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli()
        datePickerDialog.datePicker.maxDate =
            Instant.now().plus(selectedActionPlan.closureDays.toLong(), ChronoUnit.DAYS)
                .toEpochMilli()
        datePickerDialog.show()

        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
    }

    fun onAddGrievanceClick(view: View?) {

        val site: SiteEntity = selectedSiteObs.get() as SiteEntity
        val selectedTargetDateTime = targetDate.get()

        if (site == null || site.id == 0) {
            showToast("Please select Unit")
            return
        }

        val guard: GuardSuggestionItem = selectedGuardObs.get() as GuardSuggestionItem

        if (guard == null || guard.employeeId == 0) {
            showToast("Please select Employee")
            return
        }


        val category: GrievanceCategory = selectedCategoryObs.get() as GrievanceCategory
        if (category == null || category.id == 0) {
            showToast("Please select category")
            return
        }
        val actionPlanModel: ActionPlanModel = selectedActionPlanObs.get() as ActionPlanModel
        if (actionPlanModel == null || actionPlanModel.id == 0) {
            showToast("Please select action plan")
            return
        }
        val descriptionText = description.get() as String
        if (audioAttachment == null && TextUtils.isEmpty(descriptionText)) {
            showToast("Please add Description / Record audio")
            return
        }

        if (selectedTargetDateTime == null) {
            showToast("Invalid target date")
            return
        }
        if (audioAttachment != null) {
            addDisposable(attachmentDao.insert(audioAttachment)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    saveGrievance(guard.employeeId, site.id,
                        category.lookupIdentifier,
                        descriptionText,
                        actionPlanModel.id,
                        selectedTargetDateTime.toString(),
                        it)
                }, Timber::e))
        } else {
            saveGrievance(guard.employeeId, site.id,
                category.lookupIdentifier,
                descriptionText,
                actionPlanModel.id,
                selectedTargetDateTime.toString(),
                0L)
        }
    }

    private fun saveGrievance(employeeId: Int, siteId: Int,
        categoryId: Int,
        descriptionText: String,
        actionPlanId: Int,
        targetDate: String,
        attachmentId: Long) {


        val item = GrievanceEntity(employeeId, categoryId, descriptionText,
            actionPlanId, targetDate, 0, 0, siteId, attachmentId)

        addDisposable(grievanceDao.insert(item)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ row: Long ->
                val inputData = Data.Builder()
                    .putInt(GrievanceWorker::class.java.simpleName,
                        GrievanceWorker.GrievanceWorkerType.SYNC_TO_SERVER.workerType)
                    .build()
                oneTimeWorkerWithInputData(GrievanceWorker::class.java, inputData)
                message.what = NavigationConstants.ON_ISSUE_GRIEVANCE_CREATED
                message.arg1 = row.toInt()
                liveData.postValue(message)
            }
            ) { t: Throwable? ->
                Timber.e(t)
            })
    }
}