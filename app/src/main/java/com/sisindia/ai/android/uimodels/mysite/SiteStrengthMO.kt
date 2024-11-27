package com.sisindia.ai.android.uimodels.mysite

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 4/18/2020.
 */
@Parcelize
data class SiteStrengthMO(
    var id: Int = 0,
    var authorizedStrength: Int? = 0,
    var actualStrength: Int? = 0,
    var deficience: Int? = 0,
    var grade: String? = null) : Parcelable