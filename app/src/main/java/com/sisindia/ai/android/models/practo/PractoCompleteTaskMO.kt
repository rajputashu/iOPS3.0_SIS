package com.sisindia.ai.android.models.practo

import com.google.gson.annotations.SerializedName

/**
 * Created by Ashu Rajput on 02-08-2023
 */
data class PractoCompleteTaskMO(
    @SerializedName("guards")
    val checkedGuardsList: List<PractoCheckedGuardsMO>
)

data class PractoCheckedGuardsMO(
    @SerializedName("employeeId")
    val employeeId: Int,
    @SerializedName("employeeName")
    val employeeName: String,
    @SerializedName("employeeNo")
    val employeeNo: String,
    @SerializedName("guardStatus")
    val guardStatus: Int,
    @SerializedName("siteId")
    val siteId: Int,
    @SerializedName("taskId")
    val taskId: Int,
    @SerializedName("questionsJson")
    val questionsJson: String
)
