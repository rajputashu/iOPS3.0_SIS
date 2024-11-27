package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.threeten.bp.LocalDateTime;

/**
 * Created by Ashu Rajput on 4/28/2020.
 */
@Parcel
@Entity
public class GeoLocationEntity {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public int id;

    @SerializedName("geoLongitude")
    public double geoLatitude;

    @SerializedName("geoLongitude")
    public double geoLongitude;

    @SerializedName("batteryPercentage")
    public int batteryPercentage;

    @SerializedName("capturedDateTime")
    public String capturedDateTime;

    @SerializedName("isSynced")
    public boolean isSynced;

    public GeoLocationEntity() {

    }

    @Ignore
    public GeoLocationEntity(double geoLatitude, double geoLongitude, int batteryPercentage) {
        this.geoLatitude = geoLatitude;
        this.geoLongitude = geoLongitude;
        this.batteryPercentage = batteryPercentage;
        this.capturedDateTime = LocalDateTime.now().toString();
        this.isSynced = false;
    }
}
