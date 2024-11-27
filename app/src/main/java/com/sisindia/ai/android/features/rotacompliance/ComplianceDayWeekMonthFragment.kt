package com.sisindia.ai.android.features.rotacompliance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.databinding.FragmentDayWeeklyMonthlyBinding
import com.sisindia.ai.android.features.rotacompliance.adapters.RCDayWeekMonthAdapter
import timber.log.Timber

/**
 * Created by Ashu Rajput on 3/6/2020.
 */
class ComplianceDayWeekMonthFragment : IopsBaseFragment() {

    private var viewModel: ComplianceDWMViewModel? = null

    companion object {
        private const val TAB_TYPE = "ViewPagerTabType"
        fun newInstance(tabType: Int) = ComplianceDayWeekMonthFragment().apply {
            Timber.e("Set TabValue Id $tabType")
            arguments = Bundle().apply { putInt(TAB_TYPE, tabType) }
        }
    }

    override fun onCreated() {
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_day_weekly_monthly
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(ComplianceDWMViewModel::class.java)
                as ComplianceDWMViewModel
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        val binding: FragmentDayWeeklyMonthlyBinding = DataBindingUtil.inflate(inflater!!,
            layoutResource, container, false)
        val vpType = arguments.let {
            it!!.getInt(TAB_TYPE)
        }
        binding.rvDayWeekMonthly.layoutManager = LinearLayoutManager(requireActivity(),
            RecyclerView.HORIZONTAL, false)
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(binding.rvDayWeekMonthly)

//        binding.rvDayWeekMonthly.adapter = viewModel!!.adapter
        binding.rvDayWeekMonthly.adapter = RCDayWeekMonthAdapter(requireActivity(),
            viewModel!!.getData(vpType))
        return binding.root
    }
}