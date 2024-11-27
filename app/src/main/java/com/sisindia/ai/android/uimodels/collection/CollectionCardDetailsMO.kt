package com.sisindia.ai.android.uimodels.collection

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 5/30/2020.
 */
@Parcelize
data class CollectionCardDetailsMO(
    var siteName: String,
    var siteCode: String,
    var siteId: String,
    var isPending: Boolean,
    var billDuesCount: String,
    var siteAddress: String
):Parcelable