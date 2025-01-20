package com.sisindia.ai.android.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

/**
 * Created by Ashu Rajput on 13/01/2025.
 */
@Parcel
@Entity
data class NudgesMasterEntity(

    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("name")
    var title: String = "",

    @SerializedName("question")
    var jsonData: String? = null,

    @SerializedName("isActive")
    var isActive: Boolean = true)