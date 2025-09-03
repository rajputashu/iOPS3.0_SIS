package com.sisindia.ai.android.models.poa

import com.droidcommons.preference.Prefs
import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.constants.PrefConstants
import org.threeten.bp.LocalDateTime

/**
 * Created by Ashu Rajput on 4/27/2020.
 */

//typealias id = Int
class AddImprovementPlanBodyMO(
    @SerializedName("planType")
    var planType: Int? = null,
    @SerializedName("categoryId")
    var categoryId: Int? = null,
    @SerializedName("actionPlanId")
    var actionPlanId: Int? = null,
    @SerializedName("siteId")
    val siteId: Int = Prefs.getInt(PrefConstants.CURRENT_SITE, 0),
    @SerializedName("targetCompletionDate")
    var targetCompletionDate: String = "",
    @SerializedName("description")
    var remarks: String = "",
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("raisedDateTime")
    val raisedDateTime: String = LocalDateTime.now().toString(),
    @SerializedName("assignedDateTime")
    val assignedDateTime: String = LocalDateTime.now().toString(),
    @SerializedName("assignedTo")
    var assignedTo: Int = Prefs.getInt(PrefConstants.AREA_INSPECTOR_ID, 0),
    @SerializedName("assignedToEmployeeName")
    var assignedToEmployeeName: String = "",
    @SerializedName("assignedToEmployeeNo")
    var assignedToEmployeeNo: String = "")