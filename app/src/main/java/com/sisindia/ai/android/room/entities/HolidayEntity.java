package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class HolidayEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("holidayDate")
    public String holidayDate;

    @SerializedName("holidayDescription")
    public String holidayDescription;

    @SerializedName("isRegionalHoliday")
    public boolean isRegionalHoliday;

    @SerializedName("isActive")
    public boolean isActive;

    @SerializedName("companyId")
    public int companyId;

    public HolidayEntity() {
    }
}
