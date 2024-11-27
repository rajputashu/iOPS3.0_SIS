package com.sisindia.ai.android.features.rotacompliance.entities

/**
 * Created by Ashu Rajput on 3/6/2020.
 */
data class DayWeekMonthMO(val chartHeader: String? = null,
    val chartPercentage: String? = null,
    val taskDetailsList: ArrayList<ComplianceTaskDetailsMO>? = null,
    val chartTypeId: Int = 0)