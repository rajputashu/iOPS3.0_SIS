package com.sisindia.ai.android.features.taskcheck.clienthandshake

import android.app.Activity
import android.content.Intent
import android.os.Message
import androidx.databinding.DataBindingUtil
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.constants.NavigationConstants.*
import com.sisindia.ai.android.databinding.ActivityClientHandshakeBinding
import com.sisindia.ai.android.features.taskcheck.clienthandshake.frags.ClientFeedbackFragment
import com.sisindia.ai.android.features.taskcheck.clienthandshake.frags.ClientHandShakeAddComplaintFragment
import com.sisindia.ai.android.features.taskcheck.clienthandshake.frags.ClientHandshakeFragment
import com.sisindia.ai.android.features.taskcheck.clienthandshake.frags.SummaryOfHandshakeFragment
import com.sisindia.ai.android.utils.TimeUtils
import com.sisindia.ai.android.utils.loadExtFragment
import com.sisindia.ai.android.utils.unloadFragment

/**
 * Created by Ashu Rajput on 4/8/2020.
 */
class ClientHandshakeActivity : IopsBaseActivity() {

    private lateinit var binding: ActivityClientHandshakeBinding
    private var viewModel: ClientHandshakeViewModel? = null

    companion object {
        @JvmStatic
        fun newIntent(activity: Activity): Intent {
            return Intent(activity, ClientHandshakeActivity::class.java)
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_client_handshake
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(ClientHandshakeViewModel::class.java)
                as ClientHandshakeViewModel
    }

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun extractBundle() {
    }

    override fun initViewState() {

        liveData.observe(this) { message: Message ->
            when (message.what) {

                TASK_TIMER_TIK -> bindReviewInformationTime(message.arg1)

                ON_CONTINUE_ADD_FEEDBACK -> openClientFeedbackFragment()

                OPEN_FINAL_SUMMARY_HANDSHAKE -> openFinalSummaryFragment(message.obj as String)

                ON_COMPLETE_CLIENT_HANDSHAKE -> finish()

                ON_CLIENT_HANDSHAKE_COMPLAINT -> openClientHandShakeComplaint()

                ON_COMPLAINT_ADDED_CONTINUE -> {
                    onBackPressed()
                }
            }
        }
    }

    private fun openClientHandShakeComplaint() {
        loadExtFragment(R.id.flClientHandshake, ClientHandShakeAddComplaintFragment.newInstance(),
            true, 0)
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbClientHandshake)
        loadExtFragment(R.id.flClientHandshake, ClientHandshakeFragment.newInstance(), false, 0)
    }

    private fun bindReviewInformationTime(seconds: Int) {
        binding.includeTimeSpent.tvTimeSpent.text = TimeUtils.convertIntSecondsToHHMMSS(seconds)
    }

    private fun openClientFeedbackFragment() {
        loadExtFragment(R.id.flClientHandshake, ClientFeedbackFragment.newInstance(), true, 0)
    }

    private fun openFinalSummaryFragment(optedQuestions: String) {
        loadExtFragment(R.id.flClientHandshake,
            SummaryOfHandshakeFragment.newInstance(optedQuestions), true, 0)
    }

    override fun onBackPressed() {
        if (!unloadFragment())
            super.onBackPressed()
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