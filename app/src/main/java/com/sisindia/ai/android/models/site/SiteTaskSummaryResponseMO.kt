package com.sisindia.ai.android.models.site

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse

/**
 * Created by Ashu Rajput on 11/17/2020.
 */
data class SiteTaskSummaryResponseMO(
    @SerializedName("data")
    var taskSummaryData: List<TaskSummaryDataMO>? = null) : BaseNetworkResponse()

data class TaskSummaryDataMO(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("siteCode")
    var siteCode: String? = null,
    @SerializedName("siteName")
    var siteName: String? = null,
    @SerializedName("dcTarget")
    var dcTarget: Int? = null,
    @SerializedName("dcActual")
    var dcActual: Int? = null,
    @SerializedName("ncTarget")
    var ncTarget: Int? = null,
    @SerializedName("ncActual")
    var ncActual: Int? = null,
    @SerializedName("chsDone")
    var chsDone: Int? = null,
    @SerializedName("lastDCDone")
    var lastDCDone: String? = null,
    @SerializedName("lastNCDone")
    var lastNCDone: String? = null)