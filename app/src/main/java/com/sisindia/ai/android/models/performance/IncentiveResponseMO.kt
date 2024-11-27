package com.sisindia.ai.android.models.performance

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse

/**
 * Created by Ashu Rajput on 19-01-2022
 */
data class IncentiveResponseMO(
    @SerializedName("data")
    val data: IncentiveMainDataMO) : BaseNetworkResponse()

data class IncentiveMainDataMO(
    @SerializedName("employees")
    val incentiveDataList: List<IncentiveDataMO>? = null)

data class IncentiveDataMO(
    @SerializedName("eligibility")
    val eligibility: Boolean = false,
    @SerializedName("incentiveAmount")
    val incentiveAmount: Int = 0,
    @SerializedName("presentDays")
    val presentDays: Int = 0,
    @SerializedName("targetDays")
    val targetDays: Int = 0,
    @SerializedName("rotaComplaince")
    val rotaCompliance: Int = 0,
    @SerializedName("rotaComplainceTarget")
    val rotaComplianceTarget: Int = 0,
    @SerializedName("businessResult")
    val businessResult: Int = 0,
    @SerializedName("businessResultTarget")
    val businessResultTarget: Int = 0,
    @SerializedName("effectiveLoad")
    val effectiveLoad: Int = 0,
    @SerializedName("effectiveLoadTarget")
    val effectiveLoadTarget: Int = 0,
    @SerializedName("disbandment")
    val disbandment: Int = 0,
    @SerializedName("holdByBHOrHR")
    val holdByBHOrHR: Boolean = false)

data class IncentiveRowData(
    val incentiveTitle: String? = null,
    val incentiveValue: String? = null,
    val isEligible: Boolean = false)