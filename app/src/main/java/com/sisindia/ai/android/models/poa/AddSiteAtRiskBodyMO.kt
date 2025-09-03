package com.sisindia.ai.android.models.poa

import com.droidcommons.preference.Prefs
import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.constants.PrefConstants
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

/**
 * Created by Ashu Rajput on 4/27/2020.
 */
class AddSiteAtRiskBodyMO(
    @SerializedName("siteId")
    var siteId: Int = Prefs.getInt(PrefConstants.CURRENT_SITE, 0),
    @SerializedName("riskMonth")
    val riskMonth: Int = LocalDate.now().monthValue,
    @SerializedName("riskYear")
    val riskYear: Int = LocalDate.now().year,
    @SerializedName("riskStatus")
    val riskStatus: Int = 1,
    @SerializedName("createdByAppId")
    val createdByAppId: Int = Prefs.getInt(PrefConstants.AREA_INSPECTOR_ID, 0),
    @SerializedName("finalClosureDate")
    val finalClosureDate: String = LocalDateTime.now().toString(),
    @SerializedName("remarks")
    var remarks: String = "Added site at risk",
    @SerializedName("siteRiskReasons")
    var siteRiskReasons: List<SiteRiskReasonsMO>? = null)

class SiteRiskReasonsMO(
    @SerializedName("riskCategoryId")
    var riskCategoryId: Int? = null,
    @SerializedName("riskReasonId")
    var riskReasonId: Int? = null)