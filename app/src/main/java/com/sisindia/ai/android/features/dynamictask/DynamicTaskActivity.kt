package com.sisindia.ai.android.features.dynamictask

import android.app.Activity
import android.content.Intent
import android.os.Message
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.commons.audiorecord.AudioRecordingBottomSheetFragment
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.IntentRequestCodes
import com.sisindia.ai.android.constants.NavigationConstants.*
import com.sisindia.ai.android.databinding.ActivityDynamicTaskBinding
import com.sisindia.ai.android.features.imagecapture.CaptureImageActivityV2
import com.sisindia.ai.android.mlcore.ScanQRActivity
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.utils.TimeUtils
import org.parceler.Parcels.unwrap

/**
 * Created by Ashu_Rajput on 6/2/2021.
 */
class DynamicTaskActivity : IopsBaseActivity() {

    private var viewModel: DynamicTaskViewModel? = null
    private lateinit var binding: ActivityDynamicTaskBinding

    override fun getLayoutResource(): Int {
        return R.layout.activity_dynamic_task
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbDynamicTasks)

        viewModel?.apply {
            fetchJsonFormViaId()
            updateTaskExecutionStartDetails()
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
                TASK_TIMER_TIK -> {
                    bindReviewInformationTime(message.arg1)
                }

                OPEN_LIVE_QR_SCANNER_SCREEN -> {
                    startActivityForResult(ScanQRActivity.newIntent(this, true),
                        IntentRequestCodes.REQUEST_CODE_OPEN_POST_SCAN)
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

                ON_DYNAMIC_TASK_COMPLETE -> {
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(DynamicTaskViewModel::class.java) as DynamicTaskViewModel
    }

    private fun bindReviewInformationTime(seconds: Int) {
        binding.includeTimeSpent.tvTimeSpent.text = TimeUtils.convertIntSecondsToHHMMSS(seconds)
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

    private fun openCaptureImage(otherAttachment: AttachmentEntity) {
        startActivityForResult(CaptureImageActivityV2.newIntent(this, otherAttachment),
            IntentRequestCodes.REQUEST_CODE_TAKE_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
    }

    override fun onResume() {
        super.onResume()
        viewModel?.startTimer()
    }

    override fun onPause() {
        super.onPause()
        viewModel?.stopTimer()
    }
}