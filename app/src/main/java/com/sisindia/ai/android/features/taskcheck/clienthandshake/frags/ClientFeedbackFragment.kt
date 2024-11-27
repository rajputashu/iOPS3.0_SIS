package com.sisindia.ai.android.features.taskcheck.clienthandshake.frags

import android.app.Activity
import android.content.Intent
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_CREATE_COMPLAINT
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.NavigationConstants.ON_COMPLAINT_ADDED_CONTINUE
import com.sisindia.ai.android.databinding.FragmentClientFeedbackBinding
import com.sisindia.ai.android.features.issues.complaints.CreateComplaintIssueActivity
import com.sisindia.ai.android.features.taskcheck.clienthandshake.sheet.FeedbackOtpBottomSheet

/**
 * Created by Ashu Rajput on 4/9/2020.
 */
class ClientFeedbackFragment : IopsBaseFragment() {

    private lateinit var viewModel: ClientFeedbackViewModel
    private lateinit var binding: FragmentClientFeedbackBinding

    companion object {
        fun newInstance(): ClientFeedbackFragment = ClientFeedbackFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_client_feedback
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(ClientFeedbackViewModel::class.java)
                as ClientFeedbackViewModel
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater!!, layoutResource, container, false)
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {
                NavigationConstants.OPEN_OTP_BOTTOM_SHEET -> openOTPBottomSheet()
                NavigationConstants.ON_SUCCESSFUL_OTP_VERIFY -> {
                    /*val optedQuestions = viewModel.adapter.getSelectedQuestion()
                    viewModel.clientListener.onFinalSummaryHandshakeView(optedQuestions)*/
                    viewModel.getCacheClientHandshakeDetails()
                }
                //                    NavigationConstants.ON_CLIENT_HANDSHAKE_COMPLAINT -> openAddComplaint()
                ON_COMPLAINT_ADDED_CONTINUE -> {
                    viewModel.fetchAddedComplaintsFromDB()
                }
            }
        }
    }

    override fun onCreated() {
        viewModel.apply {
            fetchClientDetails()
        }
    }

    private fun openOTPBottomSheet() {
        requireActivity().apply {
            if (supportFragmentManager.findFragmentByTag(FeedbackOtpBottomSheet::class.java.simpleName) == null) {
                val otpBottomSheet: FeedbackOtpBottomSheet =
                    FeedbackOtpBottomSheet.newInstance(viewModel.clientNumber.get().toString())
                otpBottomSheet.show(supportFragmentManager,
                    FeedbackOtpBottomSheet::class.java.simpleName)
                otpBottomSheet.isCancelable = false
            }
        }
    }

    private fun openAddComplaint() {
        startActivityForResult(CreateComplaintIssueActivity.newIntent(requireActivity()),
            REQUEST_CODE_CREATE_COMPLAINT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CREATE_COMPLAINT && resultCode == Activity.RESULT_OK)
            viewModel.fetchAddedComplaintsFromDB()
    }
}