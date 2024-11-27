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
import com.sisindia.ai.android.features.billsubmit.RadioCheckedListener
import com.sisindia.ai.android.room.dao.AttachmentDao
import com.sisindia.ai.android.room.dao.BarrackDao
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.room.entities.CacheBarrackInspectionEntity
import com.sisindia.ai.android.uimodels.barracks.BILandlordsMO
import com.sisindia.ai.android.uimodels.barracks.MetLandlordMO
import com.sisindia.ai.android.utils.GsonUtils
import com.sisindia.ai.android.workers.AttachmentsUploadWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/21/2020.
 */
class BarrackMetLandlordViewModel @Inject constructor(val app: Application) :
    IopsBaseViewModel(app) {

    @Inject
    lateinit var barrackDao: BarrackDao

    @Inject
    lateinit var attachmentDao: AttachmentDao

    var photoWithLandlordUri = ObservableField<Uri>()
    var photoWithCustodianUri = ObservableField<Uri>()
    var landlordPhotoMetadata = ObservableField("")
    var custodianPhotoMetadata = ObservableField("")
    val isLandlordPhotoNewOrUpdated = ObservableField(false)
    val isCustodianPhotoNewOrUpdated = ObservableField(false)

    var name = ObservableField("")
    var mobileNumber = ObservableField("")
    var addAmenitiesRemarks = ObservableField("")
    var showMetLandlordUI = ObservableField(View.GONE)
    var isMetWithLandlord = ObservableField(false)
    var isPaymentComing = ObservableField(true)
    var showAmenitiesRemarks = ObservableField(View.GONE)
    var isAmenitiesProvided = ObservableField(true)
    var landlordTypeIs = ObservableField("")

    //----------RadioGroup Views-------------//
    var metLandlordRGChecked = ObservableField(R.id.rbNotMetLandlord)
    var paymentComingRGChecked = ObservableField(R.id.rbYesPaymentComing)
    var amenitiesRGChecked = ObservableField(R.id.rbYesAmenities)
    var landlordTypeRGChecked = ObservableField(R.id.rbRealLandlord)

    lateinit var cacheBarrackInspectionEntity: CacheBarrackInspectionEntity

    val metLandlordRCListener = object : RadioCheckedListener {
        override fun onRadioButtonChecked(value: String) {
            if (value == "Yes") {
                showMetLandlordUI.set(View.VISIBLE)
                isMetWithLandlord.set(true)
            } else {
                showMetLandlordUI.set(View.GONE)
                isMetWithLandlord.set(false)
            }
        }
    }

    val paymentComingRCListener = object : RadioCheckedListener {
        override fun onRadioButtonChecked(value: String) {
            if (value == "Yes")
                isPaymentComing.set(true)
            else
                isPaymentComing.set(false)
        }
    }

    val amenitiesRCListener = object : RadioCheckedListener {
        override fun onRadioButtonChecked(value: String) {
            if (value == "Yes") {
                showAmenitiesRemarks.set(View.GONE)
                isAmenitiesProvided.set(true)
            } else {
                showAmenitiesRemarks.set(View.VISIBLE)
                isAmenitiesProvided.set(false)
            }
        }
    }

    val landlordTypeRCListener = object : RadioCheckedListener {
        override fun onRadioButtonChecked(value: String) {
            landlordTypeIs.set(value)
        }
    }

    fun fetchCacheBI() {
        isLoading.set(View.VISIBLE)
        addDisposable(barrackDao.fetchCacheData(Prefs.getInt(PrefConstants.CURRENT_TASK))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ cacheBI: CacheBarrackInspectionEntity ->
                isLoading.set(View.GONE)
                cacheBI.apply {
                    cacheBarrackInspectionEntity = this
                    if (!cacheBI.metLandlordJson.isNullOrEmpty())
                        updateBILandlordsUIWithCacheData()
                }
            }, { t: Throwable? ->
                isLoading.set(View.GONE)
                Timber.e("Error while fetching cache MetLandlord data")
            }))
    }

    private fun updateBILandlordsUIWithCacheData() {
        if (::cacheBarrackInspectionEntity.isInitialized) {
            cacheBarrackInspectionEntity.metLandlordJson.apply {
                val model =
                    GsonUtils.toJsonWithoutExopse().fromJson(this, BILandlordsMO::class.java)
                model.apply {
                    if (isMetLandlord!!) {
                        metLandlordRGChecked.set(R.id.rbYesMetLandlord)
                        name.set(metLandlordMO!!.landlordName)
                        mobileNumber.set(metLandlordMO!!.landlordMobile)
                        photoWithLandlordUri.set(Uri.parse(metLandlordMO!!.landlordUri))
                        photoWithCustodianUri.set(Uri.parse(metLandlordMO!!.custodianUri))
                        if (!metLandlordMO!!.isPaymentComing!!)
                            paymentComingRGChecked.set(R.id.rbNoPaymentComing)
                    }

                    if (!isAmenitiesProvided!!) {
                        amenitiesRGChecked.set(R.id.rbNoAmenities)
                        addAmenitiesRemarks.set(amenitiesRemarks)
                    }

                    if (landlordType.equals("Custodian"))
                        landlordTypeRGChecked.set(R.id.rbCustodian)
                }
            }
            isLoading.set(View.GONE)
        }
    }

    fun onPhotosClicked(view: View) {
        if (view.id == R.id.takePhotoWithLandlord)
            message.what = NavigationConstants.ON_TAKING_LANDLORD_PIC
        else if (view.id == R.id.takePhotoWithCustodian)
            message.what = NavigationConstants.ON_TAKING_CUSTODIAN_PIC
        liveData.postValue(message)
    }

    fun onDoneButtonClick(view: View) {
        if (isMetWithLandlord.get()!! && name.get().toString().isEmpty())
            showToast("Please enter landlord/custodian name")
        else if (isMetWithLandlord.get()!! && mobileNumber.get().toString().isEmpty())
            showToast("Please enter mobile number")
        else if (!isAmenitiesProvided.get()!! && addAmenitiesRemarks.get().toString().isEmpty())
            showToast("Please enter amenities remark")
        else
            updateMetLandlordDetails()
    }

    private fun updateMetLandlordDetails() {
        val biLandlordMO = BILandlordsMO()
        biLandlordMO.isMetLandlord = isMetWithLandlord.get()
        biLandlordMO.isAmenitiesProvided = isAmenitiesProvided.get()
        biLandlordMO.amenitiesRemarks = addAmenitiesRemarks.get().toString()
        biLandlordMO.landlordType = landlordTypeIs.get().toString()

        if (isMetWithLandlord.get()!!) {
            val metLandlordDetail = MetLandlordMO()
            metLandlordDetail.landlordName = name.get().toString()
            metLandlordDetail.landlordMobile = mobileNumber.get().toString()
            metLandlordDetail.isPaymentComing = isPaymentComing.get()
            metLandlordDetail.landlordUri = photoWithLandlordUri.get().toString()
            metLandlordDetail.custodianUri = photoWithCustodianUri.get().toString()
            biLandlordMO.metLandlordMO = metLandlordDetail
        }

        if (::cacheBarrackInspectionEntity.isInitialized) {
            cacheBarrackInspectionEntity.metLandlordJson = GsonUtils.toJsonWithoutExopse()
                .toJson(biLandlordMO)
            cacheBarrackInspectionEntity.updatedDateTime = LocalDateTime.now().toString()

            addDisposable(barrackDao.updateBICache(cacheBarrackInspectionEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    insertBarrackMetLandlordAttachments()

                    message.what = NavigationConstants.ON_COMPLETE_BARRACK_INSPECTION_LANDLORD
                    liveData.postValue(message)
                }, { throwable: Throwable? ->
                    this.onError(throwable!!)
                }))
        }
    }

    private fun insertBarrackMetLandlordAttachments() {
        val attachmentList = ArrayList<AttachmentEntity>()

        if (isLandlordPhotoNewOrUpdated.get()!!) {
            attachmentList.add(AttachmentEntity(photoWithLandlordUri.get().toString(),
                landlordPhotoMetadata.get().toString()))
        }
        if (isCustodianPhotoNewOrUpdated.get()!!) {
            attachmentList.add(AttachmentEntity(photoWithCustodianUri.get().toString(),
                custodianPhotoMetadata.get().toString()))
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