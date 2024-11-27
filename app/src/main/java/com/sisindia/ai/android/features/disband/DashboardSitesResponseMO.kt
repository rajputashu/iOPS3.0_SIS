package com.sisindia.ai.android.features.disband

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse

/**
 * Created by Ashu Rajput on 01-04-2023
 */
data class DashboardSitesResponseMO(
    @SerializedName("data")
    val data: DashboardDisbandmentData? = null) : BaseNetworkResponse()

data class DashboardDisbandmentData(
    @SerializedName("approved")
    val approved: Int? = 0,
    @SerializedName("requested")
    val requested: Int? = 0,
    @SerializedName("data")
    val siteData: List<DashboardDisbandmentSitesData>? = null)

data class DashboardDisbandmentSitesData(
    @SerializedName("siteId")
    val siteId: Int? = null,
    @SerializedName("siteName")
    val siteName: String? = null,
    @SerializedName("siteCode")
    val siteCode: String? = null,
    @SerializedName("statusName")
    val statusName: String? = null,
    @SerializedName("disbandmentReason")
    val disbandmentReason: String? = null,
    @SerializedName("disbandmentRemarks")
    val disbandmentRemarks: String? = null,
    @SerializedName("requestedDateTime")
    val requestedDateTime: String? = null,
    @SerializedName("approvedDateTime")
    val approvedDateTime: String? = null)
