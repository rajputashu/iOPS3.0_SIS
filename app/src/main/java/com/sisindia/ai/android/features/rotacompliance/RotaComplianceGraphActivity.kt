package com.sisindia.ai.android.features.rotacompliance

import android.os.Message
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.droidcommons.base.viewpager.CustomViewPagerAdapter
import com.droidcommons.base.viewpager.ViewPagerModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.databinding.ActivityRotaComplianceBinding
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ashu Rajput on 3/5/2020.
 */
class RotaComplianceGraphActivity : IopsBaseActivity() {

    private var viewModel: RotaComplianceViewModel? = null
    private lateinit var binding: ActivityRotaComplianceBinding

    @Inject
    @field:Named("RotaComplianceViewPager")
    lateinit var viewPagers: ArrayList<ViewPagerModel>

    override fun getLayoutResource(): Int {
        return R.layout.activity_rota_compliance
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbRotaCompliance)
        setUpTabsAndViewPager()
    }

    override fun initViewBinding() {
        binding = bindActivityView(this, layoutResource) as ActivityRotaComplianceBinding
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    private fun setUpTabsAndViewPager() {
        val viewPagerAdapter = CustomViewPagerAdapter(supportFragmentManager, viewPagers)
        binding.vpRotaCompliance.adapter = viewPagerAdapter
        binding.vpRotaCompliance.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float,
                positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                Timber.e("")
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        binding.tlRotaCompliance.setupWithViewPager(binding.vpRotaCompliance)
        binding.tlRotaCompliance.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.vpRotaCompliance.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this,
            Observer { message: Message ->

            })
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(RotaComplianceViewModel::class.java)
                as RotaComplianceViewModel
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}