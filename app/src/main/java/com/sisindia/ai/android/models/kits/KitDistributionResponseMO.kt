package com.sisindia.ai.android.models.kits

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse
import com.sisindia.ai.android.room.entities.KitDistributionListEntity

/**
 * Created by Ashu Rajput on 8/20/2020.
 */
data class KitDistributionResponseMO(
    @SerializedName("data")
    var kitDistributionList: List<KitDistributionListEntity>? = null) : BaseNetworkResponse()