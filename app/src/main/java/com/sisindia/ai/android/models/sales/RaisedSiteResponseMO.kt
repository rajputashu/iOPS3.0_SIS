package com.sisindia.ai.android.models.sales

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse

/**
 * Created by Ashu_Rajput on 5/22/2021.
 */

data class RaisedSiteResponseMO(
    @SerializedName("data")
    val siteDetail: RaisingSiteDetailsMO? = null) : BaseNetworkResponse()

data class RaisingSiteDetailsMO(
    @SerializedName("siteId")
    var siteId: Int? = null,
    @SerializedName("siteName")
    var siteName: String? = null,
    @SerializedName("siteCode")
    var siteCode: String? = null)