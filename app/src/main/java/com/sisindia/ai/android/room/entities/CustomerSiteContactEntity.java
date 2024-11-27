package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class CustomerSiteContactEntity {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public int id;

    @SerializedName("siteId")
    public int siteId;

    @SerializedName("customerContactId")
    public int customerContactId;

    @SerializedName("isActive")
    public boolean isActive;

    @SerializedName("siteContactConfig")
    public String siteContactConfig;

    public CustomerSiteContactEntity() {
    }
}


