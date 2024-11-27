package com.sisindia.ai.android.uimodels.tasks

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 4/28/2020.
 */
@Parcelize
data class CacheStrengthMO(
    var id: Int? = null,
    var postId: Int? = null,
    var authorizedStrength: Int = 0,
    var grade: String? = null,
    var actualStrength: Int = 0) : Parcelable