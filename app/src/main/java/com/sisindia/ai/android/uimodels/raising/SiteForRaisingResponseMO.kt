package com.sisindia.ai.android.uimodels.raising

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse

/**
 * Created by Ashu Rajput on 7/15/2020.
 */
data class SiteForRaisingResponseMO(
    @SerializedName("data")
    val data: List<SiteRaisingCardsMO>? = null) : BaseNetworkResponse()