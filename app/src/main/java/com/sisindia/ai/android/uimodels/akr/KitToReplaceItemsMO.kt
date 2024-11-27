package com.sisindia.ai.android.uimodels.akr

import android.os.Parcelable
import androidx.room.Ignore
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 4/17/2020.
 */
@Parcelize
data class KitToReplaceItemsMO(
    var id: Int = 0,
    var kitId: Int? = 0,
    var guardName: String? = null,
    var siteName: String? = null,
    var kitItemId: Int? = 0,
    var kitItemName: String? = null,
    var itemSizeName: String? = null,
    var otpTypeId: Int? = null,
    var recipientPhoneNo: String? = null,
    @Ignore
    var isSelected: Boolean = true) : Parcelable