package com.sisindia.ai.android.features.dynamictask.models

/**
 * Created by Ashu Rajput on 04-11-2024
 */
data class DynamicRatingMO(
    var controllerId: String? = null,
    var controllerName: String? = null,
    var isMandatory: Boolean = false,
    val label: String? = null,
    var rating: Int = 0)