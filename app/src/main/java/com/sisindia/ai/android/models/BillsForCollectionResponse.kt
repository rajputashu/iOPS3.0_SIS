package com.sisindia.ai.android.models

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse
import com.sisindia.ai.android.room.entities.BillCollectionsEntity

/**
 * Created by Ashu Rajput on 4/3/2021.
 */
class BillsForCollectionResponse(
    @SerializedName("data")
    val billsList: List<BillCollectionsEntity>? = null
) : BaseNetworkResponse()