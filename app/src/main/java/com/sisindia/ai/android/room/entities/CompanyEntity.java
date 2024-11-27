package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class CompanyEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("companyName")
    public String companyName;

    @SerializedName("organizationId")
    public int organizationId;

    @SerializedName("companyAddress")
    public String companyAddress;

    public CompanyEntity() {
    }
}
