package com.sisindia.ai.android.uimodels.akr

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 4/17/2020.
 */
@Parcelize
data class KitAssignedDistributedMO(
    var guardId: Int? = null,
    var guardName: String? = null,
    var guardRegNo: String? = null,
    var totalItems: String? = null,
    var date: String? = null,
    var kitIssueNo: String? = null,
    var status: Int? = 0) : Parcelable