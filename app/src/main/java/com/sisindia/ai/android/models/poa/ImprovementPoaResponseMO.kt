package com.sisindia.ai.android.models.poa

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse
import com.sisindia.ai.android.room.entities.ImprovementPoaEntity

/**
 * Created by Ashu Rajput on 12/14/2020.
 */
data class ImprovementPoaResponseMO(
    @SerializedName("data")
    val improvementPlanData: List<ImprovementPoaEntity>? = null) : BaseNetworkResponse()
