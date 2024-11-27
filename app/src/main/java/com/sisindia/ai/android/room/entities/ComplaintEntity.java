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
public class ComplaintEntity {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public int id;

    @SerializedName("customerId")
    public int customerId;

    @SerializedName("siteId")
    public int siteId;

    @SerializedName("taskId")
    public int taskId;

    @SerializedName("complaintAudioAttachmentId")
    public int complaintAudioAttachmentId;

    @SerializedName("complaintModeId")
    public int modeLookupIdentifier;

    @SerializedName("complaintTypeId")
    public int typeLookupIdentifier;

    @SerializedName("complaintNatureId")
    public int natureLookupIdentifier;

    @SerializedName("actionPlanId")
    public int actionPlanId;

    @SerializedName("status")
    public int status;

    @SerializedName("serverId")
    public int serverId;

    @SerializedName("description")
    public String description;

    @SerializedName("targetCompletionDate")
    public String targetCompletionDate;

    @SerializedName("createdDateTime")
    public String createdDateTime;

    @SerializedName("closureComment")
    public String closureComment;

    @SerializedName("complaintGuid")
    public String localId;

    @SerializedName("isSync")
    public boolean isSync;

    @SerializedName("customerContactId")
    public int customerContactId;

    public ComplaintEntity() {

    }

    @Ignore
    public ComplaintEntity(int customerId, int taskId, int siteId, int complaintAudioAttachmentId, int modeLookupIdentifier, int typeLookupIdentifier, int natureLookupIdentifier, int actionPlanId, String description, String targetCompletionDate, int customerContactId) {
        this.customerId = customerId;
        this.siteId = siteId;
        this.taskId = taskId;
        this.complaintAudioAttachmentId = complaintAudioAttachmentId;
        this.modeLookupIdentifier = modeLookupIdentifier;
        this.typeLookupIdentifier = typeLookupIdentifier;
        this.natureLookupIdentifier = natureLookupIdentifier;
        this.actionPlanId = actionPlanId;
        this.status = ComplaintStatus.CREATED.status;
        this.description = description;
        this.targetCompletionDate = targetCompletionDate;
        this.createdDateTime = LocalDateTime.now().toString();
        this.localId = UUID.randomUUID().toString();
        this.customerContactId = customerContactId;
        this.isSync = false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ComplaintEntity)) return false;
        ComplaintEntity it = (ComplaintEntity) obj;
        return serverId == it.serverId;
    }


    public enum ComplaintStatus {

        CREATED(1), PENDING(2), CLOSED(3), COMPLETED(4);

        private final int status;

        ComplaintStatus(int status) {
            this.status = status;
        }

        public static ComplaintStatus of(int status) {
            for (ComplaintStatus type : ComplaintStatus.values()) {
                if (type.status == status) {
                    return type;
                }
            }
            return PENDING;
        }

        public int getStatus() {
            return status;
        }

    }

}
