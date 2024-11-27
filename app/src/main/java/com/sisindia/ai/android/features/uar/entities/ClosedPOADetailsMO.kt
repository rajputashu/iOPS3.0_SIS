package com.sisindia.ai.android.features.uar.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 4/4/2020.
 */
@Parcelize
data class ClosedPOADetailsMO(
    val poaUnitName: String? = null,
    val poaName: String? = null,
    val poaPendingCount: String? = null,
    val poaTotalCount: String? = null) : Parcelable