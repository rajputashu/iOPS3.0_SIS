package com.sisindia.ai.android.features.dynamictask

import com.google.gson.annotations.SerializedName

/**
 * Created by Ashu_Rajput on 6/8/2021.
 */
data class DynamicTaskParser(
    @SerializedName("Controls")
    var controllerList: List<MainControlMO>? = null)

data class MainControlMO(
    @SerializedName("contentType")
    var contentType: String? = null,
    @SerializedName("ControlID")
    var ControlID: String? = null,
    @SerializedName("data")
    var data: ControllerData? = null)

data class ControllerData(
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("innertitle")
    var hint: String? = null,
    @SerializedName("Legend")
    var legend: String? = null,
    @SerializedName("isBold")
    var isBold: String? = null,
    @SerializedName("Align")
    var align: String? = null,
    @SerializedName("count")
    var count: String? = null,
    @SerializedName("color")
    var color: String? = null,
    @SerializedName("maxsize")
    var maxsize: Int? = null,
    @SerializedName("maxduration")
    var maxduration: Int? = null,
    @SerializedName("isManditory")
    var isMandatory: String? = "false",
    @SerializedName("datavalue")
    var dataValue: ArrayList<String>? = null,
    @SerializedName("GPS")
    var gps: Boolean? = null)
