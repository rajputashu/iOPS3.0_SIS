package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Ashu Rajput on 12/14/2020.
 */
@Parcel
@Entity
public class ImprovementPoaEntity {

    @PrimaryKey(autoGenerate = true)
    public int ids;

    @SerializedName("id")
    public int serverId;

    @SerializedName("planType")
    public int planType;

    @SerializedName("categoryId")
    public int categoryId;

    @SerializedName("siteId")
    public int siteId;

    @SerializedName("barrackId")
    public Integer barrackId;

    @SerializedName("actionPlanId")
    public int actionPlanId;

    @SerializedName("description")
    public String description;

    @SerializedName("targetCompletionDate")
    public String targetDateTime;

    @SerializedName("grcticketNo")
    public String grcTicketNo;

    @SerializedName("resolution")
    public String resolution;

    @SerializedName("raisedDateTime")
    public String raisedDateTime;

    @SerializedName("closedDateTime")
    public String closedDateTime;

    @SerializedName("assignedDateTime")
    public String assignedDateTime;

    @SerializedName("status")
    public int status;

    @SerializedName("assignedTo")
    public int assignedTo;

    @SerializedName("assignedToEmployeeName")
    public String assignedToEmployeeName;

    @SerializedName("assignedToEmployeeNo")
    public String assignedToEmployeeNo;

    public ImprovementPoaEntity() {
    }
}
