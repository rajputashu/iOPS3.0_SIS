package com.sisindia.ai.android.features.issues.grievance

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_CREATE_GRIEVANCE
import com.sisindia.ai.android.constants.IntentRequestCodes.REQUEST_CODE_UPDATE_GRIEVANCE
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentGrievanceIssueManagementBinding


class GrievanceIssueManagementFragment : IopsBaseFragment() {

    lateinit var viewModel: GrievanceIssueManagementViewModel

    private lateinit var binding: FragmentGrievanceIssueManagementBinding


    companion object {
        fun newInstance(): GrievanceIssueManagementFragment = GrievanceIssueManagementFragment()
    }

    override fun initViewModel() {
        viewModel =
            getAndroidViewModel(GrievanceIssueManagementViewModel::class.java) as GrievanceIssueManagementViewModel
    }

    override fun extractBundle() {

    }

    override fun initViewState() {
        liveData.observe(this, Observer {
            when (it.what) {
                NavigationConstants.ON_CREATE_GRIEVANCE -> {
                    openCreateGrievanceScreen()
                }

                NavigationConstants.ON_EDIT_GRIEVANCE -> {
                    openEditGrievanceScreen(it.arg1)
                }

                NavigationConstants.ON_VIEW_GRIEVANCE -> {
                    openViewGrievanceScreen()
                }
            }
        })
    }

    private fun openViewGrievanceScreen() {

    }

    private fun openEditGrievanceScreen(grievanceId: Int) {
        startActivityForResult(IssueGrievanceDetailActivity.newIntent(requireActivity(),
            grievanceId), REQUEST_CODE_UPDATE_GRIEVANCE)
    }

    private fun openCreateGrievanceScreen() {
        startActivityForResult(CreateGrievanceIssueActivity.newIntent(requireActivity()),
            REQUEST_CODE_CREATE_GRIEVANCE)
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding =
            bindFragmentView(layoutResource,
                inflater,
                container) as FragmentGrievanceIssueManagementBinding
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
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
            REQUEST_CODE_UPDATE_GRIEVANCE, REQUEST_CODE_CREATE_GRIEVANCE -> {
                if (resultCode == Activity.RESULT_OK) {
                    viewModel.initViewModel()
                }
            }
        }
    }

    override fun getLayoutResource(): Int = R.layout.fragment_grievance_issue_management

}