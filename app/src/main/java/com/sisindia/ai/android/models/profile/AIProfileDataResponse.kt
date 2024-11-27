package com.sisindia.ai.android.models.profile

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse
import com.sisindia.ai.android.room.entities.AIProfileEntity

/**
 * Created by Ashu Rajput on 5/9/2020.
 */
data class AIProfileDataResponse(
    @SerializedName("data")
    var aiProfileData: AIProfileEntity? = null
) : BaseNetworkResponse()
