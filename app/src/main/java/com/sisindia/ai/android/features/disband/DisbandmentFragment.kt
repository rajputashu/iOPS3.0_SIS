package com.sisindia.ai.android.features.disband

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentDisbandmentBinding
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

/**
 * Created by Ashu Rajput on 30-03-2023
 */
class DisbandmentFragment : IopsBaseFragment() {

    private lateinit var viewModel: DisbandmentViewModel
    private lateinit var binding: FragmentDisbandmentBinding

    companion object {
        fun newInstance(): DisbandmentFragment = DisbandmentFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_disbandment
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(DisbandmentViewModel::class.java) as DisbandmentViewModel
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {
                NavigationConstants.OPEN_ADD_RECRUITMENT_SHEET -> openAddDisbandmentBottomSheet()
                NavigationConstants.ON_REFRESHING_DISBAND_DASHBOARD -> {
                    viewModel.isLoading.set(View.VISIBLE) //{Showing loader before calling API}
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(TimeUnit.SECONDS.toMillis(2))
                        withContext(Dispatchers.Main) {
                            viewModel.getDisbandSitesFromAPI()
                        }
                    }
                }
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
        viewModel.getDisbandSitesFromAPI()
        binding.swipeLayout.setOnRefreshListener {
            viewModel.getDisbandSitesFromAPI()
            binding.swipeLayout.isRefreshing = false
        }
    }

    private fun openAddDisbandmentBottomSheet() {
        if (requireActivity().supportFragmentManager.findFragmentByTag(AddDisbandmentBottomSheet::class.java.simpleName) == null) {
            AddDisbandmentBottomSheet.newInstance().show(requireActivity().supportFragmentManager,
                AddDisbandmentBottomSheet::class.java.simpleName)
        }
    }
}