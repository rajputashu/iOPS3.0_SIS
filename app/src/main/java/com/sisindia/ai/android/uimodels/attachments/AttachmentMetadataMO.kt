package com.sisindia.ai.android.uimodels.attachments

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

/**
 * Created by Ashu Rajput on 5/11/2020.
 */

@Parcel
data class AttachmentMetadataMO(
    @SerializedName("metadataName")
    var metadataName: String? = null,

    @SerializedName("storagePath")
    var storagePath: String? = null,

    @SerializedName("metadataJsonObject")
    var metadataJsonObject: MetadataJsonObjectMO? = null)

@Parcel
data class MetadataJsonObjectMO(

    @Expose
    @SerializedName("attachmentTypeId")
    var attachmentTypeId: String? = null,

    @Expose
    @SerializedName("attachmentSourceTypeId")
    var attachmentSourceTypeId: String? = null,

    @Expose
    @SerializedName("uuid")
    var uuid: String? = null,

    @Expose
    @SerializedName("fileName")
    var fileName: String? = null,

    @Expose
    @SerializedName("fileSize")
    var fileSize: String? = null,

    @Expose
    @SerializedName("companyId")
    var companyId: String? = null,

    @Expose
    @SerializedName("appTypeId")
    var appTypeId: String? = null
)