package com.sisindia.ai.android.features.clientcoordination

import android.os.Message
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.droidcommons.base.timer.CountUpTimer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.ActivityClientCoordinationBinding
import com.sisindia.ai.android.utils.TimeUtils

/**
 * Created by Ashu Rajput on 3/16/2020.
 */
class ClientCoordinationActivity : IopsBaseActivity() {

    private lateinit var binding: ActivityClientCoordinationBinding
    private var viewModel: ClientCoordinationViewModel? = null

    override fun getLayoutResource(): Int {
        return R.layout.activity_client_coordination
    }

    override fun initViewModel() {
        viewModel =
            getAndroidViewModel(ClientCoordinationViewModel::class.java) as ClientCoordinationViewModel
    }

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbClientCoordination)
        viewModel!!.timer.start()
        viewModel.apply {
            this!!.updateUnitName("GSVM BLOOD BANK")
            this.updateContactList(arrayListOf("GM : DR. Rahul Prasad", "Manager : Prakash Ranjan"))
        }
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveTimerEvent.observe(this,
            Observer { message: Message ->
                when (message.what) {
                    CountUpTimer.REVIEW_INFORMATION_TIME_SPENT -> bindReviewInformationTime(message.arg1)
                }
            })

        liveData.observe(this,
            Observer { message: Message ->
                when (message.what) {
                    NavigationConstants.OPEN_DASH_BOARD -> {
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
}