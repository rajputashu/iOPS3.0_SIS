package com.sisindia.ai.android.features.civil

import android.app.Application
import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.droidcommons.preference.Prefs
import com.google.gson.Gson
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.commons.SpinnersListener
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.models.civil.AddNominationBodyMO
import com.sisindia.ai.android.models.civil.CivilNominationMO
import com.sisindia.ai.android.room.dao.AttachmentDao
import com.sisindia.ai.android.room.dao.SiteDao
import com.sisindia.ai.android.room.dao.StateDistrictDao
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.room.entities.AttachmentEntity.AttachmentSourceType
import com.sisindia.ai.android.room.entities.DistrictsEntity
import com.sisindia.ai.android.room.entities.SiteEntity
import com.sisindia.ai.android.room.entities.StatesEntity
import com.sisindia.ai.android.uimodels.attachments.SelfieAttachmentMetadata
import com.sisindia.ai.android.utils.FileUtils
import com.sisindia.ai.android.workers.AttachmentsUploadWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 06/03/2025.
 */
class CivilDefenceViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var siteDao: SiteDao

    @Inject
    lateinit var stateDistrictDao: StateDistrictDao

    @Inject
    lateinit var attachmentDao: AttachmentDao

    val obsTodayCount = ObservableField("0")
    val obsTillDateCount = ObservableField("0")
    val obsTargetCount = ObservableField("0")
    val obsIsDataAvailable = ObservableBoolean(true)
    val nominationAdapter = CivilNominationAdapter()

    val obsStateSpinnerList = ObservableField(arrayListOf("Select State"))
    val obsDistrictSpinnerList = ObservableField(arrayListOf("Select District"))
    val obsSitesSpinnerList = ObservableField(arrayListOf("Select Site"))
    var selectedStatePos = 0
    var selectedDistrictPos = 0
    var selectedSitePos = 0
    val obsRegNo = ObservableField("")
    val obsTempRegNo = ObservableField("")
    val obsEmployeeName = ObservableField("")
    private lateinit var siteListFromDB: List<SiteEntity>
    private lateinit var stateListFromDB: List<StatesEntity>
    private lateinit var districtListFromDB: List<DistrictsEntity>
    private var selectedEmployeeId = 0

    var photoAttachmentObs =
        ObservableField(AttachmentEntity(AttachmentSourceType.CD_NOMINATION))

    var districtSpinnerListener: SpinnersListener = object : SpinnersListener {
        override fun onSpinnerOptionSelected(pos: Int) {
            selectedDistrictPos = pos
        }

        override fun onSpinnerOptionSelected(item: Any) {}
    }

    var siteSpinnerListener: SpinnersListener = object : SpinnersListener {
        override fun onSpinnerOptionSelected(pos: Int) {
            selectedSitePos = pos
        }

        override fun onSpinnerOptionSelected(item: Any) {}
    }

    var stateSpinnerListener: SpinnersListener = object : SpinnersListener {
        override fun onSpinnerOptionSelected(pos: Int) {
            selectedStatePos = pos
            if (pos > 0) {
                if (::stateListFromDB.isInitialized && stateListFromDB.isNotEmpty()) {
                    fetchDistrictsViaState(stateListFromDB[pos - 1].id!!)
                }
            } else {
                obsDistrictSpinnerList.set(arrayListOf("Select District"))
                selectedDistrictPos = 0
            }
        }

        override fun onSpinnerOptionSelected(item: Any) {}
    }

    fun onViewClicks(view: View) {
        when (view.id) {
            R.id.tbCivilDefence -> {
                message.what = NavigationConstants.OPEN_DASH_BOARD_DRAWER
                liveData.postValue(message)
            }
            R.id.addCivilNomination -> {
                message.what = NavigationConstants.ON_ADD_CIVIL_NOMINATION
                liveData.postValue(message)
            }
            R.id.addNominationButton -> {
                validateAddNomination()
            }
            R.id.regNoButton -> {
                validateRegNo()
            }
            R.id.takeEmployeePicLayout -> {
                message.what = NavigationConstants.ON_TAKE_PICTURE
                message.obj = photoAttachmentObs.get()
                liveData.postValue(message)
            }
        }
    }

    fun initDashboardUi() {
        getAddedNominationSummary()
    }

    private fun getAddedNominationSummary() {
        isLoading.set(View.VISIBLE)
        addDisposable(coreApi.nominationSummary
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.set(View.GONE)
                if (it.statusCode == 200 && !it.dataList.isNullOrEmpty()) {
                    val summaryList = arrayListOf<CivilNominationMO>()
                    var todayCount = 0
                    var tillDateCount = 0
                    var totalTarget = 0
                    it.dataList.forEach { mo ->
                        summaryList.add(CivilNominationMO(district = mo.districtName,
                            nomination = mo.tillDateCount))
                        todayCount += mo.todayCount
                        tillDateCount += mo.tillDateCount
                        totalTarget += mo.target
                    }
                    if (summaryList.isNotEmpty()) {
                        nominationAdapter.clearAndSetItems(summaryList)
                        obsTodayCount.set("" + todayCount)
                        obsTillDateCount.set("" + tillDateCount)
                        obsTargetCount.set("" + totalTarget)
                    }

                } else
                    showToast(it?.statusMessage)
            }, {
                isLoading.set(View.GONE)
                showToast("Internal server error, please try later...")
            }))
    }

    fun initAddNominationUI() {
        fetchStateFromDB()

        addDisposable(siteDao.fetchAllActiveSites(true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (!it.isNullOrEmpty()) {
                    siteListFromDB = it
                    val siteArray = arrayListOf("Select Site")
                    for (siteMO: SiteEntity in it) {
                        siteArray.add(siteMO.siteName!! + " (" + siteMO.siteCode + ")")
                    }
                    obsSitesSpinnerList.set(siteArray)
                }
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
    }

    private fun fetchStateFromDB() {
        addDisposable(stateDistrictDao.fetchAllStates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ stateList ->
                if (!stateList.isNullOrEmpty()) {
                    stateListFromDB = stateList
                    val stateArray = arrayListOf("Select State")
                    for (stateMO: StatesEntity in stateList) {
                        stateArray.add(stateMO.name!!)
                    }
                    obsStateSpinnerList.set(stateArray)
                }
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
    }

    private fun fetchDistrictsViaState(stateId: Int) {
        addDisposable(stateDistrictDao.fetchDistrictsViaState(stateId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ districtList ->
                if (!districtList.isNullOrEmpty()) {
                    districtListFromDB = districtList
                    val districtArray = arrayListOf("Select District")
                    for (districtMO: DistrictsEntity in districtList) {
                        districtArray.add(districtMO.name!!)
                    }
                    obsDistrictSpinnerList.set(districtArray)
                }
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
    }

    private fun validateRegNo() {

        if (obsRegNo.get().toString().isEmpty() ||
            obsRegNo.get().toString().trim().isEmpty() ||
            obsRegNo.get().toString().length < 7) {
            showToast("Please enter valid registration number")
            return
        }

        isLoading.set(View.VISIBLE)
        addDisposable(coreApi.getCivilEmpInformation(obsRegNo.get().toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.set(View.GONE)
                if (it.statusCode == 200) {
                    if (it.data?.empData != null) {
                        selectedEmployeeId = it.data.empData.employeeId
                        obsEmployeeName.set(it.data.empData.employeeName)
                    } else {
                        showToast(it.data?.msg)
                    }
                } else
                    showToast(it?.statusMessage)
            }, {
                isLoading.set(View.GONE)
                showToast("Internet not available, please try again...")
            }))
    }

    private fun validateAddNomination() {
        if (selectedStatePos == 0)
            showToast("Please select valid state from list")
        else if (selectedDistrictPos == 0)
            showToast("Please select valid district from list")
        else if (selectedSitePos == 0)
            showToast("Please select valid site from list")
        else if (obsRegNo.get().isNullOrEmpty())
            showToast("Please enter valid registration number")
        else if (obsEmployeeName.get().isNullOrEmpty())
            showToast("Please get employee information by pressing arrow button")
        else if (obsTempRegNo.get().isNullOrEmpty())
            showToast("Please enter valid temporary registration number id")
        else if (photoAttachmentObs.get() == null || TextUtils.isEmpty(photoAttachmentObs.get()!!.localFilePath)) {
            showToast("Its mandatory to take photo of employee")
        } else {
            insertAttachmentToDB()
        }
    }

    private fun addUpdateNominationToServer(imagePath: String) {

        isLoading.set(View.VISIBLE)
        val body = AddNominationBodyMO()
        body.stateId = stateListFromDB[selectedStatePos - 1].id
        body.districtId = districtListFromDB[selectedDistrictPos - 1].id
        body.siteId = siteListFromDB[selectedSitePos - 1].id
        body.employeeId = selectedEmployeeId
        body.tempRegNoId = obsTempRegNo.get()
        body.image = imagePath

        addDisposable(coreApi.addUpdateNomination(body).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({ responseMO ->
                isLoading.set(View.GONE)
                if (responseMO.statusCode == 200) {

                    showToast("Nomination added successfully")

                    oneTimeWorkerWithNetwork(AttachmentsUploadWorker::class.java)

                    message.what = NavigationConstants.ON_SUCCESSFUL_ADD_NOMINATION
                    liveData.postValue(message)

                } else {
                    showToast(responseMO.statusMessage)
                }

            }, { throwable: Throwable? ->
                isLoading.set(View.GONE)
                throwable!!.printStackTrace()
                showToast("Internal server error, please retry again")
            }))
    }

    private fun insertAttachmentToDB() {

        val attachment = photoAttachmentObs.get()
        val builder = StringBuilder()
        builder.append(attachment!!.attachmentSourceType)
        builder.append("_")
        builder.append(Prefs.getInt(PrefConstants.AREA_INSPECTOR_ID))
        builder.append("_")
        builder.append(stateListFromDB[selectedStatePos - 1].name)
        builder.append("_")
        builder.append(districtListFromDB[selectedDistrictPos - 1].name)
        builder.append("_")
        builder.append(siteListFromDB[selectedSitePos - 1].siteCode)
        builder.append("_")
        builder.append(selectedEmployeeId)
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
        metaData.storagePath = "Nomination/" + attachment.storagePath

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
                var userContainerSAS =
                    Prefs.getString(PrefConstants.SAS_USER_CONTAINER_KEY)
                userContainerSAS = userContainerSAS.replace("?", "/${metaData.storagePath}?")
                addUpdateNominationToServer(userContainerSAS)

            }) { t: Throwable? -> Timber.e(t) })
    }
}