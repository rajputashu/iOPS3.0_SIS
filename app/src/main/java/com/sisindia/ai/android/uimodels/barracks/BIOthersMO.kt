package com.sisindia.ai.android.uimodels.barracks

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 4/22/2020.
 */

@Parcelize
data class BIOthersMO(
    @Expose
    var remarks: String? = null,
    @Expose
    var beddingUri: String? = null,
    @Expose
    var kitUri: String? = null,
    @Expose
    var messUri: String? = null,
    @Expose
    var barrackOutsideUri: String? = null,
    @Expose
    var beddingGuid: String = "",
    @Expose
    var kitGuid: String = "",
    @Expose
    var messGuid: String = "",
    @Expose
    var outsideGuid: String = "") : Parcelable