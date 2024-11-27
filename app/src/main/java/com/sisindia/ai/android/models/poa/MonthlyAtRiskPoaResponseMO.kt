package com.sisindia.ai.android.models.poa

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse
import com.sisindia.ai.android.room.entities.SiteAtRiskEntity

/**
 * Created by Ashu Rajput on 9/15/2020.
 */
data class MonthlyAtRiskPoaResponseMO(
    @SerializedName("data")
    val siteRisks: List<SiteAtRiskEntity>? = null) : BaseNetworkResponse()