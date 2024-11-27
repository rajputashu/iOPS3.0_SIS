package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.models.GeoPointItem;

import org.parceler.Parcel;

@Parcel
@Entity
public class BarrackEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("barrackCode")
    public String barrackCode;

    @SerializedName("barrackAddress")
    public String barrackAddress;

    @SerializedName("branchId")
    public int branchId;

    @SerializedName("areaInspectorId")
    public int areaInspectorId;

    @SerializedName("barrackGeoPointString")
    public String barrackGeoPointString;

    @SerializedName("srNumber")
    public String srNumber;

    /*@Ignore
    @SerializedName("barrackGeoPoint")
    public GeoPointItem geoPoint;*/

    /*@SerializedName("Longitude")
    public double longitude;

    @SerializedName("Latitude")
    public double latitude;*/

    public BarrackEntity() {
    }
}