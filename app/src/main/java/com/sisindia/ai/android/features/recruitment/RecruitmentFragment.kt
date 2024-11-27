package com.sisindia.ai.android.features.recruitment

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentRecruitmentBinding
import com.sisindia.ai.android.features.recruitment.sheet.AddRecruitmentBottomSheet
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

/**
 * Created by Ashu Rajput on 5/6/2020.
 */
class RecruitmentFragment : IopsBaseFragment() {

    private lateinit var viewModel: RecruitmentViewModel
    private lateinit var binding: FragmentRecruitmentBinding

    companion object {
        fun newInstance(): RecruitmentFragment = RecruitmentFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_recruitment
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(RecruitmentViewModel::class.java) as RecruitmentViewModel
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {
                NavigationConstants.OPEN_ADD_RECRUITMENT_SHEET -> openAddRecruitmentBottomSheet()
                NavigationConstants.ON_REFRESHING_RECRUIT_ADDED -> {
                    viewModel.isLoading.set(View.VISIBLE) //{Showing loader before calling API}
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(TimeUnit.SECONDS.toMillis(2))
                        withContext(Dispatchers.Main) {
                            viewModel.getRecruitmentDetailsFromAPI()
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
        viewModel.getRecruitmentDetailsFromAPI()
        binding.swipeLayout.setOnRefreshListener {
            viewModel.getRecruitmentDetailsFromAPI()
            binding.swipeLayout.isRefreshing = false
        }
    }

    private fun openAddRecruitmentBottomSheet() {
        if (requireActivity().supportFragmentManager.findFragmentByTag(AddRecruitmentBottomSheet::class.java.simpleName) == null) {
            AddRecruitmentBottomSheet.newInstance().show(requireActivity().supportFragmentManager,
                AddRecruitmentBottomSheet::class.java.simpleName)
        }
    }
}