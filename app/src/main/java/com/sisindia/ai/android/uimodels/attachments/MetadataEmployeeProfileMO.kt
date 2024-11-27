package com.sisindia.ai.android.uimodels.attachments

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ashu Rajput on 5/13/2020.
 */
data class MetadataEmployeeProfileMO(
    @Expose
    @SerializedName("employeeId")
    var employeeId: String? = null
) : BaseAttachmentMetadata()