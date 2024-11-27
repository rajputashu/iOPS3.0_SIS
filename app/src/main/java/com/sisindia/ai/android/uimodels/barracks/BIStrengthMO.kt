package com.sisindia.ai.android.uimodels.barracks

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 4/22/2020.
 */
@Parcelize
data class BIStrengthMO(
    @Expose
    var capacity: String? = null,
    @Expose
    var vacant: String? = null,
    @Expose
    var lastInspection: String? = null,
    @Expose
    var currentStrength: String? = null,
    @Expose
    var leaveSick: String? = null) : Parcelable