package com.sisindia.ai.android.features.civil

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentCivilDefenceBinding

/**
 * Created by Ashu Rajput on 06/03/2025.
 */
class CivilDefenceFragment : IopsBaseFragment() {

    private lateinit var viewModel: CivilDefenceViewModel
    private lateinit var binding: FragmentCivilDefenceBinding

    companion object {
        fun newInstance(): CivilDefenceFragment = CivilDefenceFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_civil_defence
    }

    override fun initViewModel() {
        viewModel =
            getAndroidViewModel(CivilDefenceViewModel::class.java) as CivilDefenceViewModel
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {
                NavigationConstants.ON_ADD_CIVIL_NOMINATION -> openAddNominationBottomSheet()
            }
        }
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater!!, layoutResource, container, false)
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun onCreated() {
        viewModel.initDashboardUi()
    }

    private fun openAddNominationBottomSheet() {
        if (requireActivity().supportFragmentManager.findFragmentByTag(
                AddCivilNominationBottomSheet::class.java.simpleName) == null) {
            AddCivilNominationBottomSheet.newInstance()
                .show(requireActivity().supportFragmentManager,
                    AddCivilNominationBottomSheet::class.java.simpleName)
        }
    }
}