package com.sisindia.ai.android.features.disband

import com.google.gson.annotations.SerializedName

/**
 * Created by Ashu Rajput on 01-04-2023
 */
data class AddDisbandmentBodyMO(

    @SerializedName("siteId")
    var siteId: Int? = null,

    @SerializedName("disbandmentReason")
    var disbandmentReason: String? = null,

    @SerializedName("disbandmentRemarks")
    var disbandmentRemarks: String? = null,

    @SerializedName("attachmentUrl")
    var attachmentUrl: String = "",

    @SerializedName("disbandmentDate")
    var disbandmentDate: String? = null,

    @SerializedName("status")
    var status: Int = 0)
