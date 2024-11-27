package com.sisindia.ai.android.features.akr.details

import android.app.Activity
import android.os.Message
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseActivity
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.ActivityAkrDetailsBinding
import com.sisindia.ai.android.utils.loadExtFragment
import com.sisindia.ai.android.utils.unloadFragment

/**
 * Created by Ashu Rajput on 4/16/2020.
 */
class AKRDetailsActivity : IopsBaseActivity() {

    override fun getLayoutResource(): Int {
        return R.layout.activity_akr_details
    }

    override fun initViewModel() {
    }

    override fun initViewBinding() {
        val binding: ActivityAkrDetailsBinding = DataBindingUtil.setContentView(this, layoutResource)
        binding.executePendingBindings()
        setupToolBarForBackArrow(binding.tbKitAssignedDistributed)
    }

    override fun onCreated() {
        loadExtFragment(R.id.flAkrDetails, KitAssignedDistributedFragment.newInstance(), false, 0)
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this,
            Observer { message: Message ->
                when (message.what) {
                    NavigationConstants.OPEN_KIT_ASSIGNED_SELECTED -> openReplaceKit()
                }
            })
    }

    private fun openReplaceKit() {
        loadExtFragment(R.id.flAkrDetails, KitReplaceFragment.newInstance(), true, 0)
    }

    override fun onBackPressed() {
        if (!unloadFragment()) {
            setResult(Activity.RESULT_OK)
            finish()
            //            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (!unloadFragment()) {
            setResult(Activity.RESULT_OK)
            finish()
        }
        return super.onSupportNavigateUp()
    }
}