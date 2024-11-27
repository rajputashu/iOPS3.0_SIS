package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.commons.TaskStatus;

import org.parceler.Parcel;
import org.threeten.bp.LocalDateTime;

import java.util.UUID;

/**
 * Created by Ashu Rajput on 4/22/2020.
 */

@Parcel
@Entity
public class CacheBarrackInspectionEntity {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public int id;

    @SerializedName("localId")
    public String localId;

    @Expose
    @SerializedName("taskId")
    public int taskId;

    @Expose
    @SerializedName("strengthJson")
    public String strengthJson;

    @Expose
    @SerializedName("spaceJson")
    public String spaceJson;

    @Expose
    @SerializedName("othersJson")
    public String othersJson;

    @Expose
    @SerializedName("metLandlordJson")
    public String metLandlordJson;

    @SerializedName("createdDateTime")
    public String createdDateTime;

    @SerializedName("updatedDateTime")
    public String updatedDateTime;

    //[1- Pending, 2- Completed]
    @SerializedName("taskStatus")
    public int taskStatus;

    public CacheBarrackInspectionEntity() {

    }

    @Ignore
    public CacheBarrackInspectionEntity(int taskId) {
        this.taskId = taskId;
        this.localId = UUID.randomUUID().toString();
        this.taskStatus = TaskStatus.COMPLETED.getStatus();
        this.createdDateTime = LocalDateTime.now().toString();
        this.updatedDateTime = LocalDateTime.now().toString();
    }
}
