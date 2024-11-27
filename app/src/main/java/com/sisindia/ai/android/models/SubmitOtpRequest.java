package com.sisindia.ai.android.models;

import org.parceler.Parcel;

@Parcel
public class SubmitOtpRequest {

    private String countryCode;

    private String mobileNumber;

    private String otp;


    public SubmitOtpRequest() {
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
