package com.sisindia.ai.android.features.uar.add

import android.app.Application
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.commons.SpinnerTypes
import com.sisindia.ai.android.commons.SpinnersListener
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.models.poa.AddImprovementPlanBodyMO
import com.sisindia.ai.android.models.poa.AddPoaBodyMO
import com.sisindia.ai.android.models.poa.AddSiteAtRiskBodyMO
import com.sisindia.ai.android.models.poa.SiteRiskReasonsMO
import com.sisindia.ai.android.models.poa.UarEmployeeDataMO
import com.sisindia.ai.android.room.dao.ImprovementPoaDao
import com.sisindia.ai.android.room.dao.SiteAtRiskDao
import com.sisindia.ai.android.room.dao.SiteDao
import com.sisindia.ai.android.room.dao.SiteRiskPoaDao
import com.sisindia.ai.android.room.entities.ImprovementPoaEntity
import com.sisindia.ai.android.room.entities.LookUpEntity
import com.sisindia.ai.android.room.entities.SiteAtRiskEntity
import com.sisindia.ai.android.room.entities.SiteEntity
import com.sisindia.ai.android.room.entities.SiteRiskPoaEntity
import com.sisindia.ai.android.room.entities.SiteRiskReasonsEntity
import com.sisindia.ai.android.utils.IopsUtil
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import javax.inject.Inject

class AddPoaAndIpViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    val toolBarLabel = ObservableField("Add Site At Risk")
    val isComingToAddPoa = ObservableBoolean(true)
    val workAssignedToList = ObservableField<List<UarEmployeeDataMO>>()

    @Inject
    lateinit var siteDao: SiteDao

    //------------------SITE AT RISK VARIABLES AND METHODS-----------------------//
    @Inject
    lateinit var siteRiskPoaDao: SiteRiskPoaDao

    @Inject
    lateinit var siteAtRiskDao: SiteAtRiskDao

    private lateinit var siteAtRiskApiBody: AddSiteAtRiskBodyMO

    val riskCategoryList = ObservableField<List<LookUpEntity>>()
    val reasonForRiskList = ObservableField<List<LookUpEntity>>()
    var selectedSiteRiskCategoryPos = -1
    var selectedReasonForRiskPos = -1
    //---------------------------------END---------------------------------------//

    //------------------ADD POA VARIABLES AND METHODS-----------------------//
    val poaTargetDate = ObservableField(LocalDate.now())
    val poaTypeList = ObservableField<List<LookUpEntity>>()
    val actionPointList = ObservableField<List<PoaActionPointMO>>()
    val addPoaRiskRemarks = ObservableField("")
    var selectedPoaTypePos = -1
    var selectedActionPointPos = -1
    var selectedWorkAssignedTo = -1
    var selectedSitePos = 0
    var siteSpinnerListener: SpinnersListener = object : SpinnersListener {
        override fun onSpinnerOptionSelected(pos: Int) {
            selectedSitePos = pos
        }

        override fun onSpinnerOptionSelected(item: Any) {}
    }

    //---------------------------------END---------------------------------------//

    //------------------ADD IMPROVEMENT PLAN VARIABLES AND METHODS-----------------------//
    @Inject
    lateinit var improvementDao: ImprovementPoaDao

    val ipTargetDate = ObservableField(LocalDate.now())
    val ipPlanTypeList = ObservableField<List<LookUpEntity>>()
    val ipCategoryList = ObservableField<List<LookUpEntity>>()
    val ipActionPointList = ObservableField<List<PoaActionPointMO>>()
    val addIpRiskRemarks = ObservableField("")
    var selectedIpPlanTypePos = -1
    var selectedIpCategoryPos = -1
    var selectedIpActionPointPos = -1
    //---------------------------------END---------------------------------------//

    val listener = object : SpinnersListener {
        override fun onSpinnerOptionSelected(pos: Int) {

        }

        override fun onSpinnerOptionSelected(item: Any) {

        }

        override fun onSpinnerItemAndTypeSelected(pos: Int, typeId: Int) {
            when (typeId) {
                SpinnerTypes.RISK_CATEGORY.typeId -> {
                    selectedSiteRiskCategoryPos = pos
                    fetchReasonsForRiskViaCategoryId(riskCategoryList.get()!![pos].lookupIdentifier)
                }
                SpinnerTypes.REASON_FOR_RISK.typeId -> {
                    selectedReasonForRiskPos = pos
                }
                SpinnerTypes.POA_TYPE.typeId -> {
                    selectedPoaTypePos = pos
                    fetchActionPlansViaCategoryId(poaTypeList.get()!![pos].lookupIdentifier)
                }
                SpinnerTypes.ACTION_POINT.typeId -> {
                    selectedActionPointPos = pos
                }
                SpinnerTypes.IP_PLAN_TYPE.typeId -> {
                    selectedIpPlanTypePos = pos
                }
                SpinnerTypes.IP_CATEGORY.typeId -> {
                    selectedIpCategoryPos = pos
                    fetchActionPlansForIPViaCategoryId(ipCategoryList.get()!![pos].lookupIdentifier)
                }
                SpinnerTypes.IP_ACTION_POINT.typeId -> {
                    selectedIpActionPointPos = pos
                }
                SpinnerTypes.ASSIGNED_TO.typeId -> {
                    selectedWorkAssignedTo = pos
                }
            }
        }
    }

    fun initSiteAtRiskUI() {
        addDisposable(siteRiskPoaDao.getLookUpDataViaTypeId(63)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isNotEmpty())
                    riskCategoryList.set(it)
            }, {
            }))
    }

    private fun fetchReasonsForRiskViaCategoryId(lookupIdentifierId: Int) {
        addDisposable(siteRiskPoaDao.getReasonForRiskViaId(lookupIdentifierId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isNotEmpty())
                    reasonForRiskList.set(it)
            }, {
            }))
    }

    fun initCreatePoaUI() {
        getSitesData()
        getEmployeesDataFromAPI()
        addDisposable(siteRiskPoaDao.getLookUpDataViaTypeId(23)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isNotEmpty())
                    poaTypeList.set(it)
            }, {
            }))
    }

    private fun fetchActionPlansViaCategoryId(lookupIdentifierId: Int) {
        addDisposable(siteRiskPoaDao.getActionPointListViaId(23, lookupIdentifierId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isNotEmpty())
                    actionPointList.set(it)
            }, {
            }))
    }

    fun initCreateImprovementPlanUI() {
        getSitesData()
        getEmployeesDataFromAPI()
        addDisposable(Single.zip(siteRiskPoaDao.getLookUpDataViaTypeId(25),
            siteRiskPoaDao.getLookUpDataViaTypeId(26)) { ipTypeList, ipCategoryList ->
            onMapResult(ipTypeList, ipCategoryList)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

            }, {
            }))
    }

    private fun onMapResult(typeList: List<LookUpEntity>, categoryList: List<LookUpEntity>): Boolean {
        if (typeList.isNotEmpty())
            ipPlanTypeList.set(typeList)
        if (categoryList.isNotEmpty())
            ipCategoryList.set(categoryList)

        return true
    }

    private fun fetchActionPlansForIPViaCategoryId(lookupIdentifierId: Int) {
        addDisposable(siteRiskPoaDao.getActionPointListViaId(26, lookupIdentifierId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isNotEmpty())
                    ipActionPointList.set(it)
            }, {
            }))
    }

    private fun getEmployeesDataFromAPI() {
        isLoading.set(View.VISIBLE)
        addDisposable(coreApi.getSiteRiskAO()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseMO ->
                isLoading.set(View.GONE)
                if (responseMO.statusCode == 200) {
                    responseMO.employeeList?.let {
                        if (it.isNotEmpty()) {
                            workAssignedToList.set(it)
                        }
                    }
                } else
                    showToast(responseMO.statusMessage)
            }) {
                isLoading.set(View.GONE)
            })
    }

    val obsSitesSpinnerList = ObservableField(arrayListOf("Select Site"))
    private lateinit var siteListFromDB: List<SiteEntity>

    private fun getSitesData() {
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

    fun onViewClicks(view: View) {

        when (view.id) {
            R.id.markSiteAtRiskButton -> validateMarkSiteAtRisk()
            R.id.targetDatePicker -> {
                message.what = NavigationConstants.OPEN_DATE_PICKER
                liveData.postValue(message)
            }
            R.id.addPoaButton -> validateAddPoa()
            R.id.addImprovePlanButton -> validateAddImprovementPlan()
        }
    }

    private fun validateMarkSiteAtRisk() {
        message.what = NavigationConstants.OPEN_TO_ADD_POA
        liveData.postValue(message)
    }

    private fun validateAddPoa() {
        if (selectedSitePos == 0)
            showToast("Please select valid site from list")
        else {
            isLoading.set(View.VISIBLE)
            //First Call Add Site At Risk API and get siteRiskAdded id and pass to next API (Add POA)
            siteAtRiskApiBody = AddSiteAtRiskBodyMO()
            val siteRiskMO = SiteRiskReasonsMO()
            siteRiskMO.riskCategoryId =
                riskCategoryList.get()!![selectedSiteRiskCategoryPos].lookupIdentifier
            siteRiskMO.riskReasonId = reasonForRiskList.get()!![selectedReasonForRiskPos].lookupIdentifier
            siteAtRiskApiBody.siteRiskReasons = arrayListOf(siteRiskMO)
            siteAtRiskApiBody.siteId = siteListFromDB[selectedSitePos - 1].id

            if (IopsUtil.isInternetAvailable(getApplication())) {
                addDisposable(coreApi.addSiteAtRisk(siteAtRiskApiBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ responseMO ->
                        responseMO.id?.let {
                            addPoaToServerViaAPI(it)
                        }
                    }) {
                        isLoading.set(View.GONE)
                    })
            } else {
                isLoading.set(View.GONE)
                showToast("Please check your internet connection")
            }
        }
    }

    private fun addPoaToServerViaAPI(siteAtRiskId: Int) {

        /*
         * CREATING AT RISK ENTITY MODEL TO INSERT INTO 'SITE AT RISK ENTITY' TABLE
         */
        val siteAtRiskEntity = SiteAtRiskEntity()
        siteAtRiskEntity.id = siteAtRiskId
        siteAtRiskEntity.siteId = siteAtRiskApiBody.siteId
        siteAtRiskEntity.riskMonth = siteAtRiskApiBody.riskMonth
        siteAtRiskEntity.riskYear = siteAtRiskApiBody.riskYear
        siteAtRiskEntity.riskStatus = siteAtRiskApiBody.riskStatus
        siteAtRiskEntity.finalClosureDate = siteAtRiskApiBody.finalClosureDate
        siteAtRiskEntity.createdByAppId = siteAtRiskApiBody.createdByAppId

        /*
        * CREATING SITE RISK REASON MODEL TO INSERT INTO 'SITE_RISK_REASON_ENTITY' TABLE
        */
        val riskReasonEntity = SiteRiskReasonsEntity()
        riskReasonEntity.siteRiskId = siteAtRiskId
        if (siteAtRiskApiBody.siteRiskReasons?.isNotEmpty()!!) {
            siteAtRiskApiBody.siteRiskReasons?.let {
                riskReasonEntity.riskCategoryId = it[0].riskCategoryId
                riskReasonEntity.riskReasonId = it[0].riskReasonId
            }
        }

        val body = AddPoaBodyMO()
        body.siteRiskId = siteAtRiskId
        body.targetCompletionDate = poaTargetDate.get().toString()
        body.poAReason = poaTypeList.get()!![selectedPoaTypePos].lookupIdentifier
        body.atRiskActionPlan = actionPointList.get()!![selectedActionPointPos].actionPlanId!!
        if (workAssignedToList.get() != null && workAssignedToList.get()!!.isNotEmpty()) {
            body.assignedTo = workAssignedToList.get()!![selectedWorkAssignedTo].employeeId!!
        }

        //CREATING POA ENTITY MODEL TO INSERT INTO 'SITE RISK POA ENTITY' TABLE
        val siteRiskPoaEntity = SiteRiskPoaEntity()
        siteRiskPoaEntity.assignedTo = body.assignedTo
        siteRiskPoaEntity.siteRiskId = siteAtRiskId
        siteRiskPoaEntity.siteId = body.siteId
        siteRiskPoaEntity.poAReason = body.poAReason
        siteRiskPoaEntity.atRiskActionPlan = body.atRiskActionPlan
        siteRiskPoaEntity.targetCompletionDate = body.targetCompletionDate
        siteRiskPoaEntity.poAStatus = body.poAStatus
        siteRiskPoaEntity.createdByAppId = Prefs.getInt(PrefConstants.AREA_INSPECTOR_ID)
        siteRiskPoaEntity.isSynced = 1

        if (workAssignedToList.get()!!.isNotEmpty()) {
            siteRiskPoaEntity.assignedTo = workAssignedToList.get()!![selectedWorkAssignedTo].employeeId!!
            siteRiskPoaEntity.assignedToName =
                workAssignedToList.get()!![selectedWorkAssignedTo].employeeName
            siteRiskPoaEntity.assignedToEmployeeNo =
                workAssignedToList.get()!![selectedWorkAssignedTo].employeeNo
        }

        addDisposable(coreApi.addPOA(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseMO ->
                isLoading.set(View.GONE)
                if (responseMO.statusCode == 200) {
                    responseMO.id?.let {
                        siteRiskPoaEntity.id = it
                        insertSiteAtRiskAndAddedPoaToDB(siteAtRiskEntity,
                            riskReasonEntity, siteRiskPoaEntity)
                    }
                } else
                    showToast(responseMO.statusMessage)
            }) {
                isLoading.set(View.GONE)
            })
    }

    private fun validateAddImprovementPlan() {

        if (selectedSitePos == 0) {
            showToast("Please select valid site from list")
            return
        }

        isLoading.set(View.VISIBLE)
        val body = AddImprovementPlanBodyMO()
        val selectedSiteId = siteListFromDB[selectedSitePos - 1].id
        body.planType = ipPlanTypeList.get()!![selectedIpPlanTypePos].lookupIdentifier
        body.categoryId = ipCategoryList.get()!![selectedIpCategoryPos].lookupIdentifier
        body.actionPlanId = ipActionPointList.get()!![selectedIpActionPointPos].actionPlanId
        body.remarks = addIpRiskRemarks.get().toString()
        body.targetCompletionDate = ipTargetDate.get().toString()
        body.siteId = selectedSiteId

        val improvementEntity = ImprovementPoaEntity()
        improvementEntity.planType = body.planType!!
        improvementEntity.categoryId = body.categoryId!!
        improvementEntity.actionPlanId = body.actionPlanId!!
        improvementEntity.description = body.remarks
        improvementEntity.targetDateTime = body.targetCompletionDate
        improvementEntity.raisedDateTime = body.raisedDateTime
        improvementEntity.assignedDateTime = body.assignedDateTime
        improvementEntity.assignedTo = body.assignedTo
        improvementEntity.assignedToEmployeeName = body.assignedToEmployeeName
        improvementEntity.assignedToEmployeeNo = body.assignedToEmployeeNo
        improvementEntity.siteId = selectedSiteId

        addDisposable(coreApi.addImprovementPlan(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseMO ->
                isLoading.set(View.GONE)
                if (responseMO.statusCode == 200) {
                    responseMO.data?.id?.let {
                        improvementEntity.serverId = it
                        insertAddedImprovementPlanToDB(improvementEntity)
                    }
                } else
                    showToast(responseMO.statusMessage)
            }) {
                isLoading.set(View.GONE)
            })
    }

    private fun insertSiteAtRiskAndAddedPoaToDB(siteAtRiskEntity: SiteAtRiskEntity,
                                                riskReasonEntity: SiteRiskReasonsEntity,
                                                poaEntity: SiteRiskPoaEntity) {

        addDisposable(Maybe.zip(
            siteAtRiskDao.insert(siteAtRiskEntity),
            siteAtRiskDao.insertSiteRiskReason(riskReasonEntity),
            siteRiskPoaDao.insert(poaEntity))
        { _, _, _ ->
            true
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                showToast("POA added successfully")
                message.what = NavigationConstants.ON_ADDING_POA_OR_IP_SUCCESSFULLY
                liveData.postValue(message)
            }, { }))
    }

    private fun insertAddedImprovementPlanToDB(entity: ImprovementPoaEntity) {
        addDisposable(improvementDao.insert(entity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.set(View.GONE)
                message.what = NavigationConstants.ON_ADDING_POA_OR_IP_SUCCESSFULLY
                liveData.postValue(message)
            }) {
                isLoading.set(View.GONE)
            })
    }
}