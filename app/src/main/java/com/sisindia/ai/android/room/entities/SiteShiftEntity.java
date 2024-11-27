package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class SiteShiftEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("shiftName")
    public String shiftName;

    @SerializedName("description")
    public String description;

    @SerializedName("shiftStartTimeMS")
    public long shiftStartTimeMS;

    @SerializedName("shiftEndTimeMS")
    public long shiftEndTimeMS;

    @SerializedName("shiftCode")
    public String shiftCode;

    @SerializedName("weeklyOff")
    public int weeklyOff;

    @SerializedName("siteId")
    public int siteId;

    public SiteShiftEntity() {
    }
}

