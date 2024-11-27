package com.sisindia.ai.android.uimodels.uarpoa

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 4/8/2020.
 */
@Parcelize
data class AtRiskAndPoaDetailsMO(
    var SiteId: Int = 0,
    var SiteName: String? = null,
    var targetCompletionDate: String? = null,
    var Pending: String? = null,
    var TotalPOAs: String? = null,
    var Complete: String? = null,
    var Progress: Int = 0) : Parcelable