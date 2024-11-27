package com.sisindia.ai.android.features.barracks.listing

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
import com.sisindia.ai.android.models.GeoPointItem
import com.sisindia.ai.android.room.dao.BarrackDao
import com.sisindia.ai.android.room.entities.BarrackEntity
import com.sisindia.ai.android.room.entities.BarrackTaggingEntity
import com.sisindia.ai.android.uimodels.barracks.BarrackUpdateBodyMO
import com.sisindia.ai.android.utils.IopsUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/22/2020.
 */
class BarrackTaggingViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    @Inject
    lateinit var barrackDao: BarrackDao

    var barrackNameField = ObservableField("")
    var barrackCodeUnit = ObservableField("")
    var geoLocation = ObservableField("")
    private var geoLatitude = ObservableField(0.00)
    var geoLongitude = ObservableField(0.00)
    var mMapLatLng = ObservableField<LatLng>()
    var barrackPhotoUri = ObservableField<Uri>()
    var barrackQRCode = ObservableField("")
    var barrackId = ObservableField(0)
    private lateinit var selectedBarrackEntity: BarrackEntity
    private lateinit var selectedBarrackTaggingEntity: BarrackTaggingEntity
    private var isBarrackGeoLocationDataAvailable = ObservableField(false)
    private var isSyncing: Boolean = false

    fun getLatLongFromService() {
        geoLatitude.set(Prefs.getDouble(PrefConstants.LATITUDE))
        geoLongitude.set(Prefs.getDouble(PrefConstants.LONGITUDE))
        geoLocation.set(geoLatitude.get().toString() + " , " + geoLongitude.get()!!)
        mMapLatLng.set(LatLng(geoLatitude.get()!!, geoLongitude.get()!!))
    }

    fun getBarrackDetailsFromTagging() {
        isLoading.set(View.VISIBLE)
        addDisposable(barrackDao.fetchBarrackTaggingDetails(barrackId.get()!!.toInt())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ barrackTagging ->
                isLoading.set(View.GONE)
                barrackTagging?.apply {
                    isBarrackGeoLocationDataAvailable.set(true)
                    selectedBarrackTaggingEntity = this
                    barrackNameField.set(barrackName)
                    barrackCodeUnit.set(barrackUnitCode)

                    geoLatitude.set(barrackLat)
                    geoLongitude.set(barrackLat)
                    geoLocation.set(Prefs.getDouble(PrefConstants.LATITUDE)
                        .toString() + " , " + Prefs.getDouble(PrefConstants.LONGITUDE).toString())
                    mMapLatLng.set(LatLng(geoLatitude.get()!!, geoLongitude.get()!!))
                }
            }, {
                isLoading.set(View.GONE)
                getBarrackDetailsIfTaggingIsEmpty()
            }))
    }

    private fun getBarrackDetailsIfTaggingIsEmpty() {
        isLoading.set(View.VISIBLE)
        addDisposable(barrackDao.fetchBarrackDetails(barrackId.get()!!.toInt())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ barrackTagging ->
                isLoading.set(View.GONE)
                barrackTagging?.apply {
                    selectedBarrackEntity = this
                    barrackNameField.set(name)
                    barrackCodeUnit.set(barrackCode)
                }
            }, { throwable: Throwable? ->
                isLoading.set(View.GONE)
                Timber.e(throwable)
                showToast("Error while fetching details")
            }))
    }

    fun onScanQRScreen(view: View) {
        message.what = NavigationConstants.OPEN_LIVE_QR_SCANNER_SCREEN
        liveData.postValue(message)
    }

    fun onViewsClicked(view: View) {
        when (view.id) {
            R.id.takeBarrackPhotoLayout -> {
                message.what = NavigationConstants.ON_TAKE_PICTURE
                liveData.postValue(message)
            }
            R.id.locationRefreshButton -> refreshGeoLocation()
        }
    }

    fun onDoneButtonClick(view: View) {
        if (isBarrackGeoLocationDataAvailable.get()!!)
            updateBarrackTaggingTable()
        else
            insertBarrackTaggingTable()
    }

    private fun insertBarrackTaggingTable() {
        if (::selectedBarrackEntity.isInitialized) {
            val barrackTaggingMO = BarrackTaggingEntity()
            barrackTaggingMO.localId = UUID.randomUUID().toString()
            barrackTaggingMO.barrackId = selectedBarrackEntity.id
            barrackTaggingMO.barrackName = selectedBarrackEntity.name
            barrackTaggingMO.barrackLat = geoLatitude.get()
            barrackTaggingMO.barrackLong = geoLongitude.get()
            barrackTaggingMO.barrackUnitCode = selectedBarrackEntity.barrackCode
            barrackTaggingMO.srNumber = barrackQRCode.get().toString()
            barrackTaggingMO.createdDateTime = LocalDateTime.now().toString()
            barrackTaggingMO.updatedDateTime = LocalDateTime.now().toString()

            addDisposable(barrackDao.insertBarrackTagging(barrackTaggingMO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    val barrackBody = BarrackUpdateBodyMO()
                    barrackBody.id = barrackTaggingMO.barrackId
                    barrackBody.barrackCode = barrackTaggingMO.barrackUnitCode
                    barrackBody.srNumber = barrackTaggingMO.srNumber
                    barrackBody.name = barrackTaggingMO.barrackName
                    val geoLatLong = GeoPointItem()
                    geoLatLong.latitude = barrackTaggingMO.barrackLat
                    geoLatLong.longitude = barrackTaggingMO.barrackLong
                    barrackBody.geoPoint = geoLatLong

                    updateBarrackViaAPI(barrackBody)
                }, { throwable: Throwable? ->
                    this.onError(throwable!!)
                }))
        }
    }

    private fun updateBarrackTaggingTable() {
        if (::selectedBarrackTaggingEntity.isInitialized) {
            selectedBarrackTaggingEntity.barrackLat = geoLatitude.get()
            selectedBarrackTaggingEntity.barrackLong = geoLongitude.get()
            selectedBarrackTaggingEntity.srNumber = barrackQRCode.get().toString()
            selectedBarrackTaggingEntity.updatedDateTime = LocalDateTime.now().toString()
            addDisposable(barrackDao.updateBarrackTagging(selectedBarrackTaggingEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    val barrackBody = BarrackUpdateBodyMO()
                    barrackBody.id = selectedBarrackTaggingEntity.barrackId
                    barrackBody.barrackCode = selectedBarrackTaggingEntity.barrackUnitCode
                    barrackBody.srNumber = selectedBarrackTaggingEntity.srNumber
                    barrackBody.name = selectedBarrackTaggingEntity.barrackName
                    val geoLatLong = GeoPointItem()
                    geoLatLong.latitude = selectedBarrackTaggingEntity.barrackLat
                    geoLatLong.longitude = selectedBarrackTaggingEntity.barrackLong
                    barrackBody.geoPoint = geoLatLong

                    updateBarrackViaAPI(barrackBody)
//                    message.what = NavigationConstants.ON_BARRACK_TAGGING_DONE
//                    liveData.postValue(message)
                }, { throwable: Throwable? ->
                    this.onError(throwable!!)
                }))
        }
    }

    private fun onError(throwable: Throwable) {
        Timber.e(throwable)
        showToast("Error occurred...")
    }

    private fun refreshGeoLocation() {
        if (Prefs.getBoolean(PrefConstants.IS_ON_DUTY)) {
            geoLatitude.set(Prefs.getDouble(PrefConstants.LATITUDE))
            geoLongitude.set(Prefs.getDouble(PrefConstants.LONGITUDE))
            geoLocation.set(Prefs.getDouble(PrefConstants.LATITUDE)
                .toString() + " , " + Prefs.getDouble(PrefConstants.LONGITUDE).toString())
            mMapLatLng.set(LatLng(geoLatitude.get()!!, geoLongitude.get()!!))
            message.what = NavigationConstants.ON_REFRESHING_LOCATION
            liveData.postValue(message)
        } else
            showToast(R.string.please_turn_on_duty)
    }

    private fun updateBarrackViaAPI(barrackBody: BarrackUpdateBodyMO) {

        if (isSyncing) {
            showToast("Updating barrack in progress, please wait...")
            return
        }

        isSyncing = true
        isLoading.set(View.VISIBLE)

        addDisposable(coreApi.updateBarrackDetails(barrackBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isSyncing = false
                isLoading.set(View.GONE)
                if (it.statusCode == 200) {
                    message.what = NavigationConstants.ON_BARRACK_TAGGING_DONE
                    liveData.postValue(message)
                    showToast("Barrack updated successfully...")
                } else
                    showToast(it.statusMessage)
            }, {
                isSyncing = false
                isLoading.set(View.GONE)
                if (IopsUtil.isInternetAvailable(app))
                    showToast(app.resources.getString(R.string.string_server_error))
                else
                    showToast(app.resources.getString(R.string.string_no_internet))
            }))
    }
}