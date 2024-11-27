package com.sisindia.ai.android.models.recruit

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse

/**
 * Created by Ashu Rajput on 5/20/2020.
 */
data class AddedRecruitmentApiResponse(
    @SerializedName("data")
    val data: AddedRecruitResultDataMO? = null
) : BaseNetworkResponse()

data class AddedRecruitResultDataMO(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("uuid")
    val receivedUUID: String? = null
)