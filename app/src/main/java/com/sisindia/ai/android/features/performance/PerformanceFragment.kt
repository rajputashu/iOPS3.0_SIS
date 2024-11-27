package com.sisindia.ai.android.features.performance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.droidcommons.base.viewpager.CustomViewPagerAdapter
import com.droidcommons.base.viewpager.ViewPagerModel
import com.google.android.material.tabs.TabLayout
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.databinding.FragmentPerformanceBinding
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ashu Rajput on 5/7/2020.
 */
class PerformanceFragment : IopsBaseFragment() {

    private lateinit var viewModel: PerformanceViewModel
    private lateinit var binding: FragmentPerformanceBinding

    @Inject
    @Named("PerformanceViewPager")
    lateinit var viewPagers: ArrayList<ViewPagerModel>

    companion object {
        fun newInstance(): PerformanceFragment = PerformanceFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_performance
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(PerformanceViewModel::class.java) as PerformanceViewModel
    }

    override fun extractBundle() {
    }

    override fun initViewState() {

    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater!!, layoutResource, container,
            false) as FragmentPerformanceBinding
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun onCreated() {
        setUpTabsAndViewPager()
    }

    private fun setUpTabsAndViewPager() {
        val viewPagerAdapter = CustomViewPagerAdapter(requireActivity().supportFragmentManager, viewPagers)
        binding.vpPerformance.adapter = viewPagerAdapter
        binding.vpPerformance.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float,
                positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                Timber.e("")
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        binding.tlPerformance.setupWithViewPager(binding.vpPerformance)
        binding.tlPerformance.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.vpPerformance.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}