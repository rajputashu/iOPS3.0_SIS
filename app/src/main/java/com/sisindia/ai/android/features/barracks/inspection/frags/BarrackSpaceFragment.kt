package com.sisindia.ai.android.features.barracks.inspection.frags

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.databinding.FragmentBarrackSpaceBinding

/**
 * Created by Ashu Rajput on 4/21/2020.
 */
class BarrackSpaceFragment : IopsBaseFragment() {

    private lateinit var viewModel: BarrackSpaceViewModel
    private lateinit var binding: FragmentBarrackSpaceBinding

    companion object {
        fun newInstance(): BarrackSpaceFragment = BarrackSpaceFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_barrack_space
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(BarrackSpaceViewModel::class.java) as BarrackSpaceViewModel
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