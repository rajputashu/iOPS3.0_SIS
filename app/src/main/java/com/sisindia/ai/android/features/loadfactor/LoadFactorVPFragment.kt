package com.sisindia.ai.android.features.loadfactor

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
import com.sisindia.ai.android.features.loadfactor.adapter.LoadFactorDWMAdapter

/**
 * Created by Ashu Rajput on 3/11/2020.
 */
class LoadFactorVPFragment : IopsBaseFragment() {

    private var viewModel: LoadFactorDWMViewModel? = null

    companion object {
        private const val TAB_TYPE = "ViewPagerTabType"
        fun newInstance(tabType: Int) = LoadFactorVPFragment().apply {
            arguments = Bundle().apply { putInt(TAB_TYPE, tabType) }
        }
    }

    override fun onCreated() {
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_day_weekly_monthly
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(LoadFactorDWMViewModel::class.java)
                as LoadFactorDWMViewModel
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
        binding.rvDayWeekMonthly.adapter = LoadFactorDWMAdapter(requireActivity(),
            viewModel!!.getData(vpType))
        return binding.root
    }
}