package com.sisindia.ai.android.features.nudges

import com.google.gson.annotations.SerializedName

data class ChildControllerMO(

    @SerializedName("OptionName")
    var optionName: String? = null,

    @SerializedName("Dependant")
    var dependantControllerName: String? = null,

    @SerializedName("DependantTitle")
    var dependantTitle: String? = null,

    @SerializedName("DependantValue")
    var dependantValue: ArrayList<String>? = null)
