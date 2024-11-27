package com.sisindia.ai.android.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class GeoPointItem {

    @SerializedName("longitude")
    public double longitude;

    @SerializedName("latitude")
    public double latitude;

    public GeoPointItem() {
    }
}
