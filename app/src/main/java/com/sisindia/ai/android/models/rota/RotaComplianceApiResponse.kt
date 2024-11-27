package com.sisindia.ai.android.models.rota

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse

/**
 * Created by Ashu Rajput on 6/8/2020.
 */
class RotaComplianceApiResponse(
    @SerializedName("data")
    val data: List<RotaComplianceResultMO>? = null) : BaseNetworkResponse()

data class RotaComplianceResultMO(
    @SerializedName("taskType")
    val taskType: String? = null,
    @SerializedName("taskCompliance")
    val taskCompliance: Int? = null,
    @SerializedName("target")
    val target: Int? = null,
    @SerializedName("actual")
    val actual: Int? = null,
    @SerializedName("totalScore")
    val totalScore: Int? = null,
    @SerializedName("achievedScore")
    val achievedScore: Int? = null)

