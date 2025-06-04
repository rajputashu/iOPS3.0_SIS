package com.sisindia.ai.android.models.civil

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse

/**
 * Created by Ashu Rajput on 06/03/2025.
 */
data class CivilNominationResponseMO(
    @SerializedName("data")
    val data: CivilNominationDataMO? = null) : BaseNetworkResponse()

data class CivilNominationDataMO(
    @SerializedName("recommended")
    val recommended: Int = 0,
    @SerializedName("miniSalesModel")
    val salesRefList: List<CivilNominationMO>? = null)

/*
@Parcelize
data class SalesReferenceMO(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("siteCode")
    val siteCode: String? = null,
    @SerializedName("raisingCode")
    val raisingCode: String? = null,
    @SerializedName("approvedOn")
    val approvedOn: String? = null,
    @SerializedName("creditMonth")
    val creditMonth: Int? = null,
    @SerializedName("creditYear")
    val creditYear: Int? = null,
    @SerializedName("dateOfRaising")
    val dateOfRaising: String? = null,
    @SerializedName("dateOfRecommendation")
    val dateOfRecommendation: String? = null,
    @SerializedName("dateOfReporting")
    val dateOfReporting: String? = null,
    @SerializedName("status")
    val status: Int? = null) : Parcelable*/
