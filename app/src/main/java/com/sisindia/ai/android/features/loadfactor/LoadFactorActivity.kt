package com.sisindia.ai.android.features.loadfactor

import android.os.Message
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.droidcommons.base.viewpager.CustomViewPagerAdapter
import com.droidcommons.base.viewpager.ViewPagerModel
import com.google.android.material.tabs.TabLayout
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.databinding.ActivityLoadFactorBinding
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ashu Rajput on 3/11/2020.
 */
class LoadFactorActivity : IopsBaseActivity() {

    lateinit var binding: ActivityLoadFactorBinding
    private var viewModel: LoadFactorViewModel? = null

    @Inject
    @field:Named("LoadFactorViewPager")
    lateinit var viewPagers: ArrayList<ViewPagerModel>

    override fun getLayoutResource(): Int {
        return R.layout.activity_load_factor
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(LoadFactorViewModel::class.java)
                as LoadFactorViewModel
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbLoadFactor)
        setUpTabsAndViewPager()
    }

    override fun initViewBinding() {
        binding = bindActivityView(this, layoutResource) as ActivityLoadFactorBinding
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this,
            Observer { message: Message ->
            })
    }

    private fun setUpTabsAndViewPager() {
        val viewPagerAdapter = CustomViewPagerAdapter(supportFragmentManager, viewPagers)
        binding.vpLoadFactor.adapter = viewPagerAdapter
        binding.vpLoadFactor.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float,
                positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                Timber.e("")
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        binding.tlLoadFactor.setupWithViewPager(binding.vpLoadFactor)
        binding.tlLoadFactor.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.vpLoadFactor.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}