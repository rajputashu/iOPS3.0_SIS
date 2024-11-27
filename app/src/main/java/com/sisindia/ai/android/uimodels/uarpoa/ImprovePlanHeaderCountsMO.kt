package com.sisindia.ai.android.uimodels.uarpoa

import org.parceler.Parcel

/**
 * Created by Ashu Rajput on 12/21/2020.
 */
@Parcel
data class ImprovePlanHeaderCountsMO(
    val sitesWithPoa: Int = 0,
    val pendingPlanPoa: Int = 0,
    val completedPlanPoa: Int = 0)