package com.sisindia.ai.android.models.civil

import com.droidcommons.preference.Prefs
import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.constants.PrefConstants
import org.threeten.bp.LocalDateTime

data class AddNominationBodyMO(
    @SerializedName("stateId")
    var stateId: Int? = null,

    @SerializedName("districtId")
    var districtId: Int? = null,

    @SerializedName("siteId")
    var siteId: Int? = null,

    @SerializedName("employeeId")
    var employeeId: Int? = null,

    @SerializedName("image")
    var image: String? = null,

    @SerializedName("lat")
    var lat: Double = Prefs.getDouble(PrefConstants.LATITUDE),

    @SerializedName("long")
    var long: Double = Prefs.getDouble(PrefConstants.LONGITUDE),

    @SerializedName("createdDateTime")
    var createdDateTime: String? = LocalDateTime.now().toString(),

    @SerializedName("updatedDateTime")
    var updatedDateTime: String? = LocalDateTime.now().toString(),
)
