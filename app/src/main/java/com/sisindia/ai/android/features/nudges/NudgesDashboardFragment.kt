package com.sisindia.ai.android.features.nudges

import android.app.Activity
import android.content.Intent
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.constants.IntentConstants.DYNAMIC_FORM_ID
import com.sisindia.ai.android.constants.NavigationConstants.OPEN_DYNAMIC_NUDGE_SCREEN
import com.sisindia.ai.android.databinding.FragmentNudgesDashboardBinding

/**
 * Created by Ashu Rajput on 07/11/2024.
 */
class NudgesDashboardFragment : IopsBaseFragment() {

    private lateinit var viewModel: NudgesViewModel
    private lateinit var binding: FragmentNudgesDashboardBinding

    companion object {
        fun newInstance(): NudgesDashboardFragment = NudgesDashboardFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_nudges_dashboard
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(NudgesViewModel::class.java) as NudgesViewModel
    }

    override fun extractBundle() {
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {
                OPEN_DYNAMIC_NUDGE_SCREEN -> {
                    val intent = Intent(activity, NudgesDynamicActivity::class.java)
                    intent.putExtra(DYNAMIC_FORM_ID, message.obj as String)
                    dynamicNudgesLauncher.launch(intent)
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
        viewModel.initNudgesDashboard()
    }

    private var dynamicNudgesLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.initNudgesDashboard()
//                result.data?.extras.apply {}
            }
        }
}