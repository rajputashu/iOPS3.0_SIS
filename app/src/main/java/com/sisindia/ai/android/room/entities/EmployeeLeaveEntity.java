package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class EmployeeLeaveEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("employeeId")
    public int employeeId;

    @SerializedName("leaveDate")
    public String leaveDate;


    public EmployeeLeaveEntity() {
    }
}

