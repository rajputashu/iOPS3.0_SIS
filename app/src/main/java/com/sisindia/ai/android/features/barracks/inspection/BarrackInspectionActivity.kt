package com.sisindia.ai.android.features.barracks.inspection

import android.os.Message
import androidx.databinding.DataBindingUtil
import com.droidcommons.base.timer.CountUpTimer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.ActivityBarrackInspectionBinding
import com.sisindia.ai.android.features.barracks.inspection.frags.*
import com.sisindia.ai.android.utils.TimeUtils
import com.sisindia.ai.android.utils.loadExtFragment
import com.sisindia.ai.android.utils.unloadFragment

/**
 * Created by Ashu Rajput on 4/20/2020.
 */
class BarrackInspectionActivity : IopsBaseActivity() {

    private lateinit var binding: ActivityBarrackInspectionBinding
    private var viewModel: BarrackInspectionViewModel? = null

    override fun getLayoutResource(): Int {
        return R.layout.activity_barrack_inspection
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(BarrackInspectionViewModel::class.java)
                as BarrackInspectionViewModel
    }

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbBarrackInspection)
        viewModel!!.apply {
            timer.start()
            updateTaskExecutionStartDetails()
        }
        loadExtFragment(R.id.flBarrackInspection, BarrackInspectionHomeFragment.newInstance(),
            false, 0)
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveTimerEvent.observe(this, { message: Message ->
            when (message.what) {
                CountUpTimer.REVIEW_INFORMATION_TIME_SPENT -> bindReviewInformationTime(message.arg1)
            }
        })

        liveData.observe(this, { message: Message ->
            when (message.what) {
                NavigationConstants.OPEN_DASH_BOARD -> {
                    setResult(RESULT_OK)
                    finish()
                }
                NavigationConstants.OPEN_BARRACK_INSPECTION_STRENGTH -> openBarrackStrengthInspectionFragment()
                NavigationConstants.OPEN_BARRACK_INSPECTION_SPACE -> openBarrackSpaceInspectionFragment()
                NavigationConstants.OPEN_BARRACK_INSPECTION_OTHERS -> openBarrackOthersInspectionFragment()
                NavigationConstants.OPEN_BARRACK_INSPECTION_LANDLORD -> openBarrackMetLandlordInspectionFragment()
            }
        })
    }

    private fun bindReviewInformationTime(seconds: Int) {
        binding.includeTimeSpent.tvTimeSpent.text = TimeUtils.convertIntSecondsToHHMMSS(seconds)
    }

    private fun openBarrackStrengthInspectionFragment() {
        loadExtFragment(R.id.flBarrackInspection, BarrackStrengthFragment.newInstance(), true, 0)
    }

    private fun openBarrackSpaceInspectionFragment() {
        loadExtFragment(R.id.flBarrackInspection, BarrackSpaceFragment.newInstance(), true, 0)
    }

    private fun openBarrackOthersInspectionFragment() {
        loadExtFragment(R.id.flBarrackInspection, BarrackOthersFragment.newInstance(), true, 0)
    }

    private fun openBarrackMetLandlordInspectionFragment() {
        loadExtFragment(R.id.flBarrackInspection, BarrackMetLandlordFragment.newInstance(), true, 0)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (!unloadFragment())
            super.onBackPressed()
    }
}