package com.sisindia.ai.android.models.poa

import com.google.gson.annotations.SerializedName

data class UarEmployeeDataMO(
    @SerializedName("employeeId")
    val employeeId: Int? = null,
    @SerializedName("employeeName")
    val employeeName: String? = null,
    @SerializedName("employeeNo")
    val employeeNo: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("emailId")
    val emailId: String? = null,
)
