package com.sisindia.ai.android.models.poa

import com.droidcommons.preference.Prefs
import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.constants.PrefConstants

/**
 * Created by Ashu Rajput on 4/27/2020.
 */
class AddPoaBodyMO(
    @SerializedName("siteRiskId")
    var siteRiskId: Int? = null,
    @SerializedName("siteId")
    val siteId: Int = Prefs.getInt(PrefConstants.CURRENT_SITE, 0),
    @SerializedName("poAReason")
    var poAReason: Int = 0,
    @SerializedName("atRiskActionPlan")
    var atRiskActionPlan: Int = 0,
    @SerializedName("assignedTo")
    var assignedTo: Int = 0,
    @SerializedName("targetCompletionDate")
    var targetCompletionDate: String = "",
//    @SerializedName("closureDateTime")
//    var closureDateTime: String = "",
    @SerializedName("poAStatus")
    val poAStatus: Int = 1)