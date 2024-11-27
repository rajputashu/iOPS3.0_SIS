package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
@Entity
public class SiteEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("parentSiteId")
    public Integer parentSiteId;

    @SerializedName("siteName")
    public String siteName;

    @SerializedName("siteCode")
    public String siteCode;

    @SerializedName("contractId")
    public int contractId;

    @SerializedName("deploymentTypeId")
    public int deploymentTypeId;

    @SerializedName("description")
    public String description;

    @SerializedName("siteTypeId")
    public int siteTypeId;

    @SerializedName("branchId")
    public int branchId;

    @SerializedName("siteStatus")
    public int siteStatus;

    @SerializedName("siteAddress")
    public String siteAddress;

    @SerializedName("isActive")
    public boolean isActive;

    @SerializedName("areaInspectorId")
    public int areaInspectorId;

    @SerializedName("deploymentType")
    public String deploymentType;

    @SerializedName("siteType")
    public String siteType;

    @SerializedName("contractCode")
    public String contractCode;

    @SerializedName("siteGeoPointString")
    public String siteGeoPointString;

    @Ignore
    @SerializedName("siteExtension")
    public SiteExtensionEntity siteExtension;

    @Ignore
    @SerializedName("billCenters")
    public List<BillCenterEntity> billCenters;

    @Ignore
    @SerializedName("wageCenters")
    public List<WageCenterEntity> wageCenters;

    @SerializedName("isNCSite")
    public boolean isNCSite = false;

    public SiteEntity() {
    }

    @Ignore
    public SiteEntity(int id, String siteName) {
        this.id = id;
        this.siteName = siteName;
    }
}
