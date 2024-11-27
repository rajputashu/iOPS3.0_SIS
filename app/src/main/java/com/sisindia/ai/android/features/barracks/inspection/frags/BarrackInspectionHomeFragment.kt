package com.sisindia.ai.android.features.barracks.inspection.frags

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentBarrackInspectionBinding
import com.sisindia.ai.android.uimodels.NavigationUIModel.NavigationUiViewType.*

/**
 * Created by Ashu Rajput on 4/20/2020.
 */
class BarrackInspectionHomeFragment : IopsBaseFragment() {

    private lateinit var viewModel: BarrackInspectionHomeViewModel
    private lateinit var binding: FragmentBarrackInspectionBinding

    companion object {
        fun newInstance(): BarrackInspectionHomeFragment = BarrackInspectionHomeFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_barrack_inspection
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(BarrackInspectionHomeViewModel::class.java)
                as BarrackInspectionHomeViewModel
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
        var isAllActionsCompleted = false
        liveData.observe(this, { message: Message ->
            when (message.what) {
                NavigationConstants.ON_COMPLETE_BARRACK_INSPECTION_STRENGTH -> {
                    isAllActionsCompleted =
                        viewModel.isAllBarrackTaskCompleted(BARRACK_INSPECTION_STRENGTH.navigationViewType)
                    requireActivity().onBackPressed()
                }
                NavigationConstants.ON_COMPLETE_BARRACK_INSPECTION_SPACE -> {
                    isAllActionsCompleted =
                        viewModel.isAllBarrackTaskCompleted(BARRACK_INSPECTION_SPACE.navigationViewType)
                    requireActivity().onBackPressed()
                }
                NavigationConstants.ON_COMPLETE_BARRACK_INSPECTION_OTHERS -> {
                    isAllActionsCompleted =
                        viewModel.isAllBarrackTaskCompleted(BARRACK_INSPECTION_OTHERS.navigationViewType)
                    requireActivity().onBackPressed()
                }
                NavigationConstants.ON_COMPLETE_BARRACK_INSPECTION_LANDLORD -> {
                    isAllActionsCompleted =
                        viewModel.isAllBarrackTaskCompleted(BARRACK_INSPECTION_LANDLORD.navigationViewType)
                    requireActivity().onBackPressed()
                }
            }
            if (isAllActionsCompleted)
                viewModel.isBarrackInspectionCompleted.set(true)
        })
    }

    override fun onCreated() {
        viewModel.apply {
            updateBarrackCommonDetails()
            updateBarrackActionList()
            checkTaskAlreadyCached()
        }
    }
}