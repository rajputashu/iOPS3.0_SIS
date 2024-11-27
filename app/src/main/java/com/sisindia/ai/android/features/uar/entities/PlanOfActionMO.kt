package com.sisindia.ai.android.features.uar.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 4/3/2020.
 */
@Parcelize
data class PlanOfActionMO(
    val poaName: String? = null,
    val poaAssignedTo: String? = null,
    val poaCategory: String? = null) : Parcelable