package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class BillCenterEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("siteId")
    public int siteId;

    @SerializedName("billCenterCode")
    public String billCenterCode;

    @SerializedName("category")
    public int category;

    @SerializedName("billRate")
    public long billRate;

    @SerializedName("isDefault")
    public boolean isDefault;

    @SerializedName("isActive")
    public boolean isActive;


    public BillCenterEntity() {
    }
}
