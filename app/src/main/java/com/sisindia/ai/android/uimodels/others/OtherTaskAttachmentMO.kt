package com.sisindia.ai.android.uimodels.others

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 6/4/2020.
 */
@Parcelize
data class OtherTaskAttachmentMO(var fileName: String,
    var storagePath: String,
    var taskId: String,
    val siteId: String) : Parcelable