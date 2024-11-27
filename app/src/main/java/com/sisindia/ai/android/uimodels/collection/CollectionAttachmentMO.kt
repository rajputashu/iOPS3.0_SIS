package com.sisindia.ai.android.uimodels.collection

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 6/2/2020.
 */
@Parcelize
data class CollectionAttachmentMO(var fileName: String, var storagePath: String) : Parcelable