package com.sisindia.ai.android.models

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse
import org.threeten.bp.LocalDateTime

/**
 * Created by Ashu Rajput on 12-10-2022
 */
data class EventsAPIResponseMO(
    @SerializedName("data")
    val data: List<EventsDataMO>? = null) : BaseNetworkResponse()

data class EventsDataMO(
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("eventName")
    val eventName: String? = null,
    @SerializedName("eventCode")
    val eventCode: String? = null,
    @SerializedName("startDateTime")
    val startDateTime: String? = LocalDateTime.now().toString(),
    @SerializedName("mobileUrl")
    val mobileUrl: String? = null,
    @SerializedName("mobileToken")
    val mobileToken: String? = null)
