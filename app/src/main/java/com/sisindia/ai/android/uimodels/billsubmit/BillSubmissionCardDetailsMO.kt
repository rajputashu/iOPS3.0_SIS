package com.sisindia.ai.android.uimodels.billsubmit

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 6/1/2020.
 */
@Parcelize
data class BillSubmissionCardDetailsMO(
    var siteName: String,
    var siteCode: String,
    var siteAddress: String,
    var siteId: Int,
    var taskId: Int,
    var isPending: Boolean,
    var overDueBy: String? = null,
    var submittedOnDate: String? = null,
    val isSynced: Int = 1
) : Parcelable