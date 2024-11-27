package com.sisindia.ai.android.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Ashu Rajput on 4/24/2020.
 */
data class AddClientApiBodyMO(
    @SerializedName("Title")
    var title: String = "",
    @SerializedName("Role")
    var designation: String? = null,
    @SerializedName("ContactFullName")
    var clientFullName: String? = null,
    @SerializedName("ContactEmailId")
    var clientEmailId: String? = null,
    @SerializedName("ContactPhoneNo")
    var clientPhoneNo: String? = null,
    @SerializedName("CustomerId")
    var clientId: Int? = null,
    @SerializedName("CustomerMobileNo")
    var clientMobileNo: String? = null,
    @SerializedName("SiteId")
    var siteId: Int? = null
)