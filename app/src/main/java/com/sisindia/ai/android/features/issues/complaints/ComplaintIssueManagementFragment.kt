package com.sisindia.ai.android.features.issues.complaints

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_CREATE_COMPLAINT
import com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_UPDATE_COMPLAINT
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentComplaintIssueManagementBinding

class ComplaintIssueManagementFragment : IopsBaseFragment() {

    lateinit var viewModel: ComplaintIssueManagementViewModel

    private lateinit var binding: FragmentComplaintIssueManagementBinding

    companion object {
        fun newInstance(): ComplaintIssueManagementFragment = ComplaintIssueManagementFragment()
    }

    override fun extractBundle() {

    }

    override fun initViewModel() {
        viewModel =
            getAndroidViewModel(ComplaintIssueManagementViewModel::class.java) as ComplaintIssueManagementViewModel
    }

    override fun initViewBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binding =
            bindFragmentView(layoutResource,
                inflater,
                container) as FragmentComplaintIssueManagementBinding
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun initViewState() {
        liveData.observe(this, Observer {
            when (it.what) {
                NavigationConstants.ON_CREATE_COMPLAINT -> {
                    openCreateComplaintScreen()
                }

                NavigationConstants.ON_EDIT_COMPLAINT -> {
                    openEditComplaintScreen(it.arg1)
                }
//
//                NavigationConstants.ON_VIEW_GRIEVANCE -> {
//                    openViewGrievanceScreen()
//                }
            }
        })
    }

    private fun openEditComplaintScreen(complaintId: Int) {
        startActivityForResult(IssueComplaintDetailActivity.newIntent(requireActivity(),
            complaintId), REQUEST_CODE_UPDATE_COMPLAINT)
    }


    private fun openCreateComplaintScreen() {
        startActivityForResult(CreateComplaintIssueActivity.newIntent(requireActivity()),
            REQUEST_CODE_CREATE_COMPLAINT)
    }

    override fun onCreated() {
        viewModel.initViewModel()

        binding.swipeLayout.setOnRefreshListener {
            viewModel.initViewModel()
            binding.swipeLayout.isRefreshing = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_UPDATE_COMPLAINT, REQUEST_CODE_CREATE_COMPLAINT -> {
                if (resultCode == Activity.RESULT_OK) {
                    viewModel.initViewModel()
                }
            }
        }
    }

    override fun getLayoutResource(): Int = R.layout.fragment_complaint_issue_management
}