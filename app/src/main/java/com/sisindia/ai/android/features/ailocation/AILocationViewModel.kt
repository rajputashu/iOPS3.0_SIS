package com.sisindia.ai.android.features.ailocation

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.databinding.ObservableField
import com.droidcommons.preference.Prefs
import com.google.android.gms.maps.model.LatLng
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.room.dao.AIProfileDao
import com.sisindia.ai.android.room.dao.AttachmentDao
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.room.entities.CacheAIProfileEntity
import com.sisindia.ai.android.workers.AIUpdateProfileWorker
import com.sisindia.ai.android.workers.AttachmentsUploadWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 3/29/2020.
 */
class AILocationViewModel @Inject constructor(val app: Application) :
    IopsBaseViewModel(app) {

    @Inject
    lateinit var aiProfileDao: AIProfileDao

    @Inject
    lateinit var attachmentDao: AttachmentDao

    var geoLatitude = ObservableField(0.0)
    var geoLongitude = ObservableField(0.0)
    var geoLocation = ObservableField("")
    var mMapLatLng = ObservableField<LatLng>()
    var photoImageUri = ObservableField<Uri>()
    var isNewProfileImageCaptured = ObservableField(false)
    var alternateNo = ObservableField("")
    var alternateAddress = ObservableField("")
    var profileMetaData = ObservableField("")
    private lateinit var aiProfileEntity: CacheAIProfileEntity

    fun fetchAIProfileDetails() {
        if(!Prefs.getBoolean(PrefConstants.IS_ON_DUTY))
            showToast("Please turn ON your duty to get your current location")

        setIsLoading(true)
        addDisposable(aiProfileDao.fetchAIDetails()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ aiProfileEntity: CacheAIProfileEntity ->
                setIsLoading(false)
                onSuccessResult(aiProfileEntity)
            }, { throwable: Throwable? ->
                setIsLoading(false)
                Timber.e("$throwable")
                //                refreshGeoLocation()
            }))
    }

    private fun onSuccessResult(aiProfileEntity: CacheAIProfileEntity) {
        this.aiProfileEntity = aiProfileEntity
        geoLocation.set(aiProfileEntity.latitude.toString() + " , " + aiProfileEntity.longitude.toString())
        if (aiProfileEntity.aiPhotoURI != null)
            photoImageUri.set(Uri.parse(aiProfileEntity.aiPhotoURI))
        geoLatitude.set(aiProfileEntity.latitude)
        geoLongitude.set(aiProfileEntity.longitude)
        alternateNo.set(aiProfileEntity.altContactNo)
        alternateAddress.set(aiProfileEntity.altAddress)
        mMapLatLng.set(LatLng(geoLatitude.get()!!, geoLongitude.get()!!))
    }

    fun ivRotaDrawerClick(view: View?) {
        message.what = NavigationConstants.OPEN_DASH_BOARD_DRAWER
        liveData.postValue(message)
    }

    fun onEditButtonClick(view: View) {
        message.what = NavigationConstants.ON_EDIT_AI_PROFILE_PIC
        liveData.postValue(message)
    }

    fun onLocationRefreshClicked(view: View) {
        refreshGeoLocation()
    }

    private fun refreshGeoLocation() {
        if (Prefs.getBoolean(PrefConstants.IS_ON_DUTY)) {
            geoLatitude.set(Prefs.getDouble(PrefConstants.LATITUDE))
            geoLongitude.set(Prefs.getDouble(PrefConstants.LONGITUDE))
            geoLocation.set(Prefs.getDouble(PrefConstants.LATITUDE).toString() + " , " + Prefs.getDouble(PrefConstants.LONGITUDE).toString())
            mMapLatLng.set(LatLng(geoLatitude.get()!!, geoLongitude.get()!!))
            message.what = NavigationConstants.ON_REFRESHING_LOCATION
            liveData.postValue(message)
        } else {
            showToast(R.string.please_turn_on_duty)
        }
    }

    fun getLatLongFromService() {
        geoLatitude.set(Prefs.getDouble(PrefConstants.LATITUDE))
        geoLongitude.set(Prefs.getDouble(PrefConstants.LONGITUDE))
        mMapLatLng.set(LatLng(geoLatitude.get()!!, geoLongitude.get()!!))
    }

    fun onSaveBtnClick(view: View) {
        if (alternateNo.get().toString().isNotEmpty() && alternateNo.get().toString().length < 10)
            showToast("Please enter valid mobile number")
        else {
            if (::aiProfileEntity.isInitialized) {
                aiProfileEntity.aiPhotoURI = photoImageUri.get().toString()
                aiProfileEntity.latitude = geoLatitude.get()!!
                aiProfileEntity.longitude = geoLongitude.get()!!
                aiProfileEntity.altContactNo = alternateNo.get()
                aiProfileEntity.altAddress = alternateAddress.get()
                aiProfileEntity.updatedDateTime = LocalDateTime.now().toString()
                aiProfileEntity.isSynced = false
                setIsLoading(true)
                addDisposable(aiProfileDao.updateAIDetails(aiProfileEntity)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        setIsLoading(false)
                        syncAIProfile()
                        message.what = NavigationConstants.ON_UPDATING_AI_PROFILE_SUCCESSFULLY
                        liveData.postValue(message)
                    }, { throwable: Throwable? ->
                        setIsLoading(false)
                        showToast("Error while updating AI profile details")
                        Timber.e("$throwable")
                    }))
            } else {
                aiProfileEntity = CacheAIProfileEntity(photoImageUri.get().toString(),
                    alternateAddress.get(), "", geoLatitude.get()!!,
                    geoLongitude.get()!!, alternateNo.get())
                setIsLoading(true)
                addDisposable(aiProfileDao.insertAIDetails(aiProfileEntity)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        setIsLoading(false)
                        syncAIProfile()
                        message.what = NavigationConstants.ON_UPDATING_AI_PROFILE_SUCCESSFULLY
                        liveData.postValue(message)
                    }, { throwable: Throwable? ->
                        setIsLoading(false)
                        showToast("Error while updating AI profile details")
                        Timber.e("$throwable")
                    }))
            }

            if (photoImageUri.get() != null && isNewProfileImageCaptured.get()!!)
                insertAIProfilePicAttachment()
        }
    }

    private fun insertAIProfilePicAttachment() {
        addDisposable(attachmentDao.insert(AttachmentEntity(photoImageUri.get().toString(),
            profileMetaData.get()))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                oneTimeWorkerWithNetwork(AttachmentsUploadWorker::class.java)
            }, { throwable: Throwable? ->
                throwable!!.printStackTrace()
            }))
    }

    private fun syncAIProfile() {
        oneTimeWorkerWithNetwork(AIUpdateProfileWorker::class.java)
    }

    /*fun syncClick(view: View) {
        message.what = NavigationConstants.ON_MANUAL_SYNC_CLICK
        liveData.postValue(message)
    }*/
}