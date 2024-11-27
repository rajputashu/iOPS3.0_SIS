package com.sisindia.ai.android.models.recruit

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse

/**
 * Created by Ashu Rajput on 5/6/2020.
 */
data class RecruitmentApiResponseMO(
    @SerializedName("data")
    val data: ResultDataMO? = null
) : BaseNetworkResponse()

data class ResultDataMO(
    @SerializedName("recruits")
    val recruitedList: List<RecruitedMO>? = null,
    @SerializedName("currentYear")
    val currentYearCountList: List<CurrentYearCountsMO>? = null,
    @SerializedName("summary")
    val recruitGraphList: List<RecruitmentDataMO>? = null
)

data class RecruitmentDataMO(
    @SerializedName("selectedCount")
    val selectedCount: Int? = null,
    @SerializedName("rejectedCount")
    val rejectedCount: Int? = null,
    @SerializedName("inProcessCount")
    val inProcessCount: Int? = null,
    @SerializedName("recommendedCount")
    val recommended: Int? = null,
    @SerializedName("month")
    val month: Int? = null,
    @SerializedName("year")
    val year: Int? = null
)

data class CurrentYearCountsMO(
    @SerializedName("recommendedCount")
    val yearRecommendedCount: Int? = null,
    @SerializedName("selectedCount")
    val yearSelectedCount: Int? = null,
    @SerializedName("rejectedCount")
    val yearRejectedCount: Int? = null)

data class RecruitedMO(
    @SerializedName("id")
    val recruitmentId: Int? = null,
    @SerializedName("fullName")
    val fullName: String? = null,
    @SerializedName("mobileNo")
    val contactNo: String? = null,
    @SerializedName("dateofBirth")
    val dob: String? = null,
    @SerializedName("ActionTakenOn")
    val actionTakenOn: String? = null,
    @SerializedName("recruitStatus")
    val status: Int? = null
)