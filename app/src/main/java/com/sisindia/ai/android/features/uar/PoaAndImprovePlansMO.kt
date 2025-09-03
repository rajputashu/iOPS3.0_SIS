package com.sisindia.ai.android.features.uar

data class PoaAndImprovePlansMO(
    val id: Int,
    val planType: String? = null,
    val category: String? = null,
    val actionPlanName: String? = null,
    val assignedToEmployeeName: String? = null,
    val assignedDateTime: String? = null,
    val closedDateTime: String? = null,
    val description: String? = null,
    val status: Int
)
