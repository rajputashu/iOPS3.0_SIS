package com.sisindia.ai.android.uimodels.uarpoa

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 12/22/2020.
 */
@Parcelize
data class IPPoaPendingCompletedMO(
    val poaId: Int = 0,
    var siteName: String? = null,
    var assignedToEmployeeName: String? = null,
    var assignedToEmployeeNo: String? = null,
    var targetDateTime: String? = null,
    var description: String? = null,
    var status: String? = null,
    var category: String? = null,
    val isPending: Int = 1) : Parcelable