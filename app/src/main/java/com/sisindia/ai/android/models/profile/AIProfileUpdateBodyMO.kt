package com.sisindia.ai.android.models.profile

import com.google.gson.annotations.SerializedName

/**
 * Created by Ashu Rajput on 5/11/2020.
 */
data class AIProfileUpdateBodyMO(

    @SerializedName("employeeId")
    var aiId: Int = 0,

    @SerializedName("addressLine")
    var altAddress: String? = null,

    @SerializedName("pincode")
    var pinCode: String? = null,

    @SerializedName("GeoPointString")
    //    var geoPoint: GeoPointItem? = null,
    var geoPoint: String? = null,

    @SerializedName("secondaryContactNo")
    var altContactNo: String? = null,

    @SerializedName("createdDateTime")
    var createdDateTime: String? = null,

    @SerializedName("updatedDateTime")
    var updatedDateTime: String? = null

)