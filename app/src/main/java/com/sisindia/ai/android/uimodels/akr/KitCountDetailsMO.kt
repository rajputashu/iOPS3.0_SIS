package com.sisindia.ai.android.uimodels.akr

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 4/17/2020.
 */
@Parcelize
data class KitCountDetailsMO(
    var DueSinceLastMonth: Int? = 0,
    var AddedThisMonth: Int? = 0,
    var DistributedThisMonth: Int? = 0,
    var PendingForDistribution: Int? = 0) : Parcelable