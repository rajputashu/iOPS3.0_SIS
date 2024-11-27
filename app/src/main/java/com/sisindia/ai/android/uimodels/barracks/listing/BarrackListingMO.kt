package com.sisindia.ai.android.uimodels.barracks.listing

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 4/23/2020.
 */
@Parcelize
data class BarrackListingMO(
    var id: Int? = 0,
    var name: String? = null,
    var barrackCode: String? = null,
    var barrackAddress: String? = null,
    var srNumber: String? = null,
    var isTaggingDone: Int? = null,
    var lastInspectionDate: String? = null) : Parcelable