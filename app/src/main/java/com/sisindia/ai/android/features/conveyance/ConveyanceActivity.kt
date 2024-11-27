package com.sisindia.ai.android.features.conveyance

import android.app.Activity
import android.content.Intent
import android.os.Message
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.ActivityConveyanceBinding
import com.sisindia.ai.android.utils.loadExtFragment
import com.sisindia.ai.android.utils.unloadFragment

/**
 * Created by Ashu Rajput on 12/30/2020.
 */
class ConveyanceActivity : IopsBaseActivity() {

    private lateinit var binding: ActivityConveyanceBinding
    private var viewModel: ConveyanceViewModel? = null

    companion object {
        @JvmStatic
        fun newIntent(activity: Activity): Intent {
            return Intent(activity, ConveyanceActivity::class.java)
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_conveyance
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(ConveyanceViewModel::class.java) as ConveyanceViewModel
    }

    override fun initViewBinding() {
        binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this,
            Observer { message: Message ->
                when (message.what) {
                    NavigationConstants.ON_CONVEYANCE_DATE_SELECTED ->
                        openConveyanceViaDateFragment(message.obj as String)
                }
            })
    }

    override fun onCreated() {
        setupToolBarForBackArrow(binding.tbConveyance)
        loadExtFragment(R.id.flConveyance, ConveyanceMonthlyFragment.newInstance(), false, 0)
    }

    private fun openConveyanceViaDateFragment(date: String) {
        loadExtFragment(R.id.flConveyance, ConveyanceFragment.newInstance(date), true, 0)
    }

    override fun onBackPressed() {
        if (!unloadFragment())
            super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        if (!unloadFragment())
            super.onBackPressed()
        return super.onSupportNavigateUp()
    }
}