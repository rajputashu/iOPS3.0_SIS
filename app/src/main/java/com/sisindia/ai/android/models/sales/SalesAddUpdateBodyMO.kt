package com.sisindia.ai.android.models.sales

import com.google.gson.annotations.SerializedName

/**
 * Created by Ashu Rajput on 5/4/2021.
 */
data class SalesAddBodyMO(
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("employeeName")
    var employeeName: String? = null,
    @SerializedName("dateOfRecommendation")
    var dateOfRecommendation: String? = null,
    @SerializedName("dateOfReporting")
    var dateOfReporting: String? = null,
    @SerializedName("status")
    var status: Int? = 1,
    @SerializedName("referBy")
    var referBy: String? = null,
    @SerializedName("sector")
    var sector: String? = null,
    @SerializedName("leadGivenTo")
    var leadGivenTo: String? = null,
    @SerializedName("siteId")
    var siteId: Int? = 0,
    @SerializedName("siteCode")
    var siteCode: String? = null,
    @SerializedName("siteName")
    var siteName: String? = null,
    @SerializedName("remarks")
    val remarks: String = ""

    /* @SerializedName("employeeId")
     val employeeId: Int? = 0,
     @SerializedName("dateOfRaising")
     var dateOfRaising: String? = null,
     @SerializedName("raisingReportedOn")
     var raisingReportedOn: String? = null,
     @SerializedName("noOfGuards")
     val noOfGuards: Int? = 0,
     @SerializedName("approvedOn")
     val approvedOn: String? = null,
     @SerializedName("approvedName")
     val approvedName: String? = null,
     @SerializedName("approvedById")
     val approvedById: Int? = 0,*/
)

/*
data class SalesUpdateBodyMO(
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("status")
    var status: Int? = 2,
    @SerializedName("raisedUnitCode")
    var raisedUnitCode: String? = null)*/
