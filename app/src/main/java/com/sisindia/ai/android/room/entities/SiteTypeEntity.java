package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class SiteTypeEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String siteTypeName;

    @SerializedName("description")
    public String description;

    @SerializedName("siteTypeConfig")
    public String siteTypeConfig;

    @SerializedName("companyId")
    public int companyId;

    @SerializedName("isActive")
    public boolean isActive;

    public SiteTypeEntity() {
    }
}

