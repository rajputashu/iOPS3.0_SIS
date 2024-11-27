package com.sisindia.ai.android.features.units.details

import androidx.viewpager.widget.ViewPager
import com.droidcommons.base.viewpager.CustomViewPagerAdapter
import com.droidcommons.base.viewpager.ViewPagerModel
import com.google.android.material.tabs.TabLayout
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.databinding.ActivityUnitDetailsBinding
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ashu Rajput on 3/20/2020.
 */
class UnitDetailActivity : IopsBaseActivity() {

    private var viewModel: UnitDetailViewModel? = null
    private lateinit var binding: ActivityUnitDetailsBinding

    @Inject
    @Named("UnitDetailsViewPager")
    lateinit var viewPagers: ArrayList<ViewPagerModel>

    override fun getLayoutResource(): Int {
        return R.layout.activity_unit_details
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbUnitDetails)
        setUpTabsAndViewPager()
    }

    override fun initViewBinding() {
        binding = bindActivityView(this, layoutResource) as ActivityUnitDetailsBinding
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    private fun setUpTabsAndViewPager() {
        val viewPagerAdapter = CustomViewPagerAdapter(supportFragmentManager, viewPagers)
        binding.vpUnitDetails.adapter = viewPagerAdapter
        binding.vpUnitDetails.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float,
                positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        binding.tlUnitDetails.setupWithViewPager(binding.vpUnitDetails)
        binding.tlUnitDetails.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.vpUnitDetails.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        /*liveData.observe(this,
            Observer { message: Message ->
            })*/
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(UnitDetailViewModel::class.java) as UnitDetailViewModel
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}