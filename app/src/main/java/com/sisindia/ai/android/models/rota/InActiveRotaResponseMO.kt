package com.sisindia.ai.android.models.rota

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse

data class InActiveRotaResponseMO(
    @SerializedName("data")
    val data: List<Int>? = null) : BaseNetworkResponse()
