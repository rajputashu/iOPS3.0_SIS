package com.sisindia.ai.android.models.geolocation

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ashu Rajput on 4/28/2020.
 */
data class GeoLocationApiBodyMO(
    @Expose
    @SerializedName("gpsLocation")
    var gpsLocation: List<GPSLocationMO>? = null)

/*
data class GPSLocationMO(

    @SerializedName("rowId")
    var ID: Int? = null,
    @Expose
    @SerializedName("geoLocation")
    var geoLocation: String? = null,
    @Expose
    @SerializedName("capturedDateTime")
    var capturedDateTime: String? = null,
    @Expose
    @SerializedName("batteryPercentage")
    var batteryPercentage: Int? = null)*/
