package com.sisindia.ai.android.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class DeviceInfo {

    @SerializedName("PhoneNumber")
    public String phoneNumber;

    @SerializedName("AppVersion")
    public String appVersion;

    @SerializedName("DeviceId")
    public String deviceId;

    @SerializedName("Imei1")
    public String imei1;

    @SerializedName("Imei2")
    public String imei2;

    @SerializedName("Sim1PhoneNo")
    public String sim1PhoneNo;

    @SerializedName("Sim2PhoneNo")
    public String sim2PhoneNo;

    @SerializedName("AppToken")
    public String appToken;

    @SerializedName("DeviceOrgName")
    public String deviceManufacturer;

    @SerializedName("DeviceName")
    public String deviceModel;

    @SerializedName("AndroidVer")
    public String androidOSVersion;

    public DeviceInfo() {
    }

}
