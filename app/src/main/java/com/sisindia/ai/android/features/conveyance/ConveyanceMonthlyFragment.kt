package com.sisindia.ai.android.features.conveyance

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.databinding.FragmentConveyanceMonthlyBinding

/**
 * Created by Ashu Rajput on 12/30/2020.
 */
class ConveyanceMonthlyFragment : IopsBaseFragment() {

    private lateinit var viewModel: ConveyanceViewModel
    private lateinit var binding: FragmentConveyanceMonthlyBinding

    companion object {
        fun newInstance(): ConveyanceMonthlyFragment = ConveyanceMonthlyFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_conveyance_monthly
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(ConveyanceViewModel::class.java) as ConveyanceViewModel
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
        liveData.observe(this,
            Observer { message: Message ->
                when (message.what) {

                }
            })
    }

    override fun onCreated() {
        viewModel.apply {
            //            fetchMonthlyConveyanceDetails(false)
            binding.rbCurrentMonthConveyance.isChecked = true
        }
    }
}