package com.sisindia.ai.android.features.taskcheck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment
import com.sisindia.ai.android.constants.IntentConstants.IS_COMING_FROM_SITE_CHECK_IN
import com.sisindia.ai.android.databinding.SkipReasonSheetBinding
import com.sisindia.ai.android.features.onboard.OnBoardViewModel

/**
 * Created by Ashu Rajput on 08-30-2024
 */
class SkipReasonBottomSheet : IopsBaseBottomSheetDialogFragment() {

    lateinit var viewModel: OnBoardViewModel

    companion object {
        fun newInstance(isComingAtCheckInTime: Boolean = true): SkipReasonBottomSheet {
            val dialogFragment = SkipReasonBottomSheet()
            Bundle().apply {
                putBoolean(IS_COMING_FROM_SITE_CHECK_IN, isComingAtCheckInTime)
                dialogFragment.arguments = this
            }
            return dialogFragment
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.skip_reason_sheet
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        val binding = bindFragmentView(layoutResource, inflater, container)
                as SkipReasonSheetBinding
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun extractBundle() {
        arguments?.let {
            if (it.containsKey(IS_COMING_FROM_SITE_CHECK_IN))
                viewModel.isSkipAtCheckInTime.set(it.getBoolean(IS_COMING_FROM_SITE_CHECK_IN))
        }
    }

    override fun initViewState() {
        /*liveData.observe(this) { message: Message ->
            when (message.what) {
                NavigationConstants.ON_SKIP_QR_SCAN_WITH_REASON -> {
                    message.what = NavigationConstants.ON_SKIP_QR_SCAN_WITH_REASON
                    message.obj = message.obj
                    liveData.postValue(message)
                    dismissAllowingStateLoss()
                }
            }
        }*/
    }

    override fun onCreated() {
//        viewModel.initUI()
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(OnBoardViewModel::class.java) as OnBoardViewModel
    }

    override fun applyStyle() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }
}