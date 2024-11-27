package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class CustomerContactEntity {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public int id;

    @SerializedName("title")
    public String title;

    @SerializedName("role")
    public String role;

    @SerializedName("contactFullName")
    public String contactFullName;

    @SerializedName("contactEmailId")
    public String contactEmailId;

    @SerializedName("contactPhoneNo")
    public String contactPhoneNo;

    @SerializedName("customerId")
    public int customerId;

    @SerializedName("customerMobileNo")
    public String customerMobileNo;

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

    @SerializedName("isSynced")
    public int isSynced = 1;

    @Ignore
    public boolean isChecked;

    public CustomerContactEntity() {
    }

    @Ignore
    public CustomerContactEntity(int id) {
        this.id = id;
    }
}

