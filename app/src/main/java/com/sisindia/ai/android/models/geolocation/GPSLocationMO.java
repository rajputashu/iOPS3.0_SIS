package com.sisindia.ai.android.models.geolocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Ashu Rajput on 5/5/2020.
 */
@Parcel
public class GPSLocationMO {

    @Expose(serialize = false)
    public int pingId;

    @Expose
    @SerializedName("geoLocation")
    public String geoLocation;

    @Expose
    @SerializedName("capturedDateTime")
    public String capturedDateTime;

    @Expose
    @SerializedName("batteryPercentage")
    public int batteryPercentage;

    public GPSLocationMO() {

    }
}
