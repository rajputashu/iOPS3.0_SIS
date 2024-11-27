package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class WageCenterEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("siteId")
    public int siteId;

    @SerializedName("wageCenterCode")
    public String wageCenterCode;

    @SerializedName("category")
    public int category;

    @SerializedName("wageRate")
    public long wageRate;

    @SerializedName("isDefault")
    public boolean isDefault;

    @SerializedName("isActive")
    public boolean isActive;

    public WageCenterEntity() {
    }
}


