package com.sisindia.ai.android.models.civil

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse

/**
 * Created by Ashu Rajput on 06/03/2025.
 */
data class NominationSummaryResponseMO(
    @SerializedName("data")
    val dataList: List<NominationSummaryDataMO>? = null) : BaseNetworkResponse()

data class NominationSummaryDataMO(
    @SerializedName("districtId")
    val districtId: Int = 0,
    @SerializedName("tillDateCount")
    val tillDateCount: Int = 0,
    @SerializedName("todayCount")
    val todayCount: Int = 0,
    @SerializedName("target")
    val target: Int = 0,
    @SerializedName("districtName")
    val districtName: String? = null
)
