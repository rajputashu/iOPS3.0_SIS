package com.sisindia.ai.android.uimodels.uarpoa

import android.os.Parcelable
import androidx.room.Ignore
import kotlinx.android.parcel.Parcelize
import org.parceler.Parcel

/**
 * Created by Ashu Rajput on 12/21/2020.
 */
@Parcelize
data class SitesWithImprovePlansMO(
    var siteName: String? = null,
    var siteCode: String? = null,
    var siteId: Int = 0,
    var totalIP: String? = "0",
    var pendingIP: String? = "0",
    var completedIP: String? = "0",
    @Ignore
    var progress: Int = 0) : Parcelable