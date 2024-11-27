package com.sisindia.ai.android.features.practo

import android.app.Activity
import android.os.Message
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.NavigationConstants.ON_GUARD_AVAILABLE
import com.sisindia.ai.android.constants.NavigationConstants.ON_SCAN_GUARD_QR_CLICK
import com.sisindia.ai.android.constants.NavigationConstants.OPEN_DASH_BOARD
import com.sisindia.ai.android.constants.NavigationConstants.OPEN_DIRECT_PRACTO_BOTTOM_SHEET
import com.sisindia.ai.android.databinding.ActivityPractoTaskBinding
import com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.ScanGuardActivity
import com.sisindia.ai.android.utils.TimeUtils

/**
 * Created by Ashu Rajput on 10-07-2023
 */
class PractoTaskActivity : IopsBaseActivity() {

    private lateinit var viewModel: PractoTaskViewModel
    private lateinit var binding: ActivityPractoTaskBinding

    override fun getLayoutResource(): Int {
        return R.layout.activity_practo_task
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(PractoTaskViewModel::class.java) as PractoTaskViewModel
    }

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbPractoTask)
        /*loadFragment(R.id.practoFragmentContainer, GuardAvailableFragment.newInstance(true),
            FRAGMENT_REPLACE, false)*/
        viewModel.getTaskSiteName()
        viewModel.initTaskUI()
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {
                ON_GUARD_AVAILABLE -> {
                    /*loadFragment(R.id.practoFragmentContainer, PractoTaskFragment.newInstance(),
                        FRAGMENT_REPLACE, false)*/
                }

                ON_SCAN_GUARD_QR_CLICK -> {
                    val intent = ScanGuardActivity.newIntent(this, true)
                    resultLauncher.launch(intent)
                }

                OPEN_DIRECT_PRACTO_BOTTOM_SHEET -> openBottomSheet()

                NavigationConstants.ON_PRACTO_QUESTION_COMPLETED -> viewModel.initTaskUI()

                NavigationConstants.TASK_TIMER_TIK -> {
                    bindReviewInformationTime(message.arg1)
                }

                OPEN_DASH_BOARD -> {
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.initTaskUI()
                openBottomSheet()
            }
        }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun bindReviewInformationTime(seconds: Int) {
        binding.includeTimeSpent.tvTimeSpent.text = TimeUtils.convertIntSecondsToHHMMSS(seconds)
    }

    private fun openBottomSheet() {
        if (supportFragmentManager.findFragmentByTag(PractoQuestionsBottomSheet::class.java.simpleName) == null) {
            val otpBottomSheet = PractoQuestionsBottomSheet.newInstance()
            otpBottomSheet.show(supportFragmentManager,
                PractoQuestionsBottomSheet::class.java.simpleName)
            otpBottomSheet.isCancelable = false
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startTimer()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopTimer()
    }
}