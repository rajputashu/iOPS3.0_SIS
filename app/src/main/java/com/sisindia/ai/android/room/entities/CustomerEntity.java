package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class CustomerEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("customerName")
    public String customerName;

    @SerializedName("description")
    public String description;

    @SerializedName("sectorId")
    public int sectorId;

    @SerializedName("customerCode")
    public String customerCode;

    @SerializedName("isActive")
    public boolean isActive;

    @SerializedName("createdDateTime")
    public String createdDateTime;

    @SerializedName("updatedDateTime")
    public String updatedDateTime;

    @SerializedName("createdBy")
    public int createdBy;

    @SerializedName("updatedBy")
    public int updatedBy;


    public CustomerEntity() {
    }
}

