package com.sisindia.ai.android.models.poa

import com.google.gson.annotations.SerializedName

/**
 * Created by Ashu Rajput on 4/27/2020.
 */
class ClosePoaApiBodyMO(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("siteRiskId")
    var siteRiskId: Int? = null,
    @SerializedName("closureDateTime")
    var closureDateTime: String? = null,
    @SerializedName("closingRemarks")
    var closingRemarks: String? = null,
    @SerializedName("closureProofAttachment")
    var closureProofAttachment: Int? = null)