package com.sisindia.ai.android.features.imagecapture

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.droidcommons.base.BaseActivity
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.ActivityCaptureImageBinding
import com.sisindia.ai.android.room.entities.AttachmentEntity
import org.parceler.Parcels

class CaptureImageActivityV2 : IopsBaseActivity() {

    lateinit var binding: ActivityCaptureImageBinding
    lateinit var viewModel: CaptureImageViewModel

    override fun extractBundle() {
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(AttachmentEntity::class.java.simpleName)) {
            val item =
                Parcels.unwrap<AttachmentEntity>(bundle.getParcelable(AttachmentEntity::class.java.simpleName))
            viewModel.itemObs.set(item)
        }
    }

    override fun initViewState() {
        liveData.observe(this) {
            when (it.what) {
                NavigationConstants.ON_IMAGE_CAPTURED_V2 -> openPreviewImageScreen()

                NavigationConstants.ON_IMAGE_RETAKE_V2 -> openCaptureImageFragment()

                NavigationConstants.ON_IMAGE_CONFIRM_V2 -> {
                    val item = it.obj as AttachmentEntity
                    onImageCaptured(item)
                }
            }
        }
    }

    private fun onImageCaptured(item: AttachmentEntity) {
        val returnIntent = Intent()
        returnIntent.putExtra(AttachmentEntity::class.java.simpleName, Parcels.wrap(item))
        setResult(Activity.RESULT_OK, returnIntent)
        this.finish()
    }

    private fun openPreviewImageScreen() {
        loadFragment(R.id.flCaptureImage,
            PreviewImageFragmentV2.newInstance(),
            BaseActivity.FRAGMENT_REPLACE,
            false)
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbCaptureImage)
        openCaptureImageFragment()
    }

    private fun openCaptureImageFragment() {
        loadFragment(R.id.flCaptureImage,
            CaptureImageFragmentV2.newInstance(),
            BaseActivity.FRAGMENT_REPLACE,
            false)
    }

    override fun initViewBinding() {
        binding = bindActivityView(this, layoutResource) as ActivityCaptureImageBinding
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(CaptureImageViewModel::class.java) as CaptureImageViewModel
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_capture_image
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        @JvmStatic
        fun newIntent(activity: Activity, item: AttachmentEntity): Intent {
            val intent = Intent(activity, CaptureImageActivityV2::class.java)
            val bundle = Bundle()
            bundle.putParcelable(AttachmentEntity::class.java.simpleName, Parcels.wrap(item))
            intent.putExtras(bundle)
            return intent
        }
    }
}