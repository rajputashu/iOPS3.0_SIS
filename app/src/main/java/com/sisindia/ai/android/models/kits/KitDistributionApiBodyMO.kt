package com.sisindia.ai.android.models.kits

import com.google.gson.annotations.SerializedName

/**
 * Created by Ashu Rajput on 8/31/2020.
 */
data class KitDistributionApiBodyMO(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("issuingOfficerId")
    var issuingOfficerId: Int? = 0,

    @SerializedName("siteId")
    var siteId: Int? = 0,

    @SerializedName("distributionStatus")
    var distributionStatus: Int = 0,

    /*@SerializedName("siteTaskId")
    var siteTaskId: Int = 0,*/

    @SerializedName("recipientId")
    var recipientId: Int = 0,

    @SerializedName("branchId")
    var branchId: Int = 0,

    @SerializedName("kitTypeId")
    var kitTypeId: Int = 0,

    @SerializedName("kitRequestId")
    var kitRequestId: Int = 0,

    @SerializedName("distributionDateTime")
    var distributionDateTime: String? = null,

    @SerializedName("recipientName")
    var recipientName: String? = null,

    @SerializedName("recipientCode")
    var recipientCode: String? = null,

    @SerializedName("createdDateTime")
    var createdDateTime: String? = null,

    @SerializedName("createdBy")
    var createdBy: Int = 0,

    @SerializedName("updatedDateTime")
    var updatedDateTime: String? = null,

    @SerializedName("updatedBy")
    var updatedBy: Int = 0,

    @SerializedName("kitDistributionItems")
    var kitDistributionItems: List<KitDistributionItemsBodyMO>? = null)