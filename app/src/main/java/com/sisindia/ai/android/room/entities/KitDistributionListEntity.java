package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Ashu Rajput on 8/20/2020.
 */
@Parcel
@Entity
public class KitDistributionListEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("issuingOfficerId")
    public int issuingOfficerId;

    @SerializedName("siteId")
    public int siteId;

    @SerializedName("distributionStatus")
    public int distributionStatus;

    @SerializedName("siteTaskId")
    public int siteTaskId;

    @SerializedName("recipientId")
    public int recipientId;

    @SerializedName("branchId")
    public int branchId;

    @SerializedName("kitTypeId")
    public int kitTypeId;

    @SerializedName("kitRequestId")
    public int kitRequestId;

    @SerializedName("distributionDateTime")
    public String distributionDateTime;

    @SerializedName("recipientName")
    public String recipientName;

    @SerializedName("recipientCode")
    public String recipientCode;

    @SerializedName("recipientPhoneNo")
    public String recipientPhoneNo;

    @SerializedName("issuingOfficerName")
    public String issuingOfficerName;

    @SerializedName("issuingOfficerCode")
    public String issuingOfficerCode;

    @SerializedName("issuingOfficerPhoneNo")
    public String issuingOfficerPhoneNo;

    @SerializedName("branchName")
    public String branchName;

    @SerializedName("branchCode")
    public String branchCode;

    @SerializedName("siteName")
    public String siteName;

    @SerializedName("siteCode")
    public String siteCode;

    @SerializedName("otpTypeId")
    public int otpTypeId;

    @SerializedName("erpKey")
    public String kitIssueNo;

    @SerializedName("isAllItemsDistributed")
    public boolean isAllItemsDistributed = false;

    @SerializedName("createdDateTime")
    public String createdDateTime;

    @SerializedName("createdBy")
    public int createdBy;

    @SerializedName("updatedDateTime")
    public String updatedDateTime;

    @SerializedName("updatedBy")
    public int updatedBy;

    @Ignore
    @SerializedName("kitDistributionItems")
    public List<KitDistributionItemsEntity> kitDistributionItems;

    public KitDistributionListEntity() {

    }
}