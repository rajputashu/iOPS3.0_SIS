package com.sisindia.ai.android.models.mask

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.uimodels.attachments.BaseAttachmentMetadata

data class MaskAttachmentMetaDataMO(
    @SerializedName("employeeNo")
    var employeeNo: String? = null,
    @SerializedName("siteId")
    var siteId: Int? = null,
    @SerializedName("siteCode")
    var siteCode: String? = null
) : BaseAttachmentMetadata()