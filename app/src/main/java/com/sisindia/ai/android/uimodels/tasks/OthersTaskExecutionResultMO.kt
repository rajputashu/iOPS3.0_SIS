package com.sisindia.ai.android.uimodels.tasks

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ashu Rajput on 4/24/2020.
 */
data class OthersTaskExecutionResultMO(
    @Expose
    @SerializedName("OtherActivity")
    var otherActivity: OtherActivity? = null
)

data class OtherActivity(
    @Expose
    @SerializedName("otherGUID")
    var otherGUID: String = "",

    @Expose
    @SerializedName("description")
    var description: String? = null,

    @Expose
    @SerializedName("reasonId")
    var reasonLookUpIdentifier: Int = 0
)