package com.sisindia.ai.android.models

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.BuildConfig
import com.sisindia.ai.android.base.BaseNetworkResponse

/**
 * Created by Ashu Rajput on 1/19/2021.
 */
data class AppVersionResponseMO(
    @SerializedName("data")
    val appVersionData: AppVersionData? = null) : BaseNetworkResponse()

data class AppVersionData(
    @SerializedName("versionCode")
    val versionCode: Int = BuildConfig.VERSION_CODE,
    @SerializedName("versionNo")
    val versionNo: String? = null,
    @SerializedName("versionDescription")
    val versionDescription: String? = null,
    @SerializedName("apkFileName")
    val apkFileName: String? = null,
    @SerializedName("apkFileHostedUrl")
    val apkFileHostedUrl: String? = null,
    @SerializedName("publishedDateTime")
    val publishedDateTime: String? = null,
    @SerializedName("isForcedUpdate")
    val isForcedUpdate: Boolean = true)
