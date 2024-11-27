package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.models.GeoPointItem;
import com.sisindia.ai.android.utils.GeoLocationConverter;

import org.parceler.Parcel;

/**
 * Created by Ashu Rajput on 5/9/2020.
 */
@Parcel
@Entity
public class AIProfileEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("branchHeadName")
    public String branchHeadName;

    @SerializedName("bracnhHeadEmployeeNo")
    public String bracnhHeadEmployeeNo;

    @SerializedName("branchHeadEmailId")
    public String branchHeadEmailId;

    @SerializedName("employeeNo")
    public String employeeNo;

    @SerializedName("url")
    public String url;

    @SerializedName("token")
    public String token;

    @SerializedName("employeeName")
    public String employeeName;

    @TypeConverters(GeoLocationConverter.class)
    @SerializedName("branchGeoPoint")
    public GeoPointItem homeGeoPoint;

    @SerializedName("rankId")
    public int rankId;

    @SerializedName("rankCode")
    public String rankCode;

    @SerializedName("branchId")
    public int branchId;

    @SerializedName("branchName")
    public String branchName;

    @SerializedName("branchCode")
    public String branchCode;

    @SerializedName("regionId")
    public int regionId;

    @SerializedName("regionCode")
    public String regionCode;

    @SerializedName("regionName")
    public String regionName;

    @SerializedName("zoneId")
    public int zoneId;

    @SerializedName("zoneName")
    public String zoneName;

    @SerializedName("companyId")
    public int companyId;

    @SerializedName("phone")
    public String phone;

    @SerializedName("emailId")
    public String emailId;

    @SerializedName("zoneCode")
    public String zoneCode;

    @SerializedName("appTypeId")
    public int appTypeId;

    public AIProfileEntity() {

    }
}
