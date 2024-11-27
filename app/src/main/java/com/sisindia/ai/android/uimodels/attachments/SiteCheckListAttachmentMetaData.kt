package com.sisindia.ai.android.uimodels.attachments

import org.parceler.Parcel

/**
 * Created by Ashu Rajput on 2/11/2021.
 */
@Parcel
data class SiteCheckListAttachmentMetaData(
    //        @SerializedName("siteId")
    var siteId: Int? = null,

    //        @SerializedName("taskId")
    var taskId: Int? = null,

    //        @SerializedName("sequenceNo")
    var sequenceNo: Int? = 1) : BaseAttachmentMetadata()