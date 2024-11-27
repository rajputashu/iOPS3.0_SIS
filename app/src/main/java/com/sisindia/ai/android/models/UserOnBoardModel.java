package com.sisindia.ai.android.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class UserOnBoardModel {

    @SerializedName("CountryCode")
    public String countryCode;

    @SerializedName("CountryId")
    public int countryId;

    @SerializedName("PhoneNumber")
    public String phoneNumber;

    @SerializedName("ResendOtp")
    public boolean resendOtp;

    public UserOnBoardModel() {
    }

    public UserOnBoardModel(String countryCode, int countryId, String phoneNumber, boolean resendOtp) {
        this.countryCode = countryCode;
        this.countryId = countryId;
        this.phoneNumber = phoneNumber;
        this.resendOtp = resendOtp;
    }
}
