package com.sisindia.ai.android.models.sales

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse

/**
 * Created by Ashu Rajput on 5/6/2021.
 */
data class SalesRefSectorsResponseMO(
    @SerializedName("data")
    val sectorList: List<SectorMO>? = null) : BaseNetworkResponse()

data class SectorMO(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("sectorName")
    var sectorName: String? = null)