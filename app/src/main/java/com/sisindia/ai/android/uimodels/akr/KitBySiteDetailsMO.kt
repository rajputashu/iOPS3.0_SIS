package com.sisindia.ai.android.uimodels.akr

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 4/17/2020.
 */
@Parcelize
data class KitBySiteDetailsMO(
    var siteId: Int? = 0,
    var siteName: String? = null,
    var kitPending: String? = null,
    var kitItems: String? = null,
    var kitDistributed: String? = null,
    var unPaidKits: String? = null) : Parcelable