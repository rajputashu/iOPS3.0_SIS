package com.sisindia.ai.android.uimodels.uarpoa

import android.os.Parcelable
import androidx.room.Ignore
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 4/8/2020.
 */
@Parcelize
data class POADetailsMO(
    var poaId: Int = 0,
    var Name: String? = null,
    var Category: String? = null,
    var AssignedTo: String? = null,
    var targetCompletionDate: String? = null,
    var poAStatus: Int = 0,
    @Ignore
    var UnitName: String? = null,
    @Ignore
    var pendingPoaCount: String? = null,
    @Ignore
    var totalPoaCount: String? = null) : Parcelable