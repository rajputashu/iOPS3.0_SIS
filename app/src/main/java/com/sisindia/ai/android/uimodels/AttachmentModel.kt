package com.sisindia.ai.android.uimodels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AttachmentModel(var fileName: String?,
    var storagePath: String?,
    var serverId: Int?,
    var siteCode: String?,
    var taskType: String?) :
    Parcelable