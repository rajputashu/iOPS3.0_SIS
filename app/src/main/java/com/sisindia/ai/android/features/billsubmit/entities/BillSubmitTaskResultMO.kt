package com.sisindia.ai.android.features.billsubmit.entities

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

/**
 * Created by Ashu Rajput on 4/2/2020.
 */
data class BillSubmitTaskResultMO(
    @SerializedName("BillSubmission")
    var billSubmitDetails: BillSubmitDetailsMO? = null,
    @SerializedName("GEOLocation")
    var geoLocation: String? = null,
    @SerializedName("billSubmissionGUID")
    var billSubmissionGUID: String = "")

data class BillSubmitDetailsMO(
    @SerializedName("BillHandedOverTo")
    val billHandedOverTo: String? = null,
    @SerializedName("BillAcceptedName")
    val billAcceptedName: String? = null,
    @SerializedName("BillAcceptedDesignation")
    val billAcceptedDesignation: String? = null,
    @SerializedName("BillCheckList")
    val billCheckList: JsonObject? = null
)