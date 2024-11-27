package com.sisindia.ai.android.models.rota

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse
import com.sisindia.ai.android.room.entities.TaskEntity


/**
 * Created by Ashu Rajput on 10/11/2020.
 */
data class MiBsRotaResponse(
    @SerializedName("data")
    var rota: MiBsRotaData? = null) : BaseNetworkResponse()

data class MiBsRotaData(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("employeeId")
    var employeeId: Int = 0,

    @SerializedName("rotaTypeId")
    var rotaTypeId: Int = 0,

    @SerializedName("rotaWeek")
    var rotaWeek: Int = 0,

    @SerializedName("rotaStatus")
    var rotaStatus: Int = 0,

    @SerializedName("rotaMonth")
    var rotaMonth: Int = 0,

    @SerializedName("rotaYear")
    var rotaYear: Int = 0,

    @SerializedName("rotaDate")
    var rotaDate: String? = null,

    @SerializedName("rotaPublishedDate")
    var rotaPublishedDate: String? = null,

    @SerializedName("siteTasks")
    var siteTasks: List<TaskEntity>? = null)