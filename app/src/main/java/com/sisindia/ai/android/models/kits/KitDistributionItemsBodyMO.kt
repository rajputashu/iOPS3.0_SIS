package com.sisindia.ai.android.models.kits

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by Ashu Rajput on 8/31/2020.
 */
data class KitDistributionItemsBodyMO(
    @PrimaryKey @SerializedName("id")
    var id: Int = 0,

    @SerializedName("kitDistributionId")
    var kitDistributionId: Int = 0,

    @SerializedName("isIssued")
    var isIssued: Boolean = false,

    @SerializedName("issuedDate")
    var issuedDate: String? = null,

    @SerializedName("nonIssueReason")
    var nonIssueReason: Int? = null,

    @SerializedName("nonIssueComments")
    var nonIssueComments: String? = null,

    @SerializedName("kitItemId")
    var kitItemId: Int = 0,

    @SerializedName("itemSizeCode")
    var itemSizeCode: String? = null,

    @SerializedName("itemName")
    var itemName: String? = null,

    @SerializedName("itemCode")
    var itemCode: String? = null)