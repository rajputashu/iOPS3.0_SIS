package com.sisindia.ai.android.uimodels.tasks

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse
import com.sisindia.ai.android.room.entities.CustomerContactEntity

/**
 * Created by Ashu Rajput on 24-04-2023
 */
data class ClientDetailsResponseMO(
    @SerializedName("data")
    val data: List<CustomerContactEntity>? = null) : BaseNetworkResponse()
