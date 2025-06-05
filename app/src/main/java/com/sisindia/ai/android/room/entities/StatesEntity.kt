package com.sisindia.ai.android.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
@Entity
data class StatesEntity(
    @PrimaryKey
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("name")
    var name: String? = null
)
