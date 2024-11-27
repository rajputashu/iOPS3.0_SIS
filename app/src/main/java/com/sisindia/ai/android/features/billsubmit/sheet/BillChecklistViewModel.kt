package com.sisindia.ai.android.features.billsubmit.sheet

import android.app.Application
import android.view.View
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 4/1/2020.
 */
class BillChecklistViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    val billCheckListAdapter = BillCheckListAdapter()

    fun onBillChecklistDone(view: View) {
        message.what = NavigationConstants.ON_BILL_CHECKLIST_DONE
        message.obj = billCheckListAdapter.getOptedCheckList()
        liveData.value = message
    }
}