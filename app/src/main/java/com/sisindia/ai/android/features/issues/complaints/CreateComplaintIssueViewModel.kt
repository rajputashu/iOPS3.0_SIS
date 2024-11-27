package com.sisindia.ai.android.features.issues.complaints

import android.app.Application
import android.app.DatePickerDialog
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.widget.DatePicker
import androidx.databinding.ObservableField
import androidx.work.Data
import com.droidcommons.preference.Prefs
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.features.issues.ActionPlanListener
import com.sisindia.ai.android.features.issues.ClientSelectionListener
import com.sisindia.ai.android.features.issues.SiteListListeners
import com.sisindia.ai.android.models.AudioPlayState
import com.sisindia.ai.android.models.AudioRecordState
import com.sisindia.ai.android.models.LookUpType
import com.sisindia.ai.android.room.dao.*
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.room.entities.ComplaintEntity
import com.sisindia.ai.android.room.entities.SiteEntity
import com.sisindia.ai.android.uimodels.ActionPlanModel
import com.sisindia.ai.android.uimodels.ClientModel
import com.sisindia.ai.android.uimodels.ComplaintSection
import com.sisindia.ai.android.utils.FileUtils
import com.sisindia.ai.android.workers.ComplaintWorker
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.*
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.TemporalAdjusters
import timber.log.Timber
import java.io.IOException
import java.util.*
import javax.inject.Inject

class CreateComplaintIssueViewModel @Inject constructor(val appln: Application) :
    IopsBaseViewModel(appln) {


    @Inject
    lateinit var siteDao: SiteDao

    @Inject
    lateinit var lookUpDao: LookUpDao

    @Inject
    lateinit var actionPlanDao: ActionPlanDao

    @Inject
    lateinit var attachmentDao: AttachmentDao

    @Inject
    lateinit var complaintDao: ComplaintDao

    @Inject
    lateinit var customerContactDao: CustomerContactDao


    //lists
    var sitesObs = ObservableField<List<SiteEntity>>(ArrayList())
    var modesObs = ObservableField<List<ComplaintSection>>(ArrayList())
    var typesObs = ObservableField<List<ComplaintSection>>(ArrayList())
    var naturesObs = ObservableField<List<ComplaintSection>>(ArrayList())
    var actionPlanObs = ObservableField<List<ActionPlanModel>>(ArrayList())
    var clientAdapter = SingleSelectionClientListAdapter()

    //selected
    var selectedSiteObs = ObservableField(SiteEntity(0, "Select unit name"))
    var selectedModeObs = ObservableField(ComplaintSection())
    var selectedTypeObs = ObservableField(ComplaintSection())
    var selectedNatureObs = ObservableField(ComplaintSection())
    var selectedActionPlanObs = ObservableField(ActionPlanModel())
    var selectedClient = ObservableField(ClientModel())

    var isRecordedObs = ObservableField(AudioRecordState.NOT_RECORDED)
    var btnPlayState = ObservableField(AudioPlayState.PAUSE)

    var remarks = ObservableField("")
    var targetDate = ObservableField<LocalDateTime>()


    private var audioAttachment: AttachmentEntity? = null
    private var mediaPlayer: MediaPlayer? = null


    fun initViewModel() {
        addDisposable(
            Single.zip(
                siteDao.fetchAllActiveSites(true),
                lookUpDao.fetchAllComplaintSections(LookUpType.COMPLAINT_MODE.typeId),
                lookUpDao.fetchAllComplaintSections(LookUpType.COMPLAINT_TYPE.typeId),
                Function3<MutableList<SiteEntity>, List<ComplaintSection>, List<ComplaintSection>, Boolean> { sites, modes, types ->
                    sites.add(0, SiteEntity(0, "Select unit name"))
                    sitesObs.set(sites)
                    modesObs.set(modes)
                    typesObs.set(types)
                    true
                }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("Done")
                }
                ) { t: Throwable? ->
                    Timber.e(t)
                })
    }

    var siteListListener = object : SiteListListeners {
        override fun onSiteSelected(item: SiteEntity) {
            Prefs.putInt(PrefConstants.CURRENT_SITE, item.id)
            selectedSiteObs.set(item)
            selectedClient.set(ClientModel())
            clientAdapter.clearClientAdapter()
            getAllSiteClients()
        }
    }

    var clientSelectionListener = object : ClientSelectionListener {

        override fun onClientSelected(model: ClientModel) {
            selectedClient.set(model)
        }
    }

    fun getAllSiteClients() {

        val siteId = if (selectedSiteObs.get() != null) selectedSiteObs.get()!!.id else 0

        clientAdapter.clear()
        addDisposable(customerContactDao.fetchAllCustomerBySite(siteId).compose(transformSingle())
            .subscribe({
                clientAdapter.clearAndSetItems(it)
            }, Timber::e))
    }

    var actionPlanListener = object : ActionPlanListener {

        override fun onActionPlanSelected(item: ActionPlanModel) {
            setSelectedActionPlan(item)
        }
    }

    private fun setSelectedActionPlan(item: ActionPlanModel) {
        selectedActionPlanObs.set(item)

        if (item.id == 0) {
            return
        }

        targetDate.set(Instant.now()
            .plus(item.closureDays.toLong(), ChronoUnit.DAYS)
            .atZone(ZoneId.systemDefault()).toLocalDateTime())
    }

    fun getAllActionPlan() {
        //fetch all action plans
        val type = selectedTypeObs.get() as ComplaintSection
        val nature = selectedNatureObs.get() as ComplaintSection

        actionPlanObs.set(ArrayList())
        addDisposable(actionPlanDao.fetchAllForComplaint(type.lookupIdentifier,
            nature.lookupIdentifier)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.add(0,
                    ActionPlanModel("Select Action Plan"))
                actionPlanObs.set(it)
            }, Timber::e))
    }


    fun onComplaintModeChanged(chipGroup: ChipGroup, check: Int) {
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.isChecked) {
                val category = chip.tag as ComplaintSection
                selectedModeObs.set(category)
                return
            } else {
                selectedModeObs.set(ComplaintSection())
            }
        }
    }

    fun onComplaintTypeChanged(chipGroup: ChipGroup, check: Int) {
        naturesObs.set(emptyList())
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.isChecked) {
                val category = chip.tag as ComplaintSection
                selectedTypeObs.set(category)
                fetchAllNatures(category)
                return
            } else {
                selectedTypeObs.set(ComplaintSection())
            }
        }
    }

    private fun fetchAllNatures(category: ComplaintSection) {
        addDisposable(
            lookUpDao.fetchAllComplaintNature(LookUpType.COMPLAINT_NATURE.typeId,
                category.lookupIdentifier)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(naturesObs::set, Timber::e))
    }

    fun onComplaintNatureChanged(chipGroup: ChipGroup, check: Int) {
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.isChecked) {
                val category = chip.tag as ComplaintSection
                selectedNatureObs.set(category)
                getAllActionPlan()
                return
            } else {
                selectedNatureObs.set(ComplaintSection())
            }
        }
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
            DatePickerDialog.OnDateSetListener { datePickerView: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
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

    fun onAddComplaintClick(view: View?) {

        val selectedTargetDateTime = targetDate.get()

        val mode = selectedModeObs.get() as ComplaintSection

        if (mode.lookupIdentifier == 0) {
            toast("Please select complaint mode.")
            return
        }

        val type = selectedTypeObs.get() as ComplaintSection
        if (type.lookupIdentifier == 0) {
            toast("Please select complaint type.")
            return
        }

        val nature = selectedNatureObs.get() as ComplaintSection
        if (nature.lookupIdentifier == 0) {
            toast("Please select complaint nature.")
            return
        }

        val site = selectedSiteObs.get() as SiteEntity
        if (site.id == 0) {
            toast("Please select Unit.")
            return
        }

        val client = selectedClient.get() as ClientModel
        if (client.id == 0) {
            toast("Please select Client.")
            return
        }

        val actionPlan = selectedActionPlanObs.get() as ActionPlanModel
        if (actionPlan.id == 0) {
            toast("Please select complaint actionPlan.")
            return
        }

        val additionalRemarks = remarks.get() as String
        if (audioAttachment == null && TextUtils.isEmpty(additionalRemarks)) {
            toast("Please add complaint additional Remarks OR Record Audio.")
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
                    saveComplaint(site.id, client.id, it.toInt(),
                        mode,
                        type,
                        nature,
                        actionPlan.id,
                        additionalRemarks,
                        selectedTargetDateTime.toString(), client.customerContactId)
                }, Timber::e))
        } else {
            saveComplaint(site.id, client.id, 0,
                mode,
                type,
                nature,
                actionPlan.id,
                additionalRemarks,
                selectedTargetDateTime.toString(), client.customerContactId)
        }
    }

    private fun saveComplaint(siteId: Int, clientId: Int, attachmentId: Int,
        mode: ComplaintSection,
        type: ComplaintSection,
        nature: ComplaintSection,
        actionPlanId: Int,
        description: String,
        targetCompletionDate: String, customerContactId: Int) {
        val item = ComplaintEntity(clientId, 0,
            siteId,
            attachmentId,
            mode.lookupIdentifier,
            type.lookupIdentifier,
            nature.lookupIdentifier,
            actionPlanId,
            description,
            targetCompletionDate, customerContactId)

        addDisposable(complaintDao.insert(item)
            .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val inputData = Data.Builder()
                    .putInt(ComplaintWorker::class.java.simpleName,
                        ComplaintWorker.ComplaintWorkerType.SYNC_TO_SERVER.workerType)
                    .build()
                oneTimeWorkerWithInputData(ComplaintWorker::class.java, inputData)
                message.what = NavigationConstants.ON_CLIENT_COMPLAINT_ADDED
                message.arg1 = it.toInt()
                liveData.postValue(message)
            }, Timber::e))
    }


    fun onAddClientClick(view: View) {

        val site = selectedSiteObs.get() as SiteEntity
        if (site.id != 0) {
            message.what = NavigationConstants.OPEN_ADD_CLIENT_BOTTOM_SHEET
            liveData.postValue(message)
        } else {
            toast("Please select Site to Add Client")
        }

    }

}