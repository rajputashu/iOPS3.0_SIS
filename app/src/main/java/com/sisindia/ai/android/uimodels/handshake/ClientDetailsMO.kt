package com.sisindia.ai.android.uimodels.handshake

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 4/10/2020.
 */
@Parcelize
data class ClientDetailsMO(
    var siteName: String,
    var title: String? = null,
    var clientId: Int? = 0,
    var clientName: String? = null,
    var clientNo: String? = null) : Parcelable