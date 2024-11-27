package com.sisindia.ai.android.features.taskcheck.strengthcheck

import android.app.Activity
import android.content.Intent
import android.os.Message
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.constants.NavigationConstants.OPEN_DASH_BOARD
import com.sisindia.ai.android.constants.NavigationConstants.TASK_TIMER_TIK
import com.sisindia.ai.android.databinding.ActivityStrengthCheckBinding
import com.sisindia.ai.android.utils.TimeUtils

/**
 * Created by Ashu Rajput on 3/18/2020.
 */
class StrengthCheckActivity : IopsBaseActivity() {

    private lateinit var binding: ActivityStrengthCheckBinding
    private var viewModel: StrengthCheckViewModel? = null

    override fun getLayoutResource(): Int {
        return R.layout.activity_strength_check
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(StrengthCheckViewModel::class.java) as StrengthCheckViewModel
    }

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbStrengthCheck)
        viewModel!!.apply {
            this.initViewModel()
        }
    }

    override fun extractBundle() {
    }

    override fun initViewState() {

        liveData.observe(this,
            Observer { message: Message ->
                when (message.what) {
                    TASK_TIMER_TIK -> bindReviewInformationTime(message.arg1)
                    OPEN_DASH_BOARD -> {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
            })
    }

    private fun bindReviewInformationTime(seconds: Int) {
        binding.includeTimeSpent.tvTimeSpent.text = TimeUtils.convertIntSecondsToHHMMSS(seconds)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        @JvmStatic
        fun newIntent(act: Activity): Intent {
            return Intent(act, StrengthCheckActivity::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel!!.startTimer()
    }

    override fun onPause() {
        super.onPause()
        viewModel!!.stopTimer()
    }
}