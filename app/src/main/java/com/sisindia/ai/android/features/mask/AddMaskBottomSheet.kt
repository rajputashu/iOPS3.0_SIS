package com.sisindia.ai.android.features.mask

import android.app.Activity
import android.content.Intent
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_OPEN_GUARD_SCAN
import com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_TAKE_PICTURE
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.BottomSheetAddMaskBinding
import com.sisindia.ai.android.features.imagecapture.CaptureImageActivityV2
import com.sisindia.ai.android.mlcore.ScanQRActivity
import com.sisindia.ai.android.room.entities.AttachmentEntity
//import kotlinx.android.synthetic.main.bottom_sheet_add_client.*
import org.parceler.Parcels

class AddMaskBottomSheet : IopsBaseBottomSheetDialogFragment() {

    lateinit var binding: BottomSheetAddMaskBinding
    lateinit var viewModel: MaskDistributionViewModel

    companion object {
        fun newInstance() = AddMaskBottomSheet()
    }

    override fun getLayoutResource(): Int {
        return R.layout.bottom_sheet_add_mask
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = bindFragmentView(layoutResource, inflater, container)
                as BottomSheetAddMaskBinding
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this,
            Observer { message: Message ->
                when (message.what) {
                    NavigationConstants.ON_MASK_ADDED_SUCCESSFULLY -> {
                        message.what = NavigationConstants.ON_REFRESHING_MASK_ADDED
                        liveData.postValue(message)
                        dismissAllowingStateLoss()
                    }
                    NavigationConstants.OPEN_LIVE_QR_SCANNER_SCREEN -> {
                        startActivityForResult(ScanQRActivity.newIntent(requireActivity()),
                            REQUEST_CODE_OPEN_GUARD_SCAN)
                    }
                    NavigationConstants.ON_TAKE_PICTURE ->
                        openImageCaptureForKitRequest(message.obj as AttachmentEntity)
                }
            })
    }

    override fun onCreated() {
        viewModel.fetchAllSites()
        binding.sheetCloseButton.setOnClickListener { dismissAllowingStateLoss() }
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(MaskDistributionViewModel::class.java)
                as MaskDistributionViewModel
    }

    override fun applyStyle() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    private fun openImageCaptureForKitRequest(item: AttachmentEntity) {
        startActivityForResult(CaptureImageActivityV2.newIntent(requireActivity(), item),
            REQUEST_CODE_TAKE_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null && data.extras != null) {
            if (requestCode == REQUEST_CODE_OPEN_GUARD_SCAN) {
                if ((data.getStringExtra(IntentConstants.ON_QR_SCANNED) == "NA"))
                    showError(resources.getString(R.string.not_valid_post_qr))
                else
                    viewModel.onGuardQrScanned(data.getStringExtra(IntentConstants.ON_QR_SCANNED)!!)
//                    viewModel.onGuardQrScanned.set(data.getStringExtra(IntentConstants.ON_QR_SCANNED))
            } else if (requestCode == REQUEST_CODE_TAKE_PICTURE) {
                val photoData =
                    Parcels.unwrap<AttachmentEntity>(data.extras!!.getParcelable(AttachmentEntity::class.java.simpleName))
                if (photoData != null)
                    viewModel.obsPhotoAttachment.set(photoData)
                else
                    showError("Unable to Capture Image")
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
    }
}