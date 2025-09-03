package com.sisindia.ai.android.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

/**
 * Created by Ashu Rajput on 11/05/2024.
 */
@Parcel
@Entity
data class SiteRiskReasonsEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    @SerializedName("siteRiskId")
    var siteRiskId: Int? = null,

    @SerializedName("riskCategoryId")
    var riskCategoryId: Int? = null,

    @SerializedName("riskReasonId")
    var riskReasonId: Int? = null
)