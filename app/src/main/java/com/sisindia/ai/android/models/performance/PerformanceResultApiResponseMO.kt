package com.sisindia.ai.android.models.performance

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse

/**
 * Created by Ashu Rajput on 6/3/2020.
 */
data class PerformanceResultApiResponseMO(
    @SerializedName("data")
    val data: PerformanceResultsMO) : BaseNetworkResponse()

data class PerformanceResultsMO(
    @SerializedName("rotaCompliance")
    val rotaCompliance: Double?,
    @SerializedName("effectiveLoad")
    val effectiveLoad: Int?,
    @SerializedName("loadTarget")
    val loadTarget: Int?,
    @SerializedName("businessResult")
    val businessResult: Int?,
    @SerializedName("quality")
    val quality: Int?,
    @SerializedName("month")
    val month: String?,
    @SerializedName("incentiveStatus")
    val incentiveStatus: String?,
    @SerializedName("reason")
    val reason: String?,
    @SerializedName("amount")
    val amount: Double?) {
    constructor() : this(0.0, 0, 0,
        0, 0, "", "", "", 0.0)
}