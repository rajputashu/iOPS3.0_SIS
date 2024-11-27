package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.threeten.bp.LocalDateTime;

@Parcel
@Entity
public class KitRequestItemEntity {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public int id;

    @SerializedName("kitRequestId")
    public int kitRequestId;//id from GuardKitRequestEntity

    @SerializedName("employeeId")
    public int employeeId;

    @SerializedName("kitItemId")
    public int kitItemId; // id from KitItemEntity

    @SerializedName("kitSizeId")
    public int kitSizeId;//id from KitItemSizeEntity if present and selected

    @SerializedName("taskId")
    public int taskId;

    @SerializedName("siteId")
    public int siteId;

    @SerializedName("createdDateTime")
    public String createdDateTime;

    @SerializedName("updatedDateTime")
    public String updatedDateTime;

    @SerializedName("replaceStatus")
    public int replaceStatus;

    public KitRequestItemEntity() {
    }

    @Ignore
    public KitRequestItemEntity(int kitRequestId, int employeeId, int kitItemId, int taskId, int siteId) {
        this.kitRequestId = kitRequestId;
        this.employeeId = employeeId;
        this.kitItemId = kitItemId;
        this.taskId = taskId;
        this.siteId = siteId;
        this.createdDateTime = LocalDateTime.now().toString();
        this.updatedDateTime = LocalDateTime.now().toString();
        this.replaceStatus = 1;
    }
}
