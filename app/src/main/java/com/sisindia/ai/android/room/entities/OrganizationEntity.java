package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class OrganizationEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("organizationName")
    public String organizationName;

    @SerializedName("description")
    public String description;

    @SerializedName("organizationCode")
    public String organizationCode;

    @SerializedName("organizationHQGeoPoint")
    public int organizationHQGeoPoint;

    @SerializedName("isActive")
    public boolean isActive;


    public OrganizationEntity() {
    }
}
