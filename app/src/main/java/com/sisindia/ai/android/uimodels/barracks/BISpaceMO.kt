package com.sisindia.ai.android.uimodels.barracks

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 4/22/2020.
 */
@Parcelize
data class BISpaceMO(
    @Expose
    var barrackSpace: String? = null,
    @Expose
    var waterSupply: String? = null,
    @Expose
    var messing: String? = null,
    @Expose
    var messBoyProvided: String? = null,
    @Expose
    var utensils: String? = null,
    @Expose
    var recreationFacility: String? = null,
    @Expose
    var guardPerToiletRemarks: String? = null) : Parcelable