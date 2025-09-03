package com.sisindia.ai.android.models

import com.google.gson.annotations.SerializedName

data class CommonDataResponseMO(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("uuid")
    val receivedUUID: String? = null)
