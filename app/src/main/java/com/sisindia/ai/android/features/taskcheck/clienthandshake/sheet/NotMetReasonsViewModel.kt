package com.sisindia.ai.android.features.taskcheck.clienthandshake.sheet

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.features.billsubmit.RadioCheckedListener
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/9/2020.
 */
class NotMetReasonsViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    val selectedReason = ObservableField<String>("")

    val onRadioCheckedListener = object : RadioCheckedListener {
        override fun onRadioButtonChecked(value: String) {
            selectedReason.set(value)
        }
    }

    fun onDoneBtnClick(view: View) {
        if (selectedReason.get().toString().isEmpty())
            showToast(app.resources.getString(R.string.valid_msg_select_reason))
        else {
            message.what = NavigationConstants.ON_SELECT_NOT_MET_REASON
            message.obj = selectedReason.get().toString()
            liveData.postValue(message)
        }
    }
}