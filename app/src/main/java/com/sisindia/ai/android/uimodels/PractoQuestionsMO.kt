package com.sisindia.ai.android.uimodels

import com.google.gson.annotations.SerializedName

/**
 * Created by Ashu Rajput on 01-08-2023
 */
data class PractoQuestionsMO(
    @SerializedName("isAlreadyInstalled")
    val isAlreadyInstalled: Boolean,
    @SerializedName("isInstalledByAO")
    val isInstalledByAO: Boolean,
    @SerializedName("remarks")
    val remarks: String = "")
