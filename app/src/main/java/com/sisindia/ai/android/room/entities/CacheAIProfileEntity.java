package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.droidcommons.preference.Prefs;
import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.constants.PrefConstants;

import org.parceler.Parcel;
import org.threeten.bp.LocalDateTime;

/**
 * Created by Ashu Rajput on 4/7/2020.
 */

@Parcel
@Entity
public class CacheAIProfileEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("employeeId")
    public int aiId;

    @SerializedName("aiPhotoURI")
    public String aiPhotoURI;

    @SerializedName("addressLine")
    public String altAddress;

    @SerializedName("pincode")
    public String pinCode;

    @SerializedName("longitude")
    public double longitude;

    @SerializedName("latitude")
    public double latitude;

    @SerializedName("secondaryContactNo")
    public String altContactNo;

    @SerializedName("createdDateTime")
    public String createdDateTime;

    @SerializedName("updatedDateTime")
    public String updatedDateTime;

    @SerializedName("isSynced")
    public boolean isSynced;

    public CacheAIProfileEntity() {
    }

    @Ignore
    public CacheAIProfileEntity(String aiPhotoURI, String altAddress, String pinCode,
                                double latitude, double longitude, String altContactNo) {
        this.aiId = Prefs.getInt(PrefConstants.AREA_INSPECTOR_ID);
        this.aiPhotoURI = aiPhotoURI;
        this.altAddress = altAddress;
        this.pinCode = pinCode;
        this.altContactNo = altContactNo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdDateTime = LocalDateTime.now().toString();
        this.updatedDateTime = LocalDateTime.now().toString();
        this.isSynced=false;
    }
}
