package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.commons.TaskStatus;

import org.parceler.Parcel;
import org.threeten.bp.LocalDateTime;

/**
 * Created by Ashu Rajput on 4/27/2020.
 */
@Parcel
@Entity
public class CacheStrengthEntity {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public int id;

    @SerializedName("strengthId")
    public int strengthId;

    @SerializedName("siteId")
    public int siteId;

    @SerializedName("taskId")
    public int taskId;

    @SerializedName("postId")
    public int postId;

    @SerializedName("grade")
    public String grade;

    @SerializedName("authorizedStrength")
    public int authorizedStrength;

    @SerializedName("actualStrength")
    public Integer actualStrength;

    @SerializedName("createdDateTime")
    public String createdDateTime;

    @SerializedName("updatedDateTime")
    public String updatedDateTime;

    @SerializedName("taskStatus")
    public int taskStatus = TaskStatus.PENDING.getStatus();

    @SerializedName("shiftId")
    public int shiftId;

    @SerializedName("rankId")
    public int rankId;

    @SerializedName("shortage")
    public int shortage;


    public CacheStrengthEntity() {

    }

    @Ignore
    public CacheStrengthEntity(int strengthId, int siteId, int taskId, int postId, String grade, int authorizedStrength, int shiftId, int rankId) {
        this.strengthId = strengthId;
        this.siteId = siteId;
        this.taskId = taskId;
        this.postId = postId;
        this.grade = grade;
        this.authorizedStrength = authorizedStrength;
        this.shiftId = shiftId;
        this.rankId = rankId;
        this.taskStatus = TaskStatus.PENDING.getStatus();
        this.createdDateTime = LocalDateTime.now().toString();
        this.updatedDateTime = LocalDateTime.now().toString();
    }
}
