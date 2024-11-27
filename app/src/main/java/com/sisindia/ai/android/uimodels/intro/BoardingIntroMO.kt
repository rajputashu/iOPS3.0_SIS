package com.sisindia.ai.android.uimodels.intro

import com.google.gson.annotations.SerializedName

/**
 * Created by Ashu Rajput on 11/27/2020.
 */
data class BoardingIntroMO(
    @SerializedName("Login")
    var loginList: List<ModuleInfoMO>? = null)

data class ModuleInfoMO(
    @SerializedName("moduleName")
    var moduleName: String = "",
    @SerializedName("header")
    var header: String = "",
    @SerializedName("message")
    var message: String = "",
    @SerializedName("imageName")
    var imageName: String = "")