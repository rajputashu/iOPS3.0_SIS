package com.sisindia.ai.android.uimodels.nudges

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse
import com.sisindia.ai.android.room.entities.NotificationDataEntity

data class PendingNudgesResponseMO(
    @SerializedName("data")
    val data: List<NotificationDataEntity>? = null) : BaseNetworkResponse()
