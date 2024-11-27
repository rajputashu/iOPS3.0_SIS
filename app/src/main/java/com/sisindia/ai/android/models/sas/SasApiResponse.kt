package com.sisindia.ai.android.models.sas

import com.google.gson.annotations.SerializedName

/**
 * Created by Ashu Rajput on 4/13/2020.
 */
data class SasApiResponse(
    @SerializedName("statusCode")
    val statusCode: Int,

    @SerializedName("statusMessage")
    val statusMessage: String? = null,

    @SerializedName("data")
    val data: SasTokenMO? = null)

data class SasTokenMO(
    @SerializedName("sasToken")
    val sasToken: String? = null)
