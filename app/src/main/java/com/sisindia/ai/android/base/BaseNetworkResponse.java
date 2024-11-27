package com.sisindia.ai.android.base;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class BaseNetworkResponse {

    @SerializedName("statusCode")
    public int statusCode;

    @SerializedName("statusMessage")
    public String statusMessage;

    public BaseNetworkResponse() {
    }
}
