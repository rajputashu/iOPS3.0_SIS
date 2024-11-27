package com.sisindia.ai.android.features.taskcheck.clienthandshake.sheet

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.BottomSheetNotMetReasonsBinding

/**
 * Created by Ashu Rajput on 4/8/2020.
 */
class NotMetReasonBottomSheet : IopsBaseBottomSheetDialogFragment() {

    lateinit var binding: BottomSheetNotMetReasonsBinding
    lateinit var viewModel: NotMetReasonsViewModel

    companion object {
        fun newInstance() = NotMetReasonBottomSheet()
    }

    override fun getLayoutResource(): Int {
        return R.layout.bottom_sheet_not_met_reasons
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = bindFragmentView(layoutResource, inflater, container)
                as BottomSheetNotMetReasonsBinding
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
                    NavigationConstants.ON_SELECT_NOT_MET_REASON -> {
                        message.what = NavigationConstants.ON_SELECT_NOT_MET_REASON
                        message.obj = message.obj
                        liveData.postValue(message)
                        dismissAllowingStateLoss()
                    }
                }
            })
    }

    override fun onCreated() {
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(NotMetReasonsViewModel::class.java)
                as NotMetReasonsViewModel
    }

    override fun applyStyle() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }
}