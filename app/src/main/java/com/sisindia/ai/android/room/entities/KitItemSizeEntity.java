package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class KitItemSizeEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("kitItemId")
    public int kitItemId;

    @SerializedName("itemSizeCode")
    public String itemSizeCode;

    @SerializedName("itemSizeName")
    public String itemSizeName;

    @SerializedName("itemSize")
    public float itemSize;

    @SerializedName("isActive")
    public boolean isActive;

    public KitItemSizeEntity() {
    }
}

