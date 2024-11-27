package com.sisindia.ai.android.features.barracks.inspection.frags

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.databinding.ObservableField
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.room.dao.AttachmentDao
import com.sisindia.ai.android.room.dao.BarrackDao
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.room.entities.CacheBarrackInspectionEntity
import com.sisindia.ai.android.uimodels.barracks.BIOthersMO
import com.sisindia.ai.android.utils.GsonUtils
import com.sisindia.ai.android.workers.AttachmentsUploadWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/21/2020.
 */
class BarrackOthersViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var barrackDao: BarrackDao

    @Inject
    lateinit var attachmentDao: AttachmentDao

    var barrackBedPhotoUri = ObservableField<Uri>()
    var barrackKitUri = ObservableField<Uri>()
    var barrackMessUri = ObservableField<Uri>()
    var barrackOutsideUri = ObservableField<Uri>()

    var barrackBedPhotoMetadata = ObservableField("")
    var barrackKitPhotoMetadata = ObservableField("")
    var barrackMessPhotoMetadata = ObservableField("")
    var barrackOutsidePhotoMetadata = ObservableField("")

    var isBedPhotoNewOrUpdated = ObservableField(false)
    var isKitPhotoNewOrUpdated = ObservableField(false)
    var isMessPhotoNewOrUpdated = ObservableField(false)
    var isOutsidePhotoNewOrUpdated = ObservableField(false)

    val additionalRemarks = ObservableField("")
    lateinit var cacheBarrackInspectionEntity: CacheBarrackInspectionEntity
    var isComplaintAddedSuccessfully = ObservableField(false)

    fun fetchCacheBI() {
        addDisposable(barrackDao.fetchCacheData(Prefs.getInt(PrefConstants.CURRENT_TASK))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ cacheBI: CacheBarrackInspectionEntity ->
                cacheBI.apply {
                    cacheBarrackInspectionEntity = this
                    if (!cacheBI.othersJson.isNullOrEmpty())
                        updateBIOthersUIWithCacheData()
                }
            }, { t: Throwable? ->
                Timber.e("Error while fetching cache data")
            }))
    }

    private fun updateBIOthersUIWithCacheData() {
        if (::cacheBarrackInspectionEntity.isInitialized) {
            cacheBarrackInspectionEntity.othersJson.apply {
                val model = GsonUtils.toJsonWithoutExopse().fromJson(this, BIOthersMO::class.java)
                barrackBedPhotoUri.set(Uri.parse(model.beddingUri))
                barrackKitUri.set(Uri.parse(model.kitUri))
                barrackMessUri.set(Uri.parse(model.messUri))
                barrackOutsideUri.set(Uri.parse(model.barrackOutsideUri))
                additionalRemarks.set(model.remarks)
            }
        }
    }

    fun fetchAddedComplaintsFromDB() {
        showToast("Complaint added successfully...")
        isComplaintAddedSuccessfully.set(true)
        /*addDisposable(complaintDao.fetchAllPendingComplaintsBySite(Prefs.getInt(PrefConstants.CURRENT_SITE))
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                openComplaintsAdapter.clearAndSetItems(it)
                openCount.set(it.size)
            }, Timber::e))*/
    }

    fun onPhotosClicked(view: View) {
        when (view.id) {
            R.id.takeBeddingPhotoLayout ->
                message.what = NavigationConstants.ON_TAKING_BARRACK_BEDDING_PIC
            R.id.takeKitPhotoLayout ->
                message.what = NavigationConstants.ON_TAKING_BARRACK_KIT_PIC
            R.id.takeMessPhotoLayout ->
                message.what = NavigationConstants.ON_TAKING_BARRACK_MESS_PIC
            R.id.takeOutsidePhotoLayout ->
                message.what = NavigationConstants.ON_TAKING_BARRACK_OUTSIDE_PIC
        }
        liveData.postValue(message)
    }

    fun onOthersButtonClick(view: View) {
        when (view.id) {
            R.id.addComplaintButton -> {
                if (isComplaintAddedSuccessfully.get()!!)
                    showToast("Complaint already added")
                else {
                    message.what = NavigationConstants.ON_BARRACKS_COMPLAINT
                    liveData.postValue(message)
                }
            }
            R.id.addImprovementPlanButton -> {
                message.what = NavigationConstants.ON_ADD_GRIEVANCE_CLICK
                liveData.postValue(message)
            }
            R.id.othersDoneButton -> validateOthersTask()
        }
    }

    private fun validateOthersTask() {
        if (additionalRemarks.get().toString().isEmpty())
            showToast("Please enter remarks...")
        else
            updateBISpaceJson()
    }

    private fun updateBISpaceJson() {
        val biOthersMO = BIOthersMO()
        biOthersMO.beddingUri = barrackBedPhotoUri.get().toString()
        biOthersMO.kitUri = barrackKitUri.get().toString()
        biOthersMO.messUri = barrackMessUri.get().toString()
        biOthersMO.barrackOutsideUri = barrackOutsideUri.get().toString()
        biOthersMO.remarks = additionalRemarks.get().toString()

        var othersAttachmentJsonObj: JSONObject
        try {
            if (isBedPhotoNewOrUpdated.get()!! && biOthersMO.beddingUri.toString().isNotEmpty()) {
                othersAttachmentJsonObj = JSONObject(barrackBedPhotoMetadata.get().toString())
                biOthersMO.beddingGuid = othersAttachmentJsonObj.getString("uuid")
            }
            if (isKitPhotoNewOrUpdated.get()!! && biOthersMO.kitUri.toString().isNotEmpty()) {
                othersAttachmentJsonObj = JSONObject(barrackKitPhotoMetadata.get().toString())
                biOthersMO.kitGuid = othersAttachmentJsonObj.getString("uuid")
            }
            if (isMessPhotoNewOrUpdated.get()!! && biOthersMO.messUri.toString().isNotEmpty()) {
                othersAttachmentJsonObj = JSONObject(barrackMessPhotoMetadata.get().toString())
                biOthersMO.messGuid = othersAttachmentJsonObj.getString("uuid")
            }
            if (isOutsidePhotoNewOrUpdated.get()!! && biOthersMO.barrackOutsideUri.toString()
                    .isNotEmpty()) {
                othersAttachmentJsonObj = JSONObject(barrackOutsidePhotoMetadata.get().toString())
                biOthersMO.outsideGuid = othersAttachmentJsonObj.getString("uuid")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (::cacheBarrackInspectionEntity.isInitialized) {
            cacheBarrackInspectionEntity.othersJson =
                GsonUtils.toJsonWithoutExopse().toJson(biOthersMO)
            cacheBarrackInspectionEntity.updatedDateTime = LocalDateTime.now().toString()

            addDisposable(barrackDao.updateBICache(cacheBarrackInspectionEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    message.what = NavigationConstants.ON_COMPLETE_BARRACK_INSPECTION_OTHERS
                    liveData.postValue(message)
                }, { throwable: Throwable? ->
                    this.onError(throwable!!)
                }))

            insertBarrackOthersAttachment()
        }
    }

    private fun insertBarrackOthersAttachment() {
        val attachmentList = ArrayList<AttachmentEntity>()

        if (isBedPhotoNewOrUpdated.get()!!) {
            attachmentList.add(AttachmentEntity(barrackBedPhotoUri.get().toString(),
                barrackBedPhotoMetadata.get().toString()))
        }
        if (isKitPhotoNewOrUpdated.get()!!) {
            attachmentList.add(AttachmentEntity(barrackKitUri.get().toString(),
                barrackKitPhotoMetadata.get().toString()))
        }
        if (isMessPhotoNewOrUpdated.get()!!) {
            attachmentList.add(AttachmentEntity(barrackMessUri.get().toString(),
                barrackMessPhotoMetadata.get().toString()))
        }
        if (isOutsidePhotoNewOrUpdated.get()!!) {
            attachmentList.add(AttachmentEntity(barrackOutsideUri.get().toString(),
                barrackOutsidePhotoMetadata.get().toString()))
        }

        if (attachmentList.isNotEmpty()) {
            addDisposable(attachmentDao.insertAll(attachmentList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    oneTimeWorkerWithNetwork(AttachmentsUploadWorker::class.java)
                }, { throwable: Throwable? ->
                    throwable!!.printStackTrace()
                }))
        }
    }

    private fun onError(throwable: Throwable) {
        Timber.e(throwable)
        showToast("Error occurred...")
    }
}