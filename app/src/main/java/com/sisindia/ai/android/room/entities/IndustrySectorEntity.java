package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class IndustrySectorEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("sectorName")
    public String sectorName;

    @SerializedName("description")
    public String description;

    @SerializedName("sectorConfiguration")
    public String sectorConfiguration;


    public IndustrySectorEntity() {
    }
}

