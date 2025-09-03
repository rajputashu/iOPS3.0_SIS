package com.sisindia.ai.android.models.poa

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse
import com.sisindia.ai.android.models.CommonDataResponseMO

data class AddedImprovePlanResponseMO(
    @SerializedName("data")
    val data: CommonDataResponseMO? = null
) : BaseNetworkResponse()
