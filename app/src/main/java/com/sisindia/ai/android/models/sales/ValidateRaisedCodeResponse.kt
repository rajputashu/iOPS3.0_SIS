package com.sisindia.ai.android.models.sales

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse

/**
 * Created by Ashu_Rajput on 7/29/2021.
 */
data class ValidateRaisedCodeResponse(
    @SerializedName("data")
    val raiseCode: String? = null) : BaseNetworkResponse()
