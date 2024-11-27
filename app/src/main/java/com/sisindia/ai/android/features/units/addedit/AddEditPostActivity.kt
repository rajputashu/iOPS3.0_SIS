package com.sisindia.ai.android.features.units.addedit

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
import com.sisindia.ai.android.commons.YesNoDialogFragment
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.IntentRequestCodes
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.ActivityAddEditPostsBinding
import com.sisindia.ai.android.features.livecamera.ImageCaptureActivity
import com.sisindia.ai.android.features.units.sheet.AddEquipmentBottomSheet
import com.sisindia.ai.android.mlcore.ScanQRActivity
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

/**
 * Created by Ashu Rajput on 3/24/2020.
 */
class AddEditPostActivity : IopsBaseActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityAddEditPostsBinding
    private var viewModel: AddEditPostViewModel? = null
    private lateinit var googleMap: GoogleMap
    private lateinit var marker: Marker

    override fun getLayoutResource(): Int {
        return R.layout.activity_add_edit_posts
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(AddEditPostViewModel::class.java) as AddEditPostViewModel
    }

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbAddPost)
        viewModel!!.getLatLongFromService()
        CoroutineScope(Dispatchers.IO).launch {
            delay(TimeUnit.SECONDS.toMillis(1))
            withContext(Dispatchers.Main) {
                val mapFragment: SupportMapFragment? =
                    supportFragmentManager.findFragmentById(R.id.postGoogleMap) as? SupportMapFragment
                mapFragment?.getMapAsync(this@AddEditPostActivity)
            }
        }
    }

    override fun extractBundle() {
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(IntentConstants.POST_ID)) {
            val postId = bundle.getInt(IntentConstants.POST_ID)
            viewModel!!.apply {
                if (postId == 0) {
                    toolBarTitle.set(resources.getString(R.string.string_add_post))
                    isEditModeOn.set(false)
                } else {
                    toolBarTitle.set(resources.getString(R.string.string_edit_post))
                    fetchPostDetailsForEdit(postId)
                    isEditModeOn.set(true)
                }
            }
        }
    }

    override fun initViewState() {
        liveData.observe(this,
            Observer { message: Message ->
                when (message.what) {
                    NavigationConstants.OPEN_LIVE_QR_SCANNER_SCREEN -> {
                        startActivityForResult(ScanQRActivity.newIntent(this, true),
                            IntentRequestCodes.REQUEST_CODE_OPEN_POST_SCAN)
                    }
                    NavigationConstants.OPEN_ADD_EQUIPMENT -> {
                        openAddEquipmentBottomSheet()
                    }
                    NavigationConstants.ON_SUCCESS_ADD_EDIT_POST -> {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    NavigationConstants.UPDATE_SAVED_EQUIPMENT -> {
                        viewModel!!.updateAddedEquipment(message.obj as EquipmentsMO)
                    }
                    NavigationConstants.ON_TAKE_PICTURE -> {
                        if (viewModel!!.isEditingPostImage.get()!!)
                            startActivityForResult(ImageCaptureActivity.newIntentWithType(this,
                                CaptureImageType.EDIT_SITE_POST),
                                IntentRequestCodes.REQUEST_CODE_TAKE_PICTURE)
                        else
                            startActivityForResult(ImageCaptureActivity.newIntentWithType(this,
                                CaptureImageType.POST_PHOTO),
                                IntentRequestCodes.REQUEST_CODE_TAKE_PICTURE)
                    }
                    NavigationConstants.ON_POST_SPI_TAKE_PICTURE -> {
                        if (viewModel!!.isEditingSpiImage.get()!!) {
                            startActivityForResult(ImageCaptureActivity.newIntentWithType(this,
                                CaptureImageType.EDIT_SITE_POST_SPI),
                                IntentRequestCodes.REQUEST_CODE_SPI_PIC)
                        } else {
                            startActivityForResult(ImageCaptureActivity.newIntentWithType(this,
                                CaptureImageType.POST_SPI_PHOTO),
                                IntentRequestCodes.REQUEST_CODE_SPI_PIC)
                        }
                    }
                    NavigationConstants.ON_REFRESHING_LOCATION -> updateMarkerAndCameraOnMap()
                    NavigationConstants.ON_REPLACING_MAIN_GATE -> openConfirmationDialog()
                    //                    NavigationConstants.ON_SHOWING_ERROR_MESSAGE -> openQRDuplicateDialog()
                    NavigationConstants.ON_SHOWING_ERROR_MESSAGE -> openDialogWithServerMsg(message.obj as String)

                    /*NavigationConstants.OPEN_MAP_REGISTERS_SCREEN -> {
                        startActivityForResult(Intent(this, MapRegistersActivity::class.java),
                            IntentRequestCodes.REQUEST_CODE_MAP_REGISTER)
                    }*/
                }
            })
    }

    private fun openAddEquipmentBottomSheet() {
        if (supportFragmentManager.findFragmentByTag(AddEquipmentBottomSheet::class.java.simpleName) == null) {
            AddEquipmentBottomSheet.newInstance().show(supportFragmentManager,
                AddEquipmentBottomSheet::class.java.simpleName)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IntentRequestCodes.REQUEST_CODE_OPEN_POST_SCAN -> if (resultCode == Activity.RESULT_OK && data != null) {
                //Timber.e("QRData ${data.getStringExtra(IntentConstants.ON_QR_SCANNED)}")
                if ((data.getStringExtra(IntentConstants.ON_QR_SCANNED) == "NA"))
                    showToast(resources.getString(R.string.not_valid_post_qr))
                else {
                    viewModel!!.postQRCode.set(data.getStringExtra(IntentConstants.ON_QR_SCANNED))
                    /*data.getStringExtra(IntentConstants.ON_QR_SCANNED)?.apply {
                        viewModel!!.checkDuplicateQRCode(this)
                    }*/
                }
            }
            IntentRequestCodes.REQUEST_CODE_TAKE_PICTURE -> if (resultCode == Activity.RESULT_OK && data != null) {
                showToast("Photo captured successfully...")
                data.extras!!.apply {
                    viewModel!!.apply {
                        postImageUri.set(Uri.parse(getString(IntentConstants.CAPTURED_FILE_URI)))
                        postImageMetaData.set(getString(IntentConstants.FILE_METADATA))
                    }
                }
                //                viewModel!!.postImageUri.set(data.data)
            }
            IntentRequestCodes.REQUEST_CODE_SPI_PIC -> if (resultCode == Activity.RESULT_OK && data != null) {
                showToast("Photo captured successfully...")
                data.extras!!.apply {
                    viewModel!!.apply {
                        spiImageUri.set(Uri.parse(getString(IntentConstants.CAPTURED_FILE_URI)))
                        spiImageMetaData.set(getString(IntentConstants.FILE_METADATA))
                        isEditingSpiImage.set(true)
                    }
                }
            }
            /*IntentRequestCodes.REQUEST_CODE_MAP_REGISTER -> if (resultCode == Activity.RESULT_OK && data != null) {

            }*/
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
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

    private fun openConfirmationDialog() {
        viewModel!!.isQRDialogFlag.set(false)
        val dialog = YesNoDialogFragment.newInstance(resources
            .getString(R.string.string_replace_main_gate, viewModel!!.postName.get().toString()))
        dialog.show(supportFragmentManager, YesNoDialogFragment::class.java.simpleName)
        dialog.initDialogListener(viewModel!!.dialogListener)
        dialog.isCancelable = false
    }

    private fun openDialogWithServerMsg(message: String) {
        viewModel!!.isQRDialogFlag.set(true)
        //        val dialog = YesNoDialogFragment.newSingleButtonInstance("Warning!!\n\nScanned QR is already tagged to different post. Kindly use new QR")
        val dialog = YesNoDialogFragment.newSingleButtonInstance(message)
        dialog.show(supportFragmentManager, YesNoDialogFragment::class.java.simpleName)
        dialog.initDialogListener(viewModel!!.dialogListener)
        dialog.isCancelable = false
    }

    companion object {

        @JvmStatic
        fun newIntent(activity: Activity, postId: Int): Intent {
            val intent = Intent(activity, AddEditPostActivity::class.java)
            intent.putExtra(IntentConstants.POST_ID, postId)
            return intent
        }
    }

}