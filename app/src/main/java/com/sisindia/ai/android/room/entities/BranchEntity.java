package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class BranchEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("branchName")
    public String branchName;

    @SerializedName("branchCode")
    public String branchCode;

    @SerializedName("regionId")
    public int regionId;

    @SerializedName("companyId")
    public int companyId;

    @SerializedName("gstin")
    public String gstin;

    @SerializedName("isCurrentBranch")
    public boolean isCurrentBranch;

    @Ignore
    @SerializedName("branchExtensionEntity")
    public BranchExtensionEntity branchExtensionEntity;

    public BranchEntity() {
    }
}

