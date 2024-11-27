package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Ashu Rajput on 4/14/2020.
 */
@Parcel
@Entity
public class ContractsEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("customerId")
    public int customerId;

    @SerializedName("contractCode")
    public String contractCode;

    @SerializedName("contractStartDate")
    public String contractStartDate;

    @SerializedName("contractEndDate")
    public String contractEndDate;

    @SerializedName("contractStatus")
    public int contractStatus;

    public ContractsEntity() {

    }
}
