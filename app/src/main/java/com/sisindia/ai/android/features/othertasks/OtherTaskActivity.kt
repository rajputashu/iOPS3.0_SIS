package com.sisindia.ai.android.features.othertasks

import android.app.Activity
import android.content.Intent
import android.os.Message
import androidx.databinding.DataBindingUtil
import com.droidcommons.base.timer.CountUpTimer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_TAKE_PICTURE
import com.sisindia.ai.android.constants.NavigationConstants.ON_OTHER_TASK_COMPLETE
import com.sisindia.ai.android.constants.NavigationConstants.ON_TAKE_PICTURE
import com.sisindia.ai.android.databinding.ActivityOtherTaskBinding
import com.sisindia.ai.android.features.imagecapture.CaptureImageActivityV2
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.utils.TimeUtils
import org.parceler.Parcels

/**
 * Created by Ashu Rajput on 3/12/2020.
 */
class OtherTaskActivity : IopsBaseActivity() {

    lateinit var binding: ActivityOtherTaskBinding
    lateinit var viewModel: OtherTaskViewModel

    override fun getLayoutResource(): Int {
        return R.layout.activity_other_task
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(OtherTaskViewModel::class.java) as OtherTaskViewModel
    }

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbOtherTasks)
        viewModel.apply {
            timer.start()
            updateTaskExecutionStartDetails()
            getOtherTaskDetails()
        }
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveTimerEvent.observe(this) { message: Message ->
            when (message.what) {
                CountUpTimer.REVIEW_INFORMATION_TIME_SPENT -> bindReviewInformationTime(message.arg1)
            }
        }

        liveData.observe(this) { message: Message ->
            when (message.what) {
                ON_OTHER_TASK_COMPLETE -> {
                    setResult(RESULT_OK)
                    finish()
                }
                ON_TAKE_PICTURE -> {
                    val otherAttachment = message.obj as AttachmentEntity
                    openCaptureImageForOtherAttachment(otherAttachment)
                }
            }
        }
    }

    private fun openCaptureImageForOtherAttachment(otherAttachment: AttachmentEntity) {
        startActivityForResult(CaptureImageActivityV2.newIntent(this, otherAttachment),
            REQUEST_CODE_TAKE_PICTURE)
    }

    private fun bindReviewInformationTime(seconds: Int) {
        binding.includeTimeSpent.tvTimeSpent.text = TimeUtils.convertIntSecondsToHHMMSS(seconds)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                if (data != null && data.extras != null && data.extras!!.containsKey(
                        AttachmentEntity::class.java.simpleName)) {
                    viewModel.otherAttachment.set(Parcels.unwrap(data.extras!!.getParcelable(
                        AttachmentEntity::class.java.simpleName)))
                } else
                    showToast("Unable to Capture Image")
            }
        }
    }
}