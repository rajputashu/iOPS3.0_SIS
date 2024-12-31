package com.sisindia.ai.android.features.nudges

import com.google.gson.annotations.SerializedName

/**
 * Created by Ashu Rajput on 04-11-2024
 */
data class DynamicTaskParserV2(
    @SerializedName("ControlName")
    var contentType: String? = null,
    @SerializedName("ControlID")
    var ControlID: String? = null,
    @SerializedName("ControlId")
    var ControlId: Int = 0,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("innertitle")
    var hint: String? = null,
    /*@SerializedName("Legend")
    var legend: String? = null,
    @SerializedName("isBold")
    var isBold: String? = null,
    @SerializedName("Align")
    var align: String? = null,*/
    @SerializedName("count")
    var count: String? = null,
    /*@SerializedName("color")
    var color: String? = null,*/
    @SerializedName("maxsize")
    var maxsize: Int? = null,
    @SerializedName("maxduration")
    var maxduration: Int? = null,
    @SerializedName("isManditory")
    var isMandatory: String? = "false",
    @SerializedName("datavalue")
    var dataValue: ArrayList<String>? = null,
    @SerializedName("GPS")
    var gps: Boolean? = null,
    @SerializedName("image")
    var imageUrl: String? = null,
    @SerializedName("APIName")
    var routeName: String = "",
    /*@SerializedName("Parameters")
    var parameter: String = "",*/
    @SerializedName("HeaderPhoto")
    var headerPhoto: String = "",
    @SerializedName("HeaderName")
    var headerName: String = "",
    @SerializedName("HeaderRank")
    var headerRank: String = "",
    @SerializedName("childControllers")
    var childControllers: ArrayList<ChildControllerMO>? = null
)