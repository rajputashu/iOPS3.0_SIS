package com.sisindia.ai.android.models.mask

import com.google.gson.annotations.SerializedName

data class AddMaskRequestBodyMO(
    @SerializedName("guardId")
    var guardId: Int = 0,
    @SerializedName("employeeNo")
    var employeeNo: String? = null,
    @SerializedName("siteId")
    var siteId: Int? = null,
    @SerializedName("isDisributed")
    var isDistributed: Boolean? = true,
    @SerializedName("remarks")
    var remarks: String = "Distributed",
    @SerializedName("createdDateTime")
    var createdDateTime: String? = null,
    @SerializedName("updatedDateTime")
    var updatedDateTime: String? = null)