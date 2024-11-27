package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Entity
@Parcel
public class SiteBillingCheckListEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("siteId")
    public int siteId;

    @SerializedName("billingChecklistId")
    public int billingChecklistId;

    @SerializedName("isActive")
    public boolean isActive;

    public SiteBillingCheckListEntity() {
    }
}
