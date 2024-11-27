package com.sisindia.ai.android.models

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse
import com.sisindia.ai.android.room.entities.EmployeeFineRewardEntity
import com.sisindia.ai.android.room.entities.EmployeeSiteEntity

/**
 * Created by Ashu Rajput on 3/10/2021.
 */
data class EmployeeAndRewardFineResponse(
    @SerializedName("data")
    val data: EmployeeRewardFineData? = null) : BaseNetworkResponse()

data class EmployeeRewardFineData(
    @SerializedName("employees")
    var employeeData: List<EmployeeSiteEntity>? = null,
    @SerializedName("rewardFines")
    var fineRewardData: List<EmployeeFineRewardEntity>? = null
    //    var fineRewardData: List<FineRewardMasterEntity>? = null
)

