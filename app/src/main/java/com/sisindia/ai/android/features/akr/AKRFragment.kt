package com.sisindia.ai.android.features.akr

import android.app.Activity
import android.content.Intent
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.constants.IntentRequestCodes
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentAkrBinding
import com.sisindia.ai.android.features.akr.details.AKRDetailsActivity

/**
 * Created by Ashu Rajput on 4/16/2020.
 */
class AKRFragment : IopsBaseFragment() {

    private lateinit var viewModel: AKRViewModel
    private lateinit var binding: FragmentAkrBinding

    companion object {
        fun newInstance(): AKRFragment = AKRFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_akr
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(AKRViewModel::class.java) as AKRViewModel
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater!!, layoutResource, container, false)
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {
                NavigationConstants.OPEN_AKR_SELECTED -> openAssignedDistributedAKR()
            }
        }
    }

    override fun onCreated() {
        viewModel.initOrUpdateAKRScreenUI()
    }

    private fun openAssignedDistributedAKR() {
        startActivityForResult(Intent(activity, AKRDetailsActivity::class.java),
            IntentRequestCodes.REQUEST_OPEN_AKR_DETAILS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IntentRequestCodes.REQUEST_OPEN_AKR_DETAILS && resultCode == Activity.RESULT_OK)
            viewModel.initOrUpdateAKRScreenUI()
    }
}