package com.sisindia.ai.android.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Ashu Rajput on 25-07-2023
 */
data class AdHocNotificationMO(
    @SerializedName("taskId")
    val taskId: Int = 0,

    @SerializedName("ApprovedBy")
    val approvedBy: Int = 0,

    @SerializedName("TaskStatus")
    val taskStatus: Int = 1,

    @SerializedName("ApprovedOn")
    val approvedOn: String? = null)
