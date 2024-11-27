package com.sisindia.ai.android.uimodels.raising

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.parceler.Parcel

/**
 * Created by Ashu Rajput on 7/7/2020.
 */
@Parcelize
data class SiteRaisingCardsMO(

    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("siteName")
    var siteName: String? = null,

    @SerializedName("siteAddress")
    var siteAddress: String? = null,

    @SerializedName("siteCode")
    var siteCode: String? = null,

    @SerializedName("isActive")
    var isActive: Boolean? = null,

    @SerializedName("siteStatus")
    var siteStatus: Int? = null,

    @SerializedName("createdDateTime")
    var createdDateTime: String? = null) : Parcelable