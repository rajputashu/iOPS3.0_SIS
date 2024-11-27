package com.sisindia.ai.android.features.taskcheck.clienthandshake.frags

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.IntentRequestCodes
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentFinalSummaryHandshakeBinding
import com.sisindia.ai.android.features.issues.complaints.CreateComplaintIssueActivity

/**
 * Created by Ashu Rajput on 4/10/2020.
 */
class SummaryOfHandshakeFragment : IopsBaseFragment() {

    private lateinit var viewModel: SummaryHandshakeViewModel
    private lateinit var binding: FragmentFinalSummaryHandshakeBinding

    companion object {
        fun newInstance(optedQuestions: String): SummaryOfHandshakeFragment =
            SummaryOfHandshakeFragment().apply {
                arguments = Bundle().apply {
                    putString(IntentConstants.OPTED_QUESTIONS, optedQuestions)
                }
            }
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_final_summary_handshake
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(SummaryHandshakeViewModel::class.java)
                as SummaryHandshakeViewModel
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater!!, layoutResource, container, false)
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun extractBundle() {
        viewModel.questionsList.set(requireArguments().getString(IntentConstants.OPTED_QUESTIONS))
    }

    override fun initViewState() {
        liveData.observe(this,
            Observer { message: Message ->
                when (message.what) {
                    NavigationConstants.ON_ADDING_CLIENT_HANDSHAKE_COMPLAINT -> openAddComplaint()
                }
            })
    }

    override fun onCreated() {
        viewModel.fetchClientDetails()
        viewModel.fetchAddedComplaintsFromDB()
    }

    private fun openAddComplaint() {
        startActivityForResult(CreateComplaintIssueActivity.newIntent(requireActivity()),
            IntentRequestCodes.REQUEST_CODE_CREATE_COMPLAINT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IntentRequestCodes.REQUEST_CODE_CREATE_COMPLAINT && resultCode == Activity.RESULT_OK)
            viewModel.fetchAddedComplaintsFromDB()
    }
}