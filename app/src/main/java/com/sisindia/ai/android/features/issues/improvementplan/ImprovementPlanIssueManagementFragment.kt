package com.sisindia.ai.android.features.issues.improvementplan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.databinding.FragmentImprovementPlanIssueManagementBinding

class ImprovementPlanIssueManagementFragment : IopsBaseFragment() {

    lateinit var viewModel: ImprovementPlanIssueManagementViewModel

    private lateinit var binding: FragmentImprovementPlanIssueManagementBinding

    companion object {
        fun newInstance(): ImprovementPlanIssueManagementFragment =
            ImprovementPlanIssueManagementFragment()
    }

    override fun extractBundle() {

    }

    override fun initViewModel() {
        viewModel =
            getAndroidViewModel(ImprovementPlanIssueManagementViewModel::class.java) as ImprovementPlanIssueManagementViewModel
    }

    override fun initViewBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binding =
            bindFragmentView(layoutResource,
                inflater,
                container) as FragmentImprovementPlanIssueManagementBinding
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun initViewState() {

    }

    override fun onCreated() {

    }

    override fun getLayoutResource(): Int = R.layout.fragment_improvement_plan_issue_management
}