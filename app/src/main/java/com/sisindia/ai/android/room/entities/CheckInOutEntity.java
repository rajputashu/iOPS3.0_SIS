package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.commons.CheckInStatus;

import org.parceler.Parcel;

/**
 * Created by Ashu Rajput on 08/30/2024.
 */
@Parcel
@Entity
public class CheckInOutEntity {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public int id;

    @SerializedName("taskId")
    public int taskId;

    @SerializedName("checkInStatus")
    public int checkInStatus = CheckInStatus.DEFAULT.getStatus();

    @SerializedName("checkInDateTime")
    public String checkInDateTime;

    @SerializedName("checkInQrCode")
    public String checkInQrCode = "";

    @SerializedName("checkInSkipReason")
    public String checkInSkipReason;

    @SerializedName("checkOutDateTime")
    public String checkOutDateTime;

    @SerializedName("checkOutQrCode")
    public String checkOutQrCode = "";

    @SerializedName("checkOutSkipReason")
    public String checkOutSkipReason;

    public CheckInOutEntity() {

    }
}
