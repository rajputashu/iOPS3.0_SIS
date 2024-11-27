package com.sisindia.ai.android.uimodels.akr

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AKRAttachmentMO(var fileName: String, var storagePath: String) : Parcelable