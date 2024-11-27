package com.sisindia.ai.android.features.issues

import android.view.View
import androidx.databinding.ObservableField
import com.sisindia.ai.android.IopsApplication
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import javax.inject.Inject

// BELOW VIEW MODEL IS USED FOR ISSUES(GRIEVANCE, COMPLAINTS) AND POA (UAR, IMPROVEMENTS)
class IssueManagementViewModel @Inject constructor(private val appl: IopsApplication) : IopsBaseViewModel(appl) {

    val issueType = ObservableField(IssueType.GRIEVANCE)

    fun ivRotaDrawerClick(view: View) {
        message.what = NavigationConstants.OPEN_DASH_BOARD_DRAWER
        liveData.postValue(message)
    }

    fun onClickDelegate(view: View) {
        if (issueType.get() == IssueType.IMPROVEMENT_PLAN)
            message.what = NavigationConstants.ON_IMPROVEMENT_POA_CLICK
        else if (issueType.get() == IssueType.UAR)
            message.what = NavigationConstants.ON_UNITS_RISK_POA_CLICK
        liveData.postValue(message)
    }

    fun tvAddIssueClick(view: View) {
        when (issueType.get()) {
            IssueType.GRIEVANCE -> {
                message.what = NavigationConstants.ON_CREATE_GRIEVANCE
                liveData.postValue(message)
            }

            IssueType.COMPLAINT -> {
                message.what = NavigationConstants.ON_CREATE_COMPLAINT
                liveData.postValue(message)
            }

            IssueType.IMPROVEMENT_PLAN -> {
                showToast("add issue improvement plan")
            }
            else -> {

            }
        }
    }

    enum class IssueType {
        GRIEVANCE, COMPLAINT, IMPROVEMENT_PLAN, UAR
    }
}