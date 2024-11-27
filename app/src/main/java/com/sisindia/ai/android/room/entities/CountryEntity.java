package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class CountryEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("countryName")
    public String countryName;

    @SerializedName("countryCode")
    public String countryCode;

    @SerializedName("phoneCode")
    public String phoneCode;

    @SerializedName("createdDateTime")
    public String createdDateTime;

    @SerializedName("updatedDateTime")
    public String updatedDateTime;

    @SerializedName("createdBy")
    public int createdBy;

    @SerializedName("updatedBy")
    public int updatedBy;


    public CountryEntity() {
    }
}
