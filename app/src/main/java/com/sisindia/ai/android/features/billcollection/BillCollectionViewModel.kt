package com.sisindia.ai.android.features.billcollection

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.droidcommons.base.timer.CountUpTimer
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.commons.CaptureImageType
import com.sisindia.ai.android.commons.CollectionMode
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.features.billcollection.adapter.BillCollectionAdapter
import com.sisindia.ai.android.features.billcollection.adapter.CollectionsCardAdapter
import com.sisindia.ai.android.features.billsubmit.BillCollectionRadioCheckedListener
import com.sisindia.ai.android.models.BillsForCollectionResponse
import com.sisindia.ai.android.room.dao.AttachmentDao
import com.sisindia.ai.android.room.dao.AttachmentMetadataDefinitionDao
import com.sisindia.ai.android.room.dao.BillCollectionDao
import com.sisindia.ai.android.room.dao.TaskDao
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.room.entities.BillCollectionsEntity
import com.sisindia.ai.android.room.entities.CacheBillCollectionEntity
import com.sisindia.ai.android.uimodels.collection.CollectionAttachmentMO
import com.sisindia.ai.android.uimodels.collection.CollectionCardDetailsMO
import com.sisindia.ai.android.uimodels.collection.CollectionsCountMO
import com.sisindia.ai.android.workers.AttachmentsUploadWorker
import com.sisindia.ai.android.workers.UpdateBillCollectionWorker
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 3/13/2020.
 */
class BillCollectionViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var timer: CountUpTimer

    @Inject
    lateinit var attachmentDao: AttachmentDao

    @Inject
    lateinit var collectionDao: BillCollectionDao

    @Inject
    lateinit var taskDao: TaskDao

    @Inject
    lateinit var metadataDefinitionDao: AttachmentMetadataDefinitionDao

    val unitName = ObservableField("")
    val dateOfCollection = ObservableField("")
    val totalBillAmount = ObservableField("")

    //    val billCollectionAtSiteAdapter = BillCollectionAtSiteAdapter()
    val billCollectionAtSiteAdapter = CollectionsCardAdapter()
    val billCollectionAdapter = BillCollectionAdapter()
    var chequeImageUri = ObservableField<Uri>()
    var chequeAttachmentEntity = ObservableField<AttachmentEntity>()
    var obsSiteId = ObservableInt()
    val obsCollectionMode = ObservableField(CollectionMode.CHEQUE)
    val isModeSelectedCheque = ObservableField(true)
    val obsActualCollectionAmount = ObservableField("")
    val obsComments = ObservableField("")
    val obsTransactionId = ObservableField("")
    private lateinit var generatedUUID: String

    //Bill Collection At Site Details Screen {Top Card obs}
    val obsBillDueCount = ObservableField("")
    val obsLacOutstandingCount = ObservableField("")
    val obsUnitsDueCount = ObservableField("")
    val obsLastUpdatedDate = ObservableField(Prefs.getString(PrefConstants.LAST_UPDATED_DATE, "NA"))

    val onRadioCheckedListener = object : BillCollectionRadioCheckedListener {
        override fun onRadioButtonChecked(collectionMode: CollectionMode) {
            obsCollectionMode.set(collectionMode)
            when (collectionMode) {
                CollectionMode.CHEQUE -> isModeSelectedCheque.set(true)
                CollectionMode.NEFT -> isModeSelectedCheque.set(false)
                CollectionMode.RTGS -> isModeSelectedCheque.set(false)
                CollectionMode.UPI -> isModeSelectedCheque.set(false)
            }
        }
    }

    var billCollectionListener = object : BillCollectionListener {
        override fun onBillSelected(collectionCardDetailsMO: Any) {
            message.what = NavigationConstants.OPEN_BILL_COLLECTION_TASK_FROM_CARD
            message.obj = collectionCardDetailsMO
            liveData.postValue(message)
        }
    }

    fun fetchCollectionAtSiteFromDB() {
        addDisposable(
            Single.zip(collectionDao.fetchCollectionsCount(),
                collectionDao.fetchPendingCollections(),
                collectionDao.fetchCompletedCollections(),
                Function3<CollectionsCountMO, List<CollectionCardDetailsMO>, List<CollectionCardDetailsMO>, ArrayList<Any>> { collectionCounts, pendingBillsList, completedBillsList ->
                    return@Function3 onBillCollectionDetailsFetch(collectionCounts,
                        pendingBillsList, completedBillsList
                    )
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    billCollectionAtSiteAdapter.updateCollectionData(it)
                }, {
                    it.printStackTrace()
                }))
    }

    private fun onBillCollectionDetailsFetch(countsDetails: CollectionsCountMO,
        pendingList: List<CollectionCardDetailsMO>,
        completedList: List<CollectionCardDetailsMO>): ArrayList<Any> {
        countsDetails.apply {
            obsBillDueCount.set(billDuesCount)
            obsLacOutstandingCount.set(lacsOutstandingCount)
            obsUnitsDueCount.set(unitsDueCount)
        }

        val finalList = arrayListOf<Any>()
        finalList.add("PENDING")
        if (pendingList.isNotEmpty())
            finalList.addAll(pendingList)
        finalList.add("COMPLETED")
        if (completedList.isNotEmpty())
            finalList.addAll(completedList)
        billCollectionAtSiteAdapter.updateCollectionData(finalList)
        return finalList
    }

    fun fetchDueCollectionViaSiteId() {
        addDisposable(collectionDao.fetchDueBillCollectionViaSiteId(obsSiteId.get())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ collectionListMO ->
                if (collectionListMO != null && collectionListMO.isNotEmpty())
                    billCollectionAdapter.clearAndSetItems(collectionListMO)
            }, { throwable: Throwable? ->
                //this.onError(throwable!!)
            })
        )
    }

    fun onPhotoOfCheque(view: View?) {
        generateFileNameAndStoragePath()
    }

    private fun generateFileNameAndStoragePath() {
        if (billCollectionAdapter.items.isNotEmpty()) {
            generatedUUID = UUID.randomUUID().toString()
            val entity = billCollectionAdapter.items[0]

            addDisposable(
                Single.zip(taskDao.getAttachmentTypeForBillCollection(
                    CaptureImageType.BILL_COLLECTION.attachmentSourceTypeId,
                    entity.billOutputCenterId, entity.id, entity.billingMonth,
                    entity.billingYear, generatedUUID
                ),
                    metadataDefinitionDao.fetchMetaDataJsonFormat(CaptureImageType.BILL_COLLECTION.attachmentSourceTypeId),
                    BiFunction<CollectionAttachmentMO, String, Boolean> { fileNameAndStoragePath, metaDataModel ->
                        return@BiFunction onResultFetch(fileNameAndStoragePath, metaDataModel)
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({}, {
                        it.printStackTrace()
                    })
            )
        }
    }

    private fun onResultFetch(fileDetails: CollectionAttachmentMO, metaDataJson: String): Boolean {
        val entity = billCollectionAdapter.items[0]
        val finalStoragePath = fileDetails.storagePath + "/" + fileDetails.fileName
        val jsonObjectMO = JSONObject(metaDataJson)
        jsonObjectMO.put("uuid", generatedUUID)
        jsonObjectMO.put("storagePath", finalStoragePath)
        jsonObjectMO.put("billOutputCenterId", entity.billOutputCenterId)
        jsonObjectMO.put("billId", entity.id)
        jsonObjectMO.put("billMonth", entity.billingMonth)
        jsonObjectMO.put("billYear", entity.billingYear)
        jsonObjectMO.put("sequenceNo", "1")
        jsonObjectMO.put("fileName", fileDetails.fileName)

        val attachmentEntity = AttachmentEntity()
        attachmentEntity.localFileName = fileDetails.fileName
        attachmentEntity.storagePath = finalStoragePath
        attachmentEntity.attachmentMetaData = jsonObjectMO.toString()
        attachmentEntity.isAttachmentSync = false

        chequeAttachmentEntity.set(attachmentEntity)

        message.what = NavigationConstants.ON_TAKE_PICTURE
        liveData.postValue(message)
        return true
    }

    fun onTaskCompleteBtnClick(view: View?) {
        if (!validateIsAnyBillSelected())
            showToast("Please select at least one bill collection")
        else if (obsActualCollectionAmount.get().toString().isEmpty() ||
            obsActualCollectionAmount.get().toString().replace("0", "").isEmpty())
            showToast("Please enter valid collection amount")
        else if (obsCollectionMode.get() == CollectionMode.CHEQUE && obsTransactionId.get()
                .toString().isEmpty()) {
            showToast("Its mandatory to enter cheque number")
        } else if (obsCollectionMode.get() == CollectionMode.CHEQUE &&
            (chequeImageUri.get() == null || chequeImageUri.get().toString().isEmpty()))
            showToast("Its mandatory to take picture of cheque/payment")
        else if ((obsCollectionMode.get() == CollectionMode.RTGS || obsCollectionMode.get() == CollectionMode.NEFT ||
                    obsCollectionMode.get() == CollectionMode.UPI) && obsTransactionId.get()
                .toString().isEmpty()) {
            showToast("Its mandatory to enter transaction Id")
        } else {
            updateBillCollectionDetailsInDB()
        }
    }

    private fun validateIsAnyBillSelected(): Boolean {
        for (entity: BillCollectionsEntity in billCollectionAdapter.items) {
            if (entity.isBillSelected) return true
        }
        return false
    }

    private fun updateBillCollectionDetailsInDB() {

        val cacheBillCollectionList = arrayListOf<CacheBillCollectionEntity>()
        for (entity: BillCollectionsEntity in billCollectionAdapter.items) {
            val cacheCollection = CacheBillCollectionEntity(
                entity.id, obsCollectionMode.get()!!.modeValue,
                obsComments.get().toString(), obsActualCollectionAmount.get().toString().toInt(),
                obsTransactionId.get().toString(), entity.billOutputCenterId)
            cacheBillCollectionList.add(cacheCollection)
        }

        isLoading.set(View.VISIBLE)
        addDisposable(collectionDao.insertAllCacheBillCollection(cacheBillCollectionList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ row: List<Long?>? ->
                isLoading.set(View.GONE)
                oneTimeWorkerWithNetwork(UpdateBillCollectionWorker::class.java)
                insertChequePicToDB()
                message.what = NavigationConstants.ON_COMPLETE_BILL_COLLECTION
                liveData.postValue(message)
            }, { t: Throwable? ->
                isLoading.set(View.GONE)
                Timber.e(t)
            }))
    }

    private fun insertChequePicToDB() {
        if (obsCollectionMode.get() == CollectionMode.CHEQUE && (chequeImageUri.get() != null || chequeImageUri.get()
                .toString().isEmpty())) {
            addDisposable(attachmentDao.insert(chequeAttachmentEntity.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    oneTimeWorkerWithNetwork(AttachmentsUploadWorker::class.java)
                }, { throwable: Throwable? ->
                    throwable!!.printStackTrace()
                })
            )
        }
    }

    fun onSyncBillCollections(view: View) {
        isLoading.set(View.VISIBLE)
        //        val currentDate = TimeUtils.YYYY_MM_DD_FORMAT(LocalDate.now())
        addDisposable(Single.zip(collectionDao.fetchAllAutoBCRotas(), coreApi.monthlyBillCollectionRotas,
            BiFunction<List<BillCollectionsEntity>, BillsForCollectionResponse, Boolean> { localBSTasksList, rotaResponse ->
                return@BiFunction onResultFetch(localBSTasksList, rotaResponse)
            })
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.set(View.GONE)
                //Refreshing UI
                if (it) {
                    Prefs.putString(PrefConstants.LAST_UPDATED_DATE, LocalDateTime.now().toString())
                    fetchCollectionAtSiteFromDB()
                }
            }, {
                isLoading.set(View.GONE)
                Timber.e(it)
            }))
    }

    private fun onResultFetch(bcLocalRotasList: List<BillCollectionsEntity>, bcResponse: BillsForCollectionResponse): Boolean {
        var isAnyBCInserted = false
        if (bcResponse.statusCode == 200 && !bcResponse.billsList.isNullOrEmpty()) {
            for (task in bcResponse.billsList) {
                val index = bcLocalRotasList.indexOfFirst { localTaskEntity -> localTaskEntity.id == task.id }
                if (index == -1) {
                    addDisposable(collectionDao.insert(task)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Timber.e("BillCollection : Task Inserted Successfully")
                        }, { }))
                    isAnyBCInserted = true
                } else
                    Timber.e("BillCollection : Task Already exists")
            }
        }
        return isAnyBCInserted
    }
}