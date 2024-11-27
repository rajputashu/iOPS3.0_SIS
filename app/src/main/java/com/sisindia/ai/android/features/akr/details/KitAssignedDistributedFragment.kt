package com.sisindia.ai.android.features.akr.details

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentKitAssignedDistributedBinding

/**
 * Created by Ashu Rajput on 4/16/2020.
 */
class KitAssignedDistributedFragment : IopsBaseFragment() {

    private lateinit var viewModel: KitAssignedDistributedViewModel
    private lateinit var binding: FragmentKitAssignedDistributedBinding

    companion object {
        fun newInstance(): KitAssignedDistributedFragment = KitAssignedDistributedFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_kit_assigned_distributed
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(KitAssignedDistributedViewModel::class.java)
                as KitAssignedDistributedViewModel
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
                NavigationConstants.ON_RESUME_KIT_REPLACE -> viewModel.initAndUpdateKitAssignedUI()
            }
        }
    }

    override fun onCreated() {
        viewModel.initAndUpdateKitAssignedUI()
    }
}