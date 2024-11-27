package com.sisindia.ai.android.features.mask

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentMaskDistributionBinding
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class MaskDistributionFragment : IopsBaseFragment() {

    private lateinit var viewModel: MaskDistributionViewModel
    private lateinit var binding: FragmentMaskDistributionBinding

    companion object {
        fun newInstance(): MaskDistributionFragment = MaskDistributionFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_mask_distribution
    }

    override fun initViewModel() {
        viewModel =
            getAndroidViewModel(MaskDistributionViewModel::class.java) as MaskDistributionViewModel
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this,
            Observer { message: Message ->
                when (message.what) {
                    NavigationConstants.OPEN_ADD_MASK_REQUEST -> openAddMaskDistributionBottomSheet()
                    NavigationConstants.ON_REFRESHING_MASK_ADDED -> {
                        viewModel.isLoading.set(View.VISIBLE) //{Showing loader before calling API}
                        CoroutineScope(Dispatchers.IO).launch {
                            delay(TimeUnit.SECONDS.toMillis(2))
                            withContext(Dispatchers.Main) {
                                viewModel.getAddedMaskDetailsFromAPI()
                            }
                        }
                    }
                }
            })
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater!!, layoutResource, container, false)
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun onCreated() {
        viewModel.getAddedMaskDetailsFromAPI()
    }

    private fun openAddMaskDistributionBottomSheet() {
        if (requireActivity().supportFragmentManager.findFragmentByTag(AddMaskBottomSheet::class.java.simpleName) == null) {
            val sheet = AddMaskBottomSheet.newInstance()
            sheet.show(requireActivity().supportFragmentManager,
                AddMaskBottomSheet::class.java.simpleName)
            sheet.isCancelable = false
        }
    }
}