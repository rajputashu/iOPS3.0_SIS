package com.sisindia.ai.android.features.performance.frags

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.databinding.FragmentIncentiveBinding

/**
 * Created by Ashu Rajput on 1/14/2021.
 */
class IncentiveFragment : IopsBaseFragment() {

    private lateinit var viewModel: SiteTaskSummaryViewModel

    companion object {
        fun newInstance(): IncentiveFragment = IncentiveFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_incentive
    }

    override fun initViewModel() {
        viewModel =
            getAndroidViewModel(SiteTaskSummaryViewModel::class.java) as SiteTaskSummaryViewModel
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        val binding = DataBindingUtil.inflate(inflater!!, layoutResource, container,
            false) as FragmentIncentiveBinding
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
    }

    override fun onCreated() {
        viewModel.obsIsIncentiveCall.set(true)
//        viewModel.getIncentiveData()
    }
}