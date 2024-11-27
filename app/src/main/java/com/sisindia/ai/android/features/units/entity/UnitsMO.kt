package com.sisindia.ai.android.features.units.entity


/**
 * Created by Ashu Rajput on 3/20/2020.
 */
data class UnitsMO(
    val unitName: String? = null,
    val unitCode: String? = null,
    val isUnitTagged: Boolean = false,
    val upcomingActivity: String? = null,
    val upcomingActivityDetails: UnitsUpcomingDetailsMO? = null,
    var isExpandable: Boolean = false)