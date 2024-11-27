package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.threeten.bp.LocalDateTime;

import java.util.UUID;

@Parcel
@Entity
public class GrievanceEntity {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public int id;

    @SerializedName("employeeId")
    public int employeeId;

    @SerializedName("categoryId")
    public int categoryId;

    @SerializedName("description")
    public String description;

    @SerializedName("audioAttachmentId")
    public int audioAttachmentId;

    @SerializedName("actionPlanId")
    public int actionPlanId;

    @SerializedName("targetCompletionDate")
    public String targetDateTime;

    @SerializedName("siteId")
    public int siteId;

    @SerializedName("siteTaskId")
    public int taskId;

    @SerializedName("postId")
    public int postId;

    @SerializedName("status")
    public int grievanceStatus;

    @SerializedName("createdDateTime")
    public String createdDateTime;

    @SerializedName("updatedDateTime")
    public String updatedDateTime;

    @SerializedName("GrievanceGuid")
    public String localId;

    @SerializedName("closureComment")
    public String closeRemarks;

    @SerializedName("serverId")
    public int serverId;

    @SerializedName("isSync")
    public boolean isSync;


    public GrievanceEntity() {
    }

    @Ignore
    public GrievanceEntity(int employeeId, int categoryId, String descriptionText, int actionPlanId, String targetDate, int taskId, int postId, int siteId, Long attachmentId) {
        this.employeeId = employeeId;
        this.categoryId = categoryId;
        this.description = descriptionText;
        this.actionPlanId = actionPlanId;
        this.targetDateTime = targetDate;
        this.taskId = taskId;
        this.postId = postId;
        this.siteId = siteId;
        if (attachmentId != 0L) {
            this.audioAttachmentId = attachmentId.intValue();
        }
        this.grievanceStatus = GrievanceStatus.NEW.status;
        this.createdDateTime = LocalDateTime.now().toString();
        this.updatedDateTime = LocalDateTime.now().toString();
        this.localId = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof GrievanceEntity)) return false;
        GrievanceEntity it = (GrievanceEntity) obj;
        return serverId == it.serverId;
    }


    public enum GrievanceStatus {
        UNKNOWN(0), NEW(1), ASSIGNED(2), IN_PROGRESS(3), COMPLETED(4), CLOSED(5);

        private final int status;

        GrievanceStatus(int status) {
            this.status = status;
        }

        public static GrievanceStatus of(int status) {
            for (GrievanceStatus type : GrievanceStatus.values()) {
                if (type.status == status) {
                    return type;
                }
            }
            return UNKNOWN;
        }

        public int getStatus() {
            return status;
        }
    }
}

