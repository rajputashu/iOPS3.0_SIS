package com.sisindia.ai.android.features.issues

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.droidcommons.base.viewpager.CustomViewPagerAdapter
import com.droidcommons.base.viewpager.ViewPagerModel
import com.google.android.material.tabs.TabLayout
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.databinding.FragmentIssueManagementBinding
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class IssueManagementFragment : IopsBaseFragment() {

    private lateinit var viewModel: IssueManagementViewModel
    private lateinit var binding: FragmentIssueManagementBinding

    @Inject
    @Named("IssueManagement")
    lateinit var viewPagers: ArrayList<ViewPagerModel>

    companion object {
        fun newInstance(): IssueManagementFragment = IssueManagementFragment()
    }

    override fun extractBundle() {

    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(IssueManagementViewModel::class.java) as IssueManagementViewModel
    }

    override fun initViewBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binding = bindFragmentView(layoutResource, inflater, container) as FragmentIssueManagementBinding
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun initViewState() {

    }

    override fun onCreated() {
        setUpTabsAndViewPager()
    }

    private fun setUpTabsAndViewPager() {
        val viewPagerAdapter = CustomViewPagerAdapter(childFragmentManager, viewPagers)
        binding.vpIssues.adapter = viewPagerAdapter

        binding.vpIssues.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float,
                positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> viewModel.issueType.set(IssueManagementViewModel.IssueType.GRIEVANCE)
                    1 -> viewModel.issueType.set(IssueManagementViewModel.IssueType.COMPLAINT)
                    2 -> viewModel.issueType.set(IssueManagementViewModel.IssueType.IMPROVEMENT_PLAN)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        binding.tlIssues.setupWithViewPager(binding.vpIssues)

        binding.tlIssues.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.vpIssues.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun getLayoutResource(): Int = R.layout.fragment_issue_management
}