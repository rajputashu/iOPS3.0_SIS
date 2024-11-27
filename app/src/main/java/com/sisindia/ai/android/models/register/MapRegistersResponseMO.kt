package com.sisindia.ai.android.models.register

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse

/**
 * Created by Ashu Rajput on 10/23/2020.
 */
data class MapRegistersResponseMO(
    @SerializedName("data")
    var mapRegistersData: MapRegistersDataMO? = null) : BaseNetworkResponse()

data class MapRegistersDataMO(
    @SerializedName("globleRegisters")
    var globalRegistersList: List<RegistersInfoMO>? = null,
    @SerializedName("sectoreRegisters")
    var sectorRegistersList: List<RegistersInfoMO>? = null,
    @SerializedName("clientRegisters")
    var clientRegistersList: List<RegistersInfoMO>? = null,
    @SerializedName("siteRegisters")
    var siteRegistersList: List<RegistersInfoMO>? = null)

data class RegistersInfoMO(
    @SerializedName("id")
    var mySitesData: Int? = null,
    @SerializedName("registerName")
    var registerName: String? = null,
    @SerializedName("scopeId")
    var scopeId: Int? = null,
    @SerializedName("scopeLevel")
    var scopeLevel: Int? = null,
    var isRegisterMapped: Boolean? = false)