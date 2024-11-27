package com.sisindia.ai.android.uimodels.akr

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu_Rajput on 9/3/2021.
 */
@Parcelize
data class KitRequestItemsMO(

    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("kitRequestId")
    var kitRequestId: Int = 0,

//    @SerializedName("employeeId")
//    var employeeId = 0

    @SerializedName("kitItemId")
    var kitItemId: Int = 0,

    @SerializedName("itemSizeCode")
    var itemSizeCode: String = "",

    @SerializedName("taskId")
    var taskId: Int = 0,

    @SerializedName("siteId")
    var siteId: Int = 0,

    @SerializedName("createdDateTime")
    var createdDateTime: String? = null,

    @SerializedName("updatedDateTime")
    var updatedDateTime: String? = null,

    @SerializedName("replaceStatus")
    var replaceStatus: Int = 0) : Parcelable