package com.sisindia.ai.android.uimodels.collection

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 5/21/2020.
 */
@Parcelize
data class DueBillCollectionMO(
    var dueCollection: Int? = 0,
    var siteCode: String? = null,
    var paidAmount: String? = null,
    var outstandingAmount: String? = null,
    var siteName: String? = null,
    var siteId: Int? = 0
) : Parcelable