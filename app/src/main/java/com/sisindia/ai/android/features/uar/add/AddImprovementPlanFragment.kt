package com.sisindia.ai.android.features.uar.add

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.databinding.FragmentAddImprovePlanBinding

/**
 * Created by Ashu Rajput on 4/3/2020.
 */
class AddImprovementPlanFragment : IopsBaseFragment() {

    private lateinit var viewModel: AddPoaAndIpViewModel
    private lateinit var binding: FragmentAddImprovePlanBinding

    companion object {
        fun newInstance(): AddImprovementPlanFragment = AddImprovementPlanFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_add_improve_plan
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(AddPoaAndIpViewModel::class.java) as AddPoaAndIpViewModel
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

    }

    override fun onCreated() {
        viewModel.initCreateImprovementPlanUI()
    }
}