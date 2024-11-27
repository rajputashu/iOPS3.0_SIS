package com.sisindia.ai.android.features.barracks.inspection.frags

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.databinding.FragmentBarrackStrengthBinding

/**
 * Created by Ashu Rajput on 4/20/2020.
 */
class BarrackStrengthFragment : IopsBaseFragment() {

    private lateinit var viewModel: BarrackStrengthViewModel
    private lateinit var binding: FragmentBarrackStrengthBinding

    companion object {
        fun newInstance(): BarrackStrengthFragment = BarrackStrengthFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_barrack_strength
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(BarrackStrengthViewModel::class.java)
                as BarrackStrengthViewModel
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
        viewModel.apply {
            fetchCacheBI()
        }
    }
}