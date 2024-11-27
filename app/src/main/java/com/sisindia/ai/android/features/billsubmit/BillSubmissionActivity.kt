package com.sisindia.ai.android.features.billsubmit

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Message
import androidx.databinding.DataBindingUtil
import com.droidcommons.base.timer.CountUpTimer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.commons.CaptureImageType
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.IntentRequestCodes
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.ActivityBillSubmissionBinding
import com.sisindia.ai.android.features.billsubmit.sheet.BillChecklistBottomSheet
import com.sisindia.ai.android.features.livecamera.ImageCaptureActivity
import com.sisindia.ai.android.features.units.sheet.AddEquipmentBottomSheet
import com.sisindia.ai.android.uimodels.billsubmit.BillCheckListMO
import com.sisindia.ai.android.utils.TimeUtils

/**
 * Created by Ashu Rajput on 3/12/2020.
 */
class BillSubmissionActivity : IopsBaseActivity() {

    private lateinit var binding: ActivityBillSubmissionBinding
    private var viewModel: BillSubmissionViewModel? = null

    override fun getLayoutResource(): Int {
        return R.layout.activity_bill_submission
    }

    override fun initViewModel() {
        viewModel =
            getAndroidViewModel(BillSubmissionViewModel::class.java) as BillSubmissionViewModel
    }

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbOtherTasks)

        viewModel!!.apply {
            timer.start()

            val bundle = intent.extras
            if (bundle != null && bundle.containsKey("SITE_NAME_KEY"))
                unitName.set(bundle.getString("SITE_NAME_KEY", ""))

            initBSViews()
        }
    }

    override fun extractBundle() {
        /*val collectionMO =
            intent.extras!!.getParcelable<BillSubmissionCardDetailsMO>(IntentConstants.DUE_BILL_COLLECTION)*/
    }

    override fun initViewState() {
        liveTimerEvent.observe(this) { message: Message ->
            when (message.what) {
                CountUpTimer.REVIEW_INFORMATION_TIME_SPENT -> bindReviewInformationTime(message.arg1)
            }
        }

        liveData.observe(this) { message: Message ->
            when (message.what) {
                NavigationConstants.OPEN_DASH_BOARD -> {
                    showToast(resources.getString(R.string.msg_bill_submit_task_complete))
                    setResult(RESULT_OK)
                    finish()
                }
                NavigationConstants.OPEN_BILL_CHECKLIST_SHEET -> {
                    openBillCheckListBottomSheet()
                }
                NavigationConstants.ON_TAKE_PICTURE -> {
                    startActivityForResult(ImageCaptureActivity.newIntentWithType(this,
                        CaptureImageType.BILL_SUBMIT),
                        IntentRequestCodes.REQUEST_CODE_TAKE_PICTURE)
                }
                NavigationConstants.ON_BILL_CHECKLIST_OPTED -> {
                    val receivedList = message.obj as ArrayList<*>
                    var pendingListCounter = 0
                    if (message.obj is ArrayList<*>) {
                        val checkList = receivedList.filterIsInstance<BillCheckListMO>()
                        for (checkListMO: BillCheckListMO in checkList) {
                            if (!checkListMO.isOptionOpted)
                                pendingListCounter += 1
                        }
                        viewModel!!.apply {
                            updatePendingCheckListCount(pendingListCounter)
                            updateList.set(checkList)
                        }
                    }
                }
            }
        }
    }

    private fun bindReviewInformationTime(seconds: Int) {
        binding.includeTimeSpent.tvTimeSpent.text = TimeUtils.convertIntSecondsToHHMMSS(seconds)
    }

    private fun openBillCheckListBottomSheet() {
        if (supportFragmentManager.findFragmentByTag(AddEquipmentBottomSheet::class.java.simpleName) == null) {
            BillChecklistBottomSheet.newInstance(ArrayList(viewModel!!.updateList.get()!!))
                .show(supportFragmentManager, BillChecklistBottomSheet::class.java.simpleName)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentRequestCodes.REQUEST_CODE_TAKE_PICTURE && resultCode == Activity.RESULT_OK && data != null) {
            showToast("Photo captured successfully...")
            data.extras!!.apply {
                viewModel!!.billImageUri.set(Uri.parse(getString(IntentConstants.CAPTURED_FILE_URI)))
                viewModel!!.billMetadata.set(getString(IntentConstants.FILE_METADATA))
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}