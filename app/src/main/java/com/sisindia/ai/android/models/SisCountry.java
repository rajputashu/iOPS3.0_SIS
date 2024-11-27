package com.sisindia.ai.android.models;

import androidx.annotation.IntegerRes;

import org.parceler.Parcel;

@Parcel
public class SisCountry {

    private int countryId;

    private String countryName;

    private String countryCode;

    private int maxLength;

    private int minLength;

    private @IntegerRes
    int countryFlag;

    private String countryUrl;

    public SisCountry() {
    }

    public SisCountry(int countryId, String countryName, String countryCode, int maxLength, int minLength, int countryFlag, String countryUrl) {
        this.countryId = countryId;
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.maxLength = maxLength;
        this.minLength = minLength;
        this.countryFlag = countryFlag;
        this.countryUrl = countryUrl;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getCountryFlag() {
        return countryFlag;
    }

    public void setCountryFlag(int countryFlag) {
        this.countryFlag = countryFlag;
    }

    public String getCountryUrl() {
        return countryUrl;
    }

    public void setCountryUrl(String countryUrl) {
        this.countryUrl = countryUrl;
    }
}
