package com.sisindia.ai.android.models.civil

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse
import com.sisindia.ai.android.room.entities.EmployeeSiteEntity

/**
 * Created by Ashu Rajput on 06/16/2025.
 */
data class CivilEmpResponseMO(
    @SerializedName("data")
    val data: CivilEmpResponseDataMO? = null) : BaseNetworkResponse()

data class CivilEmpResponseDataMO(
    @SerializedName("msg")
    val msg: String? = null,
    @SerializedName("data")
    val empData: EmployeeSiteEntity? = null)
