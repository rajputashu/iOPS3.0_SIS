package com.sisindia.ai.android.features.selfie

import android.app.Activity
import androidx.databinding.DataBindingUtil
import com.droidcommons.base.BaseActivity
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.ActivityDutyOnSelfieBinding
import com.sisindia.ai.android.features.imagecapture.CaptureImageFragmentV2
import com.sisindia.ai.android.room.entities.AttachmentEntity

/**
 * Created by Ashu Rajput on 11/27/2020.
 */
class DutyOnOffSelfie : IopsBaseActivity() {

    private lateinit var binding: ActivityDutyOnSelfieBinding
    private var viewModel: SelfieViewModel? = null

    override fun getLayoutResource(): Int {
        return R.layout.activity_duty_on_selfie
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(SelfieViewModel::class.java) as SelfieViewModel
    }

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun initViewState() {
        liveData.observe(this) {
            when (it.what) {
                NavigationConstants.ON_SELFIE_IMAGE_CAPTURED_V2 -> {

                    openPreviewImageScreen()

                    /*val item = it.obj as AttachmentEntity
                    viewModel!!.let { vm ->
                        vm.photoAttachmentObs.set(item)
                        vm.updateSefieAttachment()
                    }*/
                }
                NavigationConstants.ON_SELFIE_ATTACHMENT_INSERTED -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }

                NavigationConstants.ON_IMAGE_RETAKE_V2 -> {
                    openCameraForSelfieFragment()
                }

                NavigationConstants.ON_IMAGE_CONFIRM_V2 -> {
                    val item = it.obj as AttachmentEntity
                    viewModel!!.let { vm ->
                        vm.photoAttachmentObs.set(item)
                        vm.updateSefieAttachment()
                    }
                }
            }
        }
    }

    override fun onCreated() {
        openCameraForSelfieFragment()
    }

    override fun extractBundle() {
    }

    private fun openCameraForSelfieFragment() {
        loadFragment(R.id.flCaptureImage,
            CaptureImageFragmentV2.newInstanceForSelfie(viewModel!!.photoAttachmentObs.get()!!),
            BaseActivity.FRAGMENT_REPLACE,
            false)
    }

    private fun openPreviewImageScreen() {
        loadFragment(R.id.flCaptureImage, SelfiePreviewFragmentV2.newInstance(),
            BaseActivity.FRAGMENT_REPLACE, false)
    }
}