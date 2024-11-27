package com.sisindia.ai.android.features.barracks.listing

import android.app.Activity
import android.content.Intent
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.IntentRequestCodes
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentBarrackListingBinding
import com.sisindia.ai.android.features.addtask.AddTaskActivity

/**
 * Created by Ashu Rajput on 4/18/2020.
 */
class BarrackListingFragment : IopsBaseFragment() {

    private lateinit var viewModel: BarrackListingViewModel
    private lateinit var binding: FragmentBarrackListingBinding

    companion object {
        fun newInstance(): BarrackListingFragment = BarrackListingFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_barrack_listing
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(BarrackListingViewModel::class.java)
                as BarrackListingViewModel
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
                    NavigationConstants.OPEN_BARRACK_QR_TAGGING -> startTaggingActivity(message.arg1)
                    NavigationConstants.OPEN_TASK_CREATE_ACTIVITY -> {
//                        startCreateBITaskActivity(message.obj as BarrackListingMO)
                        startCreateBITaskActivity()
                    }
                }
            })
    }

    override fun onCreated() {
        viewModel.apply {
            getBarrackListing()
        }
    }

    private fun startTaggingActivity(barrackId: Int) {
        startActivityForResult(Intent(activity, BarrackTaggingActivity::class.java)
            .putExtra(IntentConstants.BARRACK_ID, barrackId),
            IntentRequestCodes.REQUEST_CODE_BARRACK_TAGGING)
    }

    private fun startCreateBITaskActivity() {
        requireActivity().startActivityForResult(Intent(activity, AddTaskActivity::class.java)
            .putExtra(IntentConstants.IS_CREATING_BARRACK_TASK, true),
            IntentRequestCodes.REQUEST_CODE_ADD_TASK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IntentRequestCodes.REQUEST_CODE_BARRACK_TAGGING && resultCode == Activity.RESULT_OK)
            viewModel.getBarrackListing()
    }
}