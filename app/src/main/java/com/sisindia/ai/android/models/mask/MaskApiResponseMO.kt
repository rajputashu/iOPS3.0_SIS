package com.sisindia.ai.android.models.mask

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse

data class MaskApiResponseMO(
    @SerializedName("data")
    val data: MaskDataMO? = null) : BaseNetworkResponse()

data class MaskDataMO(
    @SerializedName("count")
    val distributedMaskCount: Int? = 0,
    @SerializedName("date")
    val distributedMaskList: List<DistributedMaskMO>? = null)

data class DistributedMaskMO(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("guardId")
    val guardId: Int? = null,
    @SerializedName("siteId")
    val siteId: Int? = null,
    @SerializedName("siteName")
    val siteName: String? = null,
    @SerializedName("siteCode")
    val siteCode: String? = null,
    @SerializedName("guardRegNo")
    val guardRegNo: String? = null,
    @SerializedName("guardName")
    val guardName: String? = null,
    @SerializedName("remarks")
    val remarks: String? = null,
    @SerializedName("isDisributed")
    val isDistributed: Boolean? = false,
    @SerializedName("updatedDateTime")
    val updatedDateTime: String? = null,
    @SerializedName("createdDateTime")
    val createdDateTime: String? = null)