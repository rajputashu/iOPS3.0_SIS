package com.sisindia.ai.android.features.nudges

import android.app.Activity
import android.content.Intent
import android.os.Message
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.commons.audiorecord.AudioRecordingBottomSheetFragment
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.IntentRequestCodes
import com.sisindia.ai.android.constants.NavigationConstants.*
import com.sisindia.ai.android.databinding.ActivityDynamicNudgesBinding
import com.sisindia.ai.android.databinding.ActivityDynamicTaskBinding
import com.sisindia.ai.android.features.imagecapture.CaptureImageActivityV2
import com.sisindia.ai.android.mlcore.ScanQRActivity
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.utils.TimeUtils
import org.parceler.Parcels
import org.parceler.Parcels.unwrap

/**
 * Created by Ashu_Rajput on 04/11/2024.
 */
class NudgesDynamicActivity : IopsBaseActivity() {

    private var viewModel: NudgesDynamicViewModel? = null
    private lateinit var binding: ActivityDynamicNudgesBinding

    override fun getLayoutResource(): Int {
        return R.layout.activity_dynamic_nudges
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbDynamicTasks)

        viewModel?.apply {
            fetchJsonFormViaId()
        }
    }

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun extractBundle() {
        viewModel?.obsTaskTypeId?.set(intent.extras?.getInt(IntentConstants.DYNAMIC_FORM_ID, 1)!!)
    }

    override fun initViewState() {

        liveData.observe(this) { message: Message ->
            when (message.what) {

                OPEN_LIVE_QR_SCANNER_SCREEN -> {
                    openQRScanner()
                }

                ON_ADD_AUDIO_CLIP_CLICK -> {
                    openAddAudioClipBottomSheet()
                }

                ON_TAKE_PICTURE -> {
                    val otherAttachment = message.obj as AttachmentEntity
                    openCaptureImage(otherAttachment)
                }

                ON_AUDIO_RECORDED_FOR_GRIEVANCE -> {
                    val audioFile = message.obj as String
                    viewModel?.onAudioRecorded(audioFile)
                }

                OPEN_DATE_TIME_PICKER -> {
//                    openDateTimePicker()
                }

                ON_DYNAMIC_TASK_COMPLETE -> {
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(NudgesDynamicViewModel::class.java) as NudgesDynamicViewModel
    }

    private fun openAddAudioClipBottomSheet() {
        if (supportFragmentManager.findFragmentByTag(AudioRecordingBottomSheetFragment::class.java.simpleName) == null) {
            val fragment: BottomSheetDialogFragment =
                AudioRecordingBottomSheetFragment.newInstance()
            fragment.isCancelable = false
            fragment.show(supportFragmentManager,
                AudioRecordingBottomSheetFragment::class.java.simpleName)
        }
    }

    private fun openQRScanner() {
        /*startActivityForResult(ScanQRActivity.newIntent(this, true),
            IntentRequestCodes.REQUEST_CODE_OPEN_POST_SCAN)*/
        val intent = ScanQRActivity.newIntent(this, true)
        qrLauncher.launch(intent)
    }

    private fun openCaptureImage(otherAttachment: AttachmentEntity) {
        /*startActivityForResult(CaptureImageActivityV2.newIntent(this, otherAttachment),
            IntentRequestCodes.REQUEST_CODE_TAKE_PICTURE)*/
        val intent = CaptureImageActivityV2.newIntent(this,otherAttachment)
        pictureLauncher.launch(intent)
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == IntentRequestCodes.REQUEST_CODE_TAKE_PICTURE) {
                if (data.extras!!.containsKey(AttachmentEntity::class.java.simpleName)) {
                    viewModel?.apply {
                        imageAttachment.set(unwrap(data.extras!!.getParcelable(
                            AttachmentEntity::class.java.simpleName)))
                        updateImageCaptureStatus()
                    }
                } else
                    showToast("Unable to Capture Image")
            } else if (requestCode == IntentRequestCodes.REQUEST_CODE_OPEN_POST_SCAN) {
                if ((data.getStringExtra(IntentConstants.ON_QR_SCANNED) == "NA"))
                    showToast(resources.getString(R.string.not_valid_post_qr))
                else
                    viewModel?.updateScannedQR(data.getStringExtra(IntentConstants.ON_QR_SCANNED)!!)
            }
        }
    }*/

    private var pictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.extras.apply {
                    if (this!!.containsKey(AttachmentEntity::class.java.simpleName)) {
                        viewModel?.apply {
                            imageAttachment.set(unwrap(getParcelable(
                                AttachmentEntity::class.java.simpleName)))
                            updateImageCaptureStatus()
                        }
                    } else
                        showToast("Unable to capture image")
                }
            }
        }

    private var qrLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if ((result.data?.getStringExtra(IntentConstants.ON_QR_SCANNED) == "NA"))
                    showToast(resources.getString(R.string.not_valid_post_qr))
                else
                    viewModel?.updateScannedQR(result.data?.getStringExtra(IntentConstants.ON_QR_SCANNED)!!)
            }
        }
}