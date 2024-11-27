package com.sisindia.ai.android.features.poa

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.droidcommons.base.viewpager.CustomViewPagerAdapter
import com.droidcommons.base.viewpager.ViewPagerModel
import com.google.android.material.tabs.TabLayout
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentPlanOfActionsBinding
import com.sisindia.ai.android.features.improvementplans.ImprovementPlansFragment
import com.sisindia.ai.android.features.issues.IssueManagementViewModel
import com.sisindia.ai.android.features.uar.UnitAtRiskFragment
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ashu Rajput on 12/15/2020.
 */
class PlansOfActionFragment : IopsBaseFragment() {

    private lateinit var viewModel: IssueManagementViewModel
    private lateinit var binding: FragmentPlanOfActionsBinding

    @Inject
    @Named("PlanOfActions")
    lateinit var viewPagers: ArrayList<ViewPagerModel>

    companion object {
        fun newInstance(): PlansOfActionFragment = PlansOfActionFragment()
    }

    override fun getLayoutResource(): Int = R.layout.fragment_plan_of_actions

    override fun extractBundle() {

    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(IssueManagementViewModel::class.java) as IssueManagementViewModel
    }

    override fun initViewBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binding = bindFragmentView(layoutResource, inflater, container) as FragmentPlanOfActionsBinding
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun initViewState() {
        liveData.observe(this, Observer { message: Message ->
            when (message.what) {
                NavigationConstants.ON_IMPROVEMENT_POA_CLICK -> getFragmentFromViewPager(1)
                NavigationConstants.ON_UNITS_RISK_POA_CLICK -> getFragmentFromViewPager(0)
            }
        })
    }

    override fun onCreated() {
        viewModel.issueType.set(IssueManagementViewModel.IssueType.UAR)
        setUpTabsAndViewPager()
    }

    private fun setUpTabsAndViewPager() {
        val viewPagerAdapter = CustomViewPagerAdapter(childFragmentManager, viewPagers)
        binding.vpPlanOfActions.adapter = viewPagerAdapter

        binding.vpPlanOfActions.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float,
                positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> viewModel.issueType.set(IssueManagementViewModel.IssueType.UAR)
                    1 -> viewModel.issueType.set(IssueManagementViewModel.IssueType.IMPROVEMENT_PLAN)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        binding.tlPlanOfActions.setupWithViewPager(binding.vpPlanOfActions)

        binding.tlPlanOfActions.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.vpPlanOfActions.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun getFragmentFromViewPager(fragmentIndex: Int) {
        val selectedFragment = childFragmentManager.fragments[fragmentIndex]
        if (selectedFragment is ImprovementPlansFragment)
            selectedFragment.refreshImprovementPlanUI()
        else if (selectedFragment is UnitAtRiskFragment)
            selectedFragment.refreshUarUI()
    }
}