package com.sisindia.ai.android.features.performance.frags

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.databinding.PerformanceSiteSummaryBinding

/**
 * Created by Ashu Rajput on 11/17/2020.
 */
class SiteTaskSummaryFragment : IopsBaseFragment() {

    private lateinit var viewModel: SiteTaskSummaryViewModel

    companion object {
        fun newInstance(): SiteTaskSummaryFragment = SiteTaskSummaryFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.performance_site_summary
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(SiteTaskSummaryViewModel::class.java) as SiteTaskSummaryViewModel
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        val binding = DataBindingUtil.inflate(inflater!!, layoutResource, container,
            false) as PerformanceSiteSummaryBinding
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
    }

    override fun onCreated() {
//        viewModel.getSiteTaskSummaryData()
    }
}