package com.sisindia.ai.android.models.performance

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse

/**
 * Created by Ashu Rajput on 12/31/2020.
 */
data class AOConveyanceSummaryResponseMO(
    @SerializedName("data")
    val data: AOConveyanceSummaryData) : BaseNetworkResponse()

data class AOConveyanceSummaryData(
    @SerializedName("today")
    val todayConveyance: Double? = null,
    @SerializedName("thisMonth")
    val thisMonthConveyance: Double? = null,
    @SerializedName("thisMonthIncentive")
    val thisMonthIncentive: Double? = null,
    @SerializedName("lastMonthIncentive")
    val lastMonthIncentive: Double? = null)