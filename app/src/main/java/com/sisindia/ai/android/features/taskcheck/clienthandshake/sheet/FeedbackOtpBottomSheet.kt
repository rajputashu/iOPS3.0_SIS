package com.sisindia.ai.android.features.taskcheck.clienthandshake.sheet

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseBottomSheetDialogFragment
import com.sisindia.ai.android.constants.IntentConstants
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.BottomSheetClientOtpBinding

/**
 * Created by Ashu Rajput on 4/10/2020.
 */
class FeedbackOtpBottomSheet : IopsBaseBottomSheetDialogFragment() {

    lateinit var binding: BottomSheetClientOtpBinding
    lateinit var viewModel: FeedbackOtpSheetViewModel

    companion object {
        fun newInstance(clientNo: String) = FeedbackOtpBottomSheet().apply {
            arguments = Bundle().apply { putString(IntentConstants.CLIENT_NO, clientNo) }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.bottom_sheet_client_otp
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = bindFragmentView(layoutResource, inflater, container)
                as BottomSheetClientOtpBinding
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun extractBundle() {
        val mobNumber = arguments.let {
            it!!.getString(IntentConstants.CLIENT_NO)
        }
        viewModel.clientMobNo.set(mobNumber)
        viewModel.requestOTPFromClient()
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {
                NavigationConstants.ON_SUCCESSFUL_OTP_VERIFY -> {
                    message.what = NavigationConstants.ON_SUCCESSFUL_OTP_VERIFY
                    liveData.postValue(message)
                    dismissAllowingStateLoss()
                }

                NavigationConstants.ON_CLOSE_SCREEN -> dismissAllowingStateLoss()
            }
        }
    }

    override fun onCreated() {
        viewModel.initResendOTPTimer()
    }

    override fun initViewModel() {
        viewModel = getAndroidViewModel(FeedbackOtpSheetViewModel::class.java)
                as FeedbackOtpSheetViewModel
    }

    override fun applyStyle() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }
}