package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class RegionEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("regionName")
    public String regionName;

    @SerializedName("regionCode")
    public String regionCode;

    @SerializedName("zoneId")
    public int zoneId;

    @SerializedName("isActive")
    public boolean isActive;

    @SerializedName("createdDateTime")
    public String createdDateTime;

    @SerializedName("updatedDatetime")
    public String updatedDatetime;

    @SerializedName("companyId")
    public int companyId;

    public RegionEntity() {
    }
}

