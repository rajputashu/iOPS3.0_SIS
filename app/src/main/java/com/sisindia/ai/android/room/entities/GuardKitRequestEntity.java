package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.threeten.bp.LocalDateTime;

import java.util.List;
import java.util.UUID;

@Parcel
@Entity
public class GuardKitRequestEntity {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public int id;

    @SerializedName("employeeId")
    public int employeeId;

    @SerializedName("imageAttachmentGuid")
    public String imageAttachmentGuid;

    @SerializedName("signatureAttachmentGuid")
    public String signatureAttachmentGuid;

    @SerializedName("taskId")
    public int taskId;

    @SerializedName("siteId")
    public int siteId;

    @SerializedName("localId")
    public String localId;

    @SerializedName("serverId")
    public int serverId;

    @SerializedName("isSync")
    public boolean isSync;

    @SerializedName("createdDateTime")
    public String createdDateTime;

    @SerializedName("updatedDateTime")
    public String updatedDateTime;

    @SerializedName("kitStatus")
    public int kitStatus;

    @Ignore
    @SerializedName("kitItems")
    public List<KitRequestItemEntity> kitItems;

    @Ignore
    public GuardKitRequestEntity(int employeeId, String signatureAttachmentGuid, int taskId, int siteId) {
        this.employeeId = employeeId;
        this.signatureAttachmentGuid = signatureAttachmentGuid;
        this.taskId = taskId;
        this.siteId = siteId;
        this.localId = UUID.randomUUID().toString();
        this.isSync = false;
        this.createdDateTime = LocalDateTime.now().toString();
        this.updatedDateTime = LocalDateTime.now().toString();
        this.kitStatus = 1;
    }


    public GuardKitRequestEntity() {
    }
}
