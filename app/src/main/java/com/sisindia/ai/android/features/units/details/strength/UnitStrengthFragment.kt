package com.sisindia.ai.android.features.units.details.strength

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.databinding.FragmentUnitStrengthBinding

/**
 * Created by Ashu Rajput on 3/24/2020.
 */
class UnitStrengthFragment : IopsBaseFragment() {

    private var viewModel: UnitStrengthViewModel? = null

    companion object {
        fun newInstance() = UnitStrengthFragment()
    }

    override fun onCreated() {
        viewModel!!.fetchUnitStrengthFromDB()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_unit_strength
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(UnitStrengthViewModel::class.java) as UnitStrengthViewModel
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        val binding: FragmentUnitStrengthBinding = DataBindingUtil.inflate(inflater!!,
            layoutResource, container, false)
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }
}