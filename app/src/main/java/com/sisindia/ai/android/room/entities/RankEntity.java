package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class RankEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("rankName")
    public String rankName;

    @SerializedName("rankCode")
    public String rankCode;

    @SerializedName("canCarryArms")
    public boolean canCarryArms;

    @SerializedName("companyId")
    public int companyId;

    public RankEntity() {
    }
}
