package com.sisindia.ai.android.features.taskcheck.clienthandshake.sheet

import android.app.Application
import android.os.CountDownTimer
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.BaseNetworkResponse
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.commons.KitOTP
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.utils.TimeUtils
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/10/2020.
 */
class FeedbackOtpSheetViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    var clientOTP = ObservableField("")
    var clientMobNo = ObservableField("")
    var otpTypeId = ObservableInt(1)
    val isTimerEnable = ObservableBoolean(true)
    val waitingTime = ObservableField("")
    val obsShowSkipLayout = ObservableBoolean(false)
    var isKitOTPRequestModule: Boolean = false

    fun requestOTPFromClient(isKitOTPRequest: Boolean = false) {
        isLoading.set(View.VISIBLE)

        val requestType = if (isKitOTPRequest)
            coreApi.requestKitOTP(clientMobNo.get().toString())
        else
            coreApi.requestClientOTP(clientMobNo.get().toString())

        addDisposable(requestType
            .compose(transformSingle())
            .subscribe({
                isLoading.set(View.GONE)
                isTimerEnable.set(true)
                if (it?.statusCode != 200)
                    showToast(it?.statusMessage)
            }) { t: Throwable? ->
                isLoading.set(View.GONE)
                onApiError(t)
            })
    }

    private fun validateWhenToShowSkipLayout() {
        if (otpTypeId.get() == KitOTP.VERIFY_SKIP_OTP.kitOtpType) {
            obsShowSkipLayout.set(true)
        }
    }

    fun onBtnClick(view: View) {
        when (view.id) {
            R.id.otpCrossButton -> {
                message.what = NavigationConstants.ON_CLOSE_SCREEN
                liveData.postValue(message)
            }

            R.id.skipOtpVerification -> {
                message.what = NavigationConstants.ON_SUCCESSFUL_OTP_VERIFY
                liveData.postValue(message)
            }

            R.id.resendOTP -> {
                showToast("Satisfaction code, resent successfully")
                obsShowSkipLayout.set(false)
                initResendOTPTimer()
                requestOTPFromClient(isKitOTPRequestModule)
            }

            R.id.otpDoneButton -> {
                when {
                    clientOTP.get().toString()
                        .isEmpty() -> showToast("Please enter valid satisfaction code")

                    clientOTP.get()
                        .toString().length < 4 -> showToast("Please enter valid 4 digit satisfaction code")

                    else -> submitAndVerifyOTP()
                }
            }
        }
    }

    private fun submitAndVerifyOTP() {
        isLoading.set(View.VISIBLE)
        addDisposable(coreApi.submitClientOTP(clientMobNo.get().toString(),
            clientOTP.get().toString())
            .compose(transformSingle()).subscribe({ response: BaseNetworkResponse? ->
                isLoading.set(View.GONE)
                if (response?.statusCode == 200) {
                    showToast("Satisfaction code verified successfully")
                    message.what = NavigationConstants.ON_SUCCESSFUL_OTP_VERIFY
                    liveData.postValue(message)
                } else {
                    showToast(response?.statusMessage)
                }
            }) {
                isLoading.set(View.GONE)
                showToast("Unable to verify OTP")
            })
    }

    fun initResendOTPTimer() {
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val sec = (millisUntilFinished / 1000).toInt()
                waitingTime.set(TimeUtils.dutyOnOffWaitingTime(sec))
            }

            override fun onFinish() {
                isTimerEnable.set(false)
                validateWhenToShowSkipLayout()
            }
        }.start()
    }
}