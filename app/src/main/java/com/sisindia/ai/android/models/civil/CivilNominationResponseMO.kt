package com.sisindia.ai.android.models.civil

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse
import com.sisindia.ai.android.room.entities.DistrictsEntity
import com.sisindia.ai.android.room.entities.StatesEntity

/**
 * Created by Ashu Rajput on 06/03/2025.
 */
data class CivilNominationResponseMO(
    @SerializedName("data")
    val data: CivilNominationDataMO? = null) : BaseNetworkResponse()

data class CivilNominationDataMO(
    @SerializedName("states")
    val stateList: List<StatesEntity>? = null,
    @SerializedName("districts")
    val districtList: List<DistrictsEntity>? = null)
