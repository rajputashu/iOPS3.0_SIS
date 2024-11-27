package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class SiteExtensionEntity {

    @PrimaryKey
    @SerializedName("siteId")
    public int siteId;

    @SerializedName("wageBranchCode")
    public String wageBranchCode;

    @SerializedName("misbranchCode")
    public String misbranchCode;

    @SerializedName("areaOfficeCode")
    public String areaOfficeCode;

    @SerializedName("gstbranchCode")
    public String gstbranchCode;

    @SerializedName("billingCode")
    public String billingCode;

    @SerializedName("areaInspectorId")
    public int areaInspectorId;

    @SerializedName("unitCommanderId")
    public int unitCommanderId;

    @SerializedName("distanceFromBranch")
    public double distanceFromBranch;

    @SerializedName("distanceFromAihome")
    public double distanceFromAihome;

    @SerializedName("siteConfig")
    public String siteConfig;

    @SerializedName("billGenerationDay")
    public int billGenerationDay;

    @SerializedName("billSubmissionDay")
    public int billSubmissionDay;

    @SerializedName("billCollectionDay")
    public int billCollectionDay;

    @SerializedName("wageDay")
    public int wageDay;

    @SerializedName("isClientHandshakeApplicable")
    public boolean isClientHandshakeApplicable;

    @SerializedName("isNAG")
    public boolean isNAG;

    @SerializedName("siteRelationshipType")
    public int siteRelationshipType;

    @SerializedName("tempAreaOfficerId")
    public int tempAreaOfficerId;

    @SerializedName("tempAOValidTo")
    public String tempAOValidTo;

    @SerializedName("tempAOValidFrom")
    public String tempAOValidFrom;

    @SerializedName("clientHandshakeTarget")
    public int clientHandshakeTarget;

    @SerializedName("dcMinPostCheck")
    public int dcMinPostCheck;

    @SerializedName("ncMinPostCheck")
    public int ncMinPostCheck;

    @SerializedName("dcMinGuard")
    public int dcMinGuard;

    @SerializedName("ncMinGuard")
    public int ncMinGuard;

    @Ignore
    @SerializedName("siteConfiguration")
    public SiteConfiguration siteConfiguration;

    public SiteExtensionEntity() {
    }
}