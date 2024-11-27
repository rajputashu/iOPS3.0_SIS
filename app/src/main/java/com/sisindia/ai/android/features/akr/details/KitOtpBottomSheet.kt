package com.sisindia.ai.android.features.akr.details

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
import com.sisindia.ai.android.databinding.BottomSheetKitOtpBinding
import com.sisindia.ai.android.features.taskcheck.clienthandshake.sheet.FeedbackOtpSheetViewModel

/**
 * Created by Ashu Rajput on 22-05-2023
 */
class KitOtpBottomSheet : IopsBaseBottomSheetDialogFragment() {

    lateinit var binding: BottomSheetKitOtpBinding
    lateinit var viewModel: FeedbackOtpSheetViewModel

    companion object {
        fun newInstance(clientNo: String, otpTypeId: Int) = KitOtpBottomSheet().apply {
            arguments = Bundle().apply {
                putString(IntentConstants.CLIENT_NO, clientNo)
                putInt(IntentConstants.OTP_TYPE_ID, otpTypeId)
            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.bottom_sheet_kit_otp
    }

    override fun initViewBinding(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = bindFragmentView(layoutResource, inflater, container)
                as BottomSheetKitOtpBinding
        binding.vm = viewModel
        binding.executePendingBindings()
        return binding.root
    }

    override fun extractBundle() {
        arguments.let {
            viewModel.clientMobNo.set(it!!.getString(IntentConstants.CLIENT_NO))
            viewModel.otpTypeId.set(it.getInt(IntentConstants.OTP_TYPE_ID, 1))
        }
    }

    override fun initViewState() {
        liveData.observe(this) { message: Message ->
            when (message.what) {
                NavigationConstants.ON_SUCCESSFUL_OTP_VERIFY -> {
                    message.what = NavigationConstants.ON_SUCCESSFUL_OTP_VERIFY_SKIP
                    liveData.postValue(message)
                    dismissAllowingStateLoss()
                }

                NavigationConstants.ON_CLOSE_SCREEN -> dismissAllowingStateLoss()
            }
        }
    }

    override fun onCreated() {
        viewModel.requestOTPFromClient(true)
        viewModel.isKitOTPRequestModule = true
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