package com.sisindia.ai.android.features.performance.frags

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.ViewpagerFragmentPerformanceEffortsBinding

/**
 * Created by Ashu Rajput on 5/8/2020.
 */
class PerformanceEffortsFragment : IopsBaseFragment() {

    private lateinit var viewModel: PerformanceEffortsViewModel
    private lateinit var binding: ViewpagerFragmentPerformanceEffortsBinding

    companion object {
        fun newInstance(): PerformanceEffortsFragment = PerformanceEffortsFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.viewpager_fragment_performance_efforts
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(PerformanceEffortsViewModel::class.java)
                as PerformanceEffortsViewModel
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this,
            Observer { message: Message ->
                when (message.what) {
                    NavigationConstants.ON_REFRESHING_RECRUIT_ADDED -> {
                    }
                }
            })
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater!!, layoutResource, container,
            false) as ViewpagerFragmentPerformanceEffortsBinding
        binding.vm = viewModel
        binding.isEffortsCall = true
        binding.executePendingBindings()
        return binding.root
    }

    override fun onCreated() {
    }
}