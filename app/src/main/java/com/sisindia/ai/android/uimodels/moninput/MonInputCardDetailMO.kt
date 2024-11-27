package com.sisindia.ai.android.uimodels.moninput

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 6/4/2020.
 */
@Parcelize
data class MonInputCardDetailMO(
    var siteName: String,
    var siteCode: String,
    var siteAddress: String,
    var siteId: Int,
    var taskId: Int,
    var isPending: Boolean,
    var overDueBy: String? = null,
    var submittedOnDate: String? = null
) : Parcelable