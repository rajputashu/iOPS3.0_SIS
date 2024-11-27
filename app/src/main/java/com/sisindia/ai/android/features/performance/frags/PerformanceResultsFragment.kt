package com.sisindia.ai.android.features.performance.frags

import android.content.Intent
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.ViewpagerFragmentPerformanceResultsBinding
import com.sisindia.ai.android.features.rotacompliance.RotaComplianceActivity

/**
 * Created by Ashu Rajput on 5/8/2020.
 */
class PerformanceResultsFragment : IopsBaseFragment() {

    private lateinit var viewModel: PerformanceResultsViewModel
    private lateinit var binding: ViewpagerFragmentPerformanceResultsBinding

    companion object {
        fun newInstance(): PerformanceResultsFragment = PerformanceResultsFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.viewpager_fragment_performance_results
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(PerformanceResultsViewModel::class.java)
                as PerformanceResultsViewModel
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this,
            Observer { message: Message ->
                when (message.what) {
                    NavigationConstants.OPEN_ROTA_COMPLIANCE_CARDS_ACTIVITY ->
                        startActivity(Intent(activity, RotaComplianceActivity::class.java))
                    /*NavigationConstants.OPEN_LOAD_FACTOR_ACTIVITY ->
                        startActivity(Intent(activity, LoadFactorActivity::class.java))*/
                }
            })
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater!!, layoutResource, container,
            false) as ViewpagerFragmentPerformanceResultsBinding
        binding.vm = viewModel
        binding.isEffortsCall = false
        binding.executePendingBindings()
        return binding.root
    }

    override fun onCreated() {

    }
}