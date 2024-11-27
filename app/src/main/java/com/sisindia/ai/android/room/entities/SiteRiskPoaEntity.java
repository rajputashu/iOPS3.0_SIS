package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Ashu Rajput on 4/6/2020.
 */
@Parcel
@Entity
public class SiteRiskPoaEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("siteRiskId")
    public int siteRiskId;

    @SerializedName("siteId")
    public int siteId;

    @SerializedName("poAReason")
    public int poAReason;

    @SerializedName("atRiskActionPlan")
    public int atRiskActionPlan;

    @SerializedName("targetCompletionDate")
    public String targetCompletionDate;

    @SerializedName("closureDateTime")
    public String closureDateTime;

    @SerializedName("poAStatus")
    public int poAStatus;

    @SerializedName("assignedTo")
    public int assignedTo;

    @SerializedName("assignedToName")
    public String assignedToName;

    @SerializedName("assignedToEmployeeNo")
    public String assignedToEmployeeNo;

    @SerializedName("openingRemarks")
    public String openingRemarks;

    @SerializedName("closingRemarks")
    public String closingRemarks;

    @SerializedName("createdByAppId")
    public int createdByAppId;

    @SerializedName("closureProofAttachment")
    public int closureProofAttachment;

    @SerializedName("isSynced")
    public int isSynced;

    public SiteRiskPoaEntity() {

    }
}
