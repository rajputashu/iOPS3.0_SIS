package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.threeten.bp.LocalDateTime;

/**
 * Created by Ashu Rajput on 4/25/2020.
 */
@Parcel
@Entity
public class CacheClientHandshakeEntity {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public int id;

    @SerializedName("taskId")
    public int taskId;

    @SerializedName("siteId")
    public int siteId;

    @SerializedName("IsMetClient")
    public boolean isMetClient;

    @SerializedName("Reason")
    public String reason;

    @SerializedName("ClientName")
    public String clientName;

    @SerializedName("ContactNo")
    public String contactNo;

    @SerializedName("Designation")
    public String designation;

    @SerializedName("ClientId")
    public String clientId;

    @SerializedName("ClientEmail")
    public String clientEmail;

    @SerializedName("Feedback")
    public String feedbackStar;

    @SerializedName("questions")
    public String questions;

    @SerializedName("createdDateTime")
    public String createdDateTime;

    @SerializedName("updatedDateTime")
    public String updatedDateTime;

    //[1-Started or Pending 2-Completed]
    @SerializedName("statusType")
    public int taskStatus = ClientHandShakeStatus.STARTED.statusType;

    public CacheClientHandshakeEntity() {

    }

    @Ignore
    public CacheClientHandshakeEntity(int taskId) {
        this.taskId = taskId;
        this.createdDateTime = LocalDateTime.now().toString();
        this.updatedDateTime = LocalDateTime.now().toString();
    }

    public enum ClientHandShakeStatus {

        STARTED(1), COMPLETED(2);

        private final int statusType;

        ClientHandShakeStatus(int statusType) {
            this.statusType = statusType;
        }

        public int getStatusType() {
            return statusType;
        }
    }
}
