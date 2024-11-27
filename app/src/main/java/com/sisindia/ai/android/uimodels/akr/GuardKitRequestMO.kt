package com.sisindia.ai.android.uimodels.akr

import android.os.Parcelable
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu_Rajput on 9/3/2021.
 */
@Parcelize
data class GuardKitRequestMO(

    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("requestorId")
    var requestorId: Int = 0,

//    @SerializedName("imageAttachmentGuid")
    @Ignore
    @SerializedName("photoId")
    var photoId: Int? = 0,

//    @SerializedName("signatureAttachmentGuid")
    @Ignore
    @SerializedName("signatureId")
    var signatureId: Int? = 0,

    @SerializedName("siteTaskId")
    var siteTaskId: Int = 0,

    @SerializedName("siteId")
    var siteId: Int = 0,

    @SerializedName("kitRequestGuid")
    var kitRequestGuid: String? = null,

    @SerializedName("serverId")
    var serverId: Int = 0,

    @SerializedName("isSync")
    var isSync: Boolean = false,

    @SerializedName("requestedOn")
    var requestedOn: String? = null,

    @SerializedName("requestStatus")
    var requestStatus: Int = 0,

    @Ignore
    @SerializedName("companyId")
    var companyId: Int = 1,

    @SerializedName("requestorRegno")
    var requestorRegno: String? = null,

    @Ignore
    @SerializedName("kitRequestItems")
    var kitRequestItems: List<KitRequestItemsMO>? = null) : Parcelable