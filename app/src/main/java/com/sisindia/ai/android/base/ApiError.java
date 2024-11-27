package com.sisindia.ai.android.base;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class ApiError {

    @SerializedName("statusCode")
    public int statusCode;

    @SerializedName("statusMessage")
    public String statusMessage;

    @SerializedName("error")
    public String error;

    @SerializedName("error_description")
    public String description;

    public ApiError() {
    }
}
