package com.sisindia.ai.android.features.barracks.listing

import android.app.Activity
import android.content.Intent
import android.os.Message
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.commons.CaptureImageType
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.IntentConstants.BARRACK_ID
import com.sisindia.ai.android.constants.IntentRequestCodes
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.ActivityBarrackTaggingBinding
import com.sisindia.ai.android.features.livecamera.ImageCaptureActivity
import com.sisindia.ai.android.mlcore.ScanQRActivity
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

/**
 * Created by Ashu Rajput on 4/22/2020.
 */
class BarrackTaggingActivity : IopsBaseActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityBarrackTaggingBinding
    private var viewModel: BarrackTaggingViewModel? = null
    private lateinit var googleMap: GoogleMap
    private lateinit var marker: Marker

    override fun getLayoutResource(): Int {
        return R.layout.activity_barrack_tagging
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(BarrackTaggingViewModel::class.java)
                as BarrackTaggingViewModel
    }

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbBarrackTagging)
        viewModel!!.apply {
            getLatLongFromService()
            getBarrackDetailsFromTagging()
        }
        CoroutineScope(Dispatchers.IO).launch {
            delay(TimeUnit.SECONDS.toMillis(1))
            withContext(Dispatchers.Main) {
                val mapFragment: SupportMapFragment? =
                    supportFragmentManager.findFragmentById(R.id.postGoogleMap) as? SupportMapFragment
                mapFragment?.getMapAsync(this@BarrackTaggingActivity)
            }
        }
    }

    override fun extractBundle() {
        viewModel!!.barrackId.set(intent.extras?.getInt(BARRACK_ID))
    }

    override fun initViewState() {
        liveData.observe(this,
            Observer { message: Message ->
                when (message.what) {
                    NavigationConstants.OPEN_DASH_BOARD -> finish()
                    NavigationConstants.OPEN_LIVE_QR_SCANNER_SCREEN -> {
                        startActivityForResult(ScanQRActivity.newIntent(this, true),
                            IntentRequestCodes.REQUEST_CODE_OPEN_POST_SCAN)
                    }
                    NavigationConstants.ON_TAKE_PICTURE -> {
                        startActivityForResult(ImageCaptureActivity.newIntentWithType(this,
                            CaptureImageType.BARRACK_PROFILE),
                            IntentRequestCodes.REQUEST_CODE_TAKE_PICTURE)
                    }
                    NavigationConstants.ON_BARRACK_TAGGING_DONE -> onSuccessfulBarrackTagging()

                    NavigationConstants.ON_REFRESHING_LOCATION -> updateMarkerAndCameraOnMap()
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IntentRequestCodes.REQUEST_CODE_OPEN_POST_SCAN -> if (resultCode == Activity.RESULT_OK && data != null) {
                if ((data.getStringExtra(IntentConstants.ON_QR_SCANNED) == "NA"))
                    showToast(resources.getString(R.string.not_valid_post_qr))
                else
                    viewModel!!.barrackQRCode.set(data.getStringExtra(IntentConstants.ON_QR_SCANNED))
            }
            IntentRequestCodes.REQUEST_CODE_TAKE_PICTURE -> if (resultCode == Activity.RESULT_OK && data != null) {
                showToast("Photo captured successfully...")
                viewModel!!.barrackPhotoUri.set(data.data)
            }
        }
    }

    /*override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap!!
        updateMarkerAndCameraOnMap()
    }*/

    override fun onMapReady(p0: GoogleMap) {
        this.googleMap = p0
        updateMarkerAndCameraOnMap()
    }

    private fun updateMarkerAndCameraOnMap() {
        if (::googleMap.isInitialized) {
            googleMap.apply {
                viewModel?.apply {
                    if (::marker.isInitialized)
                        marker.remove()

                    val currentHomeLocation = "Location ${geoLocation.get().toString()}"
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMapLatLng.get()!!, 16f))
                    val markerOption = MarkerOptions().position(mMapLatLng.get()!!)
                        .title(currentHomeLocation)
                    marker = googleMap.addMarker(markerOption)!!
                    marker.showInfoWindow()
                }
            }
        }
    }

    private fun onSuccessfulBarrackTagging() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
}