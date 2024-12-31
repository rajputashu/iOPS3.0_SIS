package com.sisindia.ai.android.models.nudges

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDateTime

data class NudgeRequestBodyMO(
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("response")
    var response: String? =null,
    @SerializedName("responseUpdatedDateTime")
    var responseUpdatedDateTime: String? = LocalDateTime.now().toString(),
    @SerializedName("updatedDateTime")
    var updatedDateTime: String? = LocalDateTime.now().toString())