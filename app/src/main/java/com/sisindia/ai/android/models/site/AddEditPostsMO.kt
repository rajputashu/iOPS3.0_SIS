package com.sisindia.ai.android.models.site

import com.google.gson.annotations.SerializedName

/**
 * Created by Ashu Rajput on 3/27/2020.
 */
data class AddEditPostsMO(

    @SerializedName("Id")
    var id: Int,

    @SerializedName("PostName")
    val postName: String? = null,

    @SerializedName("DutyPostCode")
    val dutyPostCode: String? = null,

    @SerializedName("Description")
    val description: String? = null,

    @SerializedName("Qrenabled")
    val qrEnabled: Boolean = false,

    @SerializedName("IsActive")
    val isActive: Boolean = true,

    @SerializedName("IsMainPost")
    val isMainPost: Boolean = false,

    @SerializedName("SiteId")
    val siteId: Int,

    @SerializedName("isCreated")
    val isCreated: Int,

    @SerializedName("postGeoPointString")
    val postGeoPointString: String,

    @SerializedName("spiEnabled")
    val spiEnabled: Boolean = false)