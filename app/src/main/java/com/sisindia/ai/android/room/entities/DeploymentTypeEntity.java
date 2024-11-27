package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class DeploymentTypeEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("deploymentTypeName")
    public String deploymentTypeName;

    @SerializedName("sectorId")
    public int sectorId;

    @SerializedName("companyId")
    public int companyId;

    @SerializedName("sectorName")
    public String sectorName;

    @SerializedName("isActive")
    public boolean isActive;

    public DeploymentTypeEntity() {
    }
}
