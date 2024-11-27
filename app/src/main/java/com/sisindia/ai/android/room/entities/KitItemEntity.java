package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
@Entity
public class KitItemEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("itemName")
    public String itemName;

    @SerializedName("description")
    public String description;

    @SerializedName("itemCode")
    public String itemCode;

    @SerializedName("companyId")
    public int companyId;

    @SerializedName("isActive")
    public boolean isActive;

    @Ignore
    @SerializedName("kitItemSizes")
    public List<KitItemSizeEntity> kitItemSizes;


    public KitItemEntity() {
    }
}

