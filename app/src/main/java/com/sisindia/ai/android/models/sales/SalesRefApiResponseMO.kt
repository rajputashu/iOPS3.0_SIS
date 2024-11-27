package com.sisindia.ai.android.models.sales

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 4/30/2021.
 */
data class SalesRefApiResponseMO(
    @SerializedName("data")
    val data: SalesReferenceDataMO? = null) : BaseNetworkResponse()

data class SalesReferenceDataMO(
    @SerializedName("recommended")
    val recommended: Int = 0,
    @SerializedName("raised")
    val raised: Int = 0,
    @SerializedName("approved")
    val approved: Int = 0,
    @SerializedName("miniSalesModel")
    val salesRefList: List<SalesReferenceMO>? = null)

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
    val status: Int? = null) : Parcelable