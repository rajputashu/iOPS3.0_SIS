package com.sisindia.ai.android.features.akr.details

import android.app.Activity
import android.content.Intent
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.commons.CaptureSignatureBottomSheetFragment
import com.sisindia.ai.android.constants.IntentRequestCodes
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentKitReplaceBinding
import com.sisindia.ai.android.features.imagecapture.CaptureImageActivityV2.Companion.newIntent
import com.sisindia.ai.android.room.entities.AttachmentEntity
import org.parceler.Parcels

/**
 * Created by Ashu Rajput on 4/17/2020.
 */
class KitReplaceFragment : IopsBaseFragment() {

    private lateinit var viewModel: KitReplaceViewModel
    private lateinit var binding: FragmentKitReplaceBinding

    companion object {
        fun newInstance(): KitReplaceFragment = KitReplaceFragment()
        /*fun newInstance(isWinterKitRequest: Boolean): KitReplaceFragment {
            val kitFragment = KitReplaceFragment()
            Bundle().apply {
                putBoolean(IntentConstants.IS_WINTER_KIT_REQUEST, isWinterKitRequest)
                kitFragment.arguments = this
            }
            return kitFragment
        }*/
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_kit_replace
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(KitReplaceViewModel::class.java) as KitReplaceViewModel
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater!!, layoutResource, container, false)
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun extractBundle() {
        /*val isWinterKit = arguments?.getBoolean(IntentConstants.IS_WINTER_KIT_REQUEST, false)
        isWinterKit?.apply {
            viewModel.obsIsWinterKitRequest.set(this)
        }*/
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {

                NavigationConstants.ON_TAKE_PICTURE ->
                    openImageCaptureForKitRequest(message.obj as AttachmentEntity)

                NavigationConstants.ON_KIT_VOUCHER_CLICK ->
                    openImageCaptureForVoucher(message.obj as AttachmentEntity)

                NavigationConstants.ON_TAKE_SIGNATURE ->
                    openSignatureCaptureForKitRequest(message.obj as AttachmentEntity)

                NavigationConstants.ON_KIT_OTP_REQUEST -> openOTPBottomSheet()

                NavigationConstants.ON_SUCCESSFUL_OTP_VERIFY_SKIP ->
                    viewModel.updateKitDistributionItems()

                NavigationConstants.ON_KIT_REPLACE_DONE -> {
                    message.what = NavigationConstants.ON_RESUME_KIT_REPLACE
                    liveData.postValue(message)
                    requireActivity().onBackPressed()
                }
            }
        }
    }

    private fun openImageCaptureForKitRequest(item: AttachmentEntity) {
        //        startActivityForResult(ImageCaptureActivity.newIntentWithType(this,
        //            CaptureImageType.GUARD_FULL_PHOTO),
        //            IntentRequestCodes.REQUEST_CODE_TAKE_PICTURE)

        startActivityForResult(newIntent(requireActivity(), item),
            IntentRequestCodes.REQUEST_CODE_TAKE_PICTURE)
    }

    private fun openImageCaptureForVoucher(item: AttachmentEntity) {
        startActivityForResult(newIntent(requireActivity(), item),
            IntentRequestCodes.REQUEST_CODE_KIT_VOUCHER)
    }

    override fun onCreated() {
        viewModel.apply {
            fetchReasonsAndKitItemsToDistribute()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            IntentRequestCodes.REQUEST_CODE_TAKE_PICTURE ->
                if (resultCode == Activity.RESULT_OK && data != null && data.extras != null && data.extras!!
                        .containsKey(AttachmentEntity::class.java.simpleName)) {
                    if (Parcels.unwrap<AttachmentEntity>(data.extras!!.getParcelable(
                            AttachmentEntity::class.java.simpleName)) != null)
                        viewModel.photoAttachmentObs.set(Parcels.unwrap(data.extras!!.getParcelable(
                            AttachmentEntity::class.java.simpleName)))
                } else
                    errorOnPhotoCapture()

            IntentRequestCodes.REQUEST_CODE_KIT_VOUCHER ->
                if (resultCode == Activity.RESULT_OK && data != null && data.extras != null && data.extras!!
                        .containsKey(AttachmentEntity::class.java.simpleName)) {
                    if (Parcels.unwrap<AttachmentEntity>(data.extras!!.getParcelable(
                            AttachmentEntity::class.java.simpleName)) != null)
                        viewModel.voucherPhotoAttachmentObs.set(Parcels.unwrap(data.extras!!.getParcelable(
                            AttachmentEntity::class.java.simpleName)))
                } else
                    errorOnPhotoCapture()
        }
    }

    private fun openSignatureCaptureForKitRequest(signAttachment: AttachmentEntity) {
        if (childFragmentManager.findFragmentByTag(CaptureSignatureBottomSheetFragment::class.java.simpleName) == null) {
            val fragment =
                CaptureSignatureBottomSheetFragment.newInstance(signAttachment)
            fragment.isCancelable = false
            fragment.setOnSignDoneListeners { item: AttachmentEntity? ->
                viewModel.signAttachmentObs.set(item)
                viewModel.signAttachmentObs.notifyChange()
            }
            fragment.show(childFragmentManager,
                CaptureSignatureBottomSheetFragment::class.java.simpleName)
        }
    }

    private fun errorOnPhotoCapture() {
        Toast.makeText(requireActivity(), "Unable to Capture Image", Toast.LENGTH_SHORT).show()
    }

    private fun openOTPBottomSheet() {
        requireActivity().apply {
            if (supportFragmentManager.findFragmentByTag(KitOtpBottomSheet::class.java.simpleName) == null) {
                val otpBottomSheet = KitOtpBottomSheet.newInstance(viewModel.guardMobileNumber,
                    viewModel.selectedOtpTypeId)
                otpBottomSheet.show(supportFragmentManager,
                    KitOtpBottomSheet::class.java.simpleName)
                otpBottomSheet.isCancelable = false
            }
        }
    }
}