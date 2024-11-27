package com.sisindia.ai.android.features.issues.complaints

import com.sisindia.ai.android.uimodels.ComplaintModel

interface ComplaintItemListener {
    fun onComplaintItemClick(item: ComplaintModel)
}