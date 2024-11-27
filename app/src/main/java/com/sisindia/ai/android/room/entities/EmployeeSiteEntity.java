package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class EmployeeSiteEntity {

    @PrimaryKey
    @SerializedName("employeeId")
    public int employeeId;

    @SerializedName("employeeName")
    public String employeeName;

    @SerializedName("employeeNo")
    public String employeeNo;

    @SerializedName("phone")
    public String phone;

    @SerializedName("emailId")
    public String emailId;

    @SerializedName("deployedDate")
    public String deployedDate;

    @SerializedName("profilePhotoUrl")
    public String profilePhotoUrl;

    @SerializedName("siteId")
    public int siteId;

    @SerializedName("rankId")
    public int rankId;

    @SerializedName("branchId")
    public int branchId;

    @SerializedName("isActive")
    public boolean isActive;

    @SerializedName("inBranch")
    public boolean inBranch;

    public EmployeeSiteEntity() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EmployeeSiteEntity)) return false;
        EmployeeSiteEntity it = (EmployeeSiteEntity) obj;
        return employeeNo.equalsIgnoreCase(it.employeeNo);
    }
}
