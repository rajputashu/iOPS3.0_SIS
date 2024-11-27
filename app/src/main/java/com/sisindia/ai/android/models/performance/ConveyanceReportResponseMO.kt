package com.sisindia.ai.android.models.performance

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse

/**
 * Created by Ashu Rajput on 12/29/2020.
 */
data class ConveyanceReportResponseMO(
    @SerializedName("data")
    val data: List<ConveyanceReportDataMO>) : BaseNetworkResponse()

data class ConveyanceReportDataMO(
    @SerializedName("taskDate")
    val taskDate: String? = null,
    @SerializedName("toatlTask")
    val totalTasks: Int? = null,
    @SerializedName("totalArialDistance")
    val totalArialDistance: Double? = null,
    @SerializedName("dayMove")
    val dayMove: Double? = null,
    @SerializedName("approvalDistance")
    val approvalDistance: Double? = null,
    @SerializedName("deductionDistance")
    val deductionDistance: Double? = null)