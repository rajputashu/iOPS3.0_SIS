package com.sisindia.ai.android.features.ailocation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.commons.CaptureImageType
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.IntentRequestCodes
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentAiLocationBinding
import com.sisindia.ai.android.features.livecamera.ImageCaptureActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

/**
 * Created by Ashu Rajput on 3/29/2020.
 */
class AILocationFragment : IopsBaseFragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentAiLocationBinding
    private var viewModel: AILocationViewModel? = null
    private lateinit var googleMap: GoogleMap

    companion object {
        fun newInstance(): AILocationFragment = AILocationFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_ai_location
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater!!, layoutResource, container, false)
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(AILocationViewModel::class.java) as AILocationViewModel
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {
                NavigationConstants.ON_EDIT_AI_PROFILE_PIC -> {
                    openPhotoCapture()
                }
                NavigationConstants.ON_UPDATING_AI_PROFILE_SUCCESSFULLY -> {
                    Toast.makeText(
                        requireActivity(), "Successfully updated you profile",
                        Toast.LENGTH_LONG
                    ).show()
                    requireActivity().onBackPressed()
                }
                NavigationConstants.ON_REFRESHING_LOCATION -> updateMarkerAndCameraOnMap()
            }
        }
    }

    override fun onCreated() {
        viewModel!!.apply {
            getLatLongFromService()
            fetchAIProfileDetails()
        }
        CoroutineScope(Dispatchers.IO).launch {
            delay(TimeUnit.SECONDS.toMillis(2))
            withContext(Dispatchers.Main) {
                if (isAdded) {
                    val mMapFragment =
                        childFragmentManager.findFragmentById(R.id.aiProfileMap) as SupportMapFragment
                    mMapFragment.getMapAsync(this@AILocationFragment)
                }
            }
        }
    }

    private fun openPhotoCapture() {
        startActivityForResult(
            ImageCaptureActivity.newIntentWithFrontCamera(
                this,
                CaptureImageType.AI_PROFILE
            ), IntentRequestCodes.REQUEST_CODE_EDIT_PROFILE_PIC
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null && resultCode == Activity.RESULT_OK) {
            data.extras!!.apply {
                viewModel!!.apply {
                    isNewProfileImageCaptured.set(true)
                    photoImageUri.set(Uri.parse(getString(IntentConstants.CAPTURED_FILE_URI)))
                    profileMetaData.set(getString(IntentConstants.FILE_METADATA))
                }
            }
        }
    }

    /* override fun onMapReady(googleMap: GoogleMap?) {
         this.googleMap = googleMap!!
         updateMarkerAndCameraOnMap()
     }*/

    private fun updateMarkerAndCameraOnMap() {
        if (::googleMap.isInitialized) {
            googleMap.apply {
                viewModel?.apply {
                    val currentHomeLocation = "Your Location ${geoLocation.get().toString()}"
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMapLatLng.get()!!, 16f))
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(mMapLatLng.get()!!)
                            .title(currentHomeLocation)
                    )?.showInfoWindow()
                }
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        this.googleMap = p0
        updateMarkerAndCameraOnMap()
    }
}