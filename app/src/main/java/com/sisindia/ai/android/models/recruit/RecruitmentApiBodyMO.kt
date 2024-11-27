package com.sisindia.ai.android.models.recruit

import com.google.gson.annotations.SerializedName

/**
 * Created by Ashu Rajput on 5/19/2020.
 */
data class RecruitmentApiBodyMO(
    @SerializedName("recruitGuid")
    var recruitGuid: String = "",
    @SerializedName("fullName")
    var recruitName: String? = null,
    @SerializedName("dateofBirth")
    var dateOfBirth: String? = null,
    @SerializedName("mobileNo")
    var recruitPhoneNumber: String? = null,
    @SerializedName("referredById")
    var referredById: Int? = null,
    @SerializedName("recruitStatus")
    var recruitStatus: Int? = null,
    @SerializedName("referredDateTime")
    var referredDateTime: String? = null,
    @SerializedName("aadharNo")
    var aadharNo: String? = null
)