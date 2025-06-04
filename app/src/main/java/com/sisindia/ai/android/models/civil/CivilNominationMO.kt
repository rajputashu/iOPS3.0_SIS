package com.sisindia.ai.android.models.civil

import com.google.gson.annotations.SerializedName

data class CivilNominationMO(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("district")
    val district: String? = null,
    @SerializedName("nominationCount")
    val nomination: Int? = null
)
