package com.sisindia.ai.android.features.sync

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.commons.YesNoDialogFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentManualSyncBinding


class ManualSyncFragment : IopsBaseFragment() {

    lateinit var binding: FragmentManualSyncBinding
    lateinit var viewModel: ManualSyncViewModel

    companion object {
        fun newInstance(): ManualSyncFragment = ManualSyncFragment()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_manual_sync
    }

    override fun extractBundle() {
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(ManualSyncViewModel::class.java) as ManualSyncViewModel
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = bindFragmentView(layoutResource, inflater, container) as FragmentManualSyncBinding
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun initViewState() {
        liveData.observe(this, { message: Message ->
            if (message.what == NavigationConstants.OPEN_MASTER_SYNC_DIALOG)
                openConfirmationDialog()
        })
    }

    private fun openConfirmationDialog() {
        val dialog =
            YesNoDialogFragment.newInstance(resources.getString(R.string.string_sync_master_data))
        dialog.show(requireActivity().supportFragmentManager,
            YesNoDialogFragment::class.java.simpleName)
        dialog.initDialogListener(viewModel.dialogListener)
        dialog.isCancelable = false
    }

    override fun onCreated() {
        viewModel.initManualSyncAdapter()
    }
}