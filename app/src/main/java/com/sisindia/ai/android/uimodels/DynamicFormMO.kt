package com.sisindia.ai.android.uimodels

/**
 * Created by Ashu_Rajput on 6/27/2021.
 */
data class DynamicFormMO(
    val id: Int,
    val dynamicTaskTypeId: String? = null,
    val form: String? = null,
    val taskName: String
//    , val taskCode: String
)