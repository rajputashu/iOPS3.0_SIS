package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Entity
@Parcel
public class CheckedRegisterEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int postId;

    public int siteId;

    public int taskId;

    public boolean isMissing;

    public String createdDateTime;

    public String updatedDateTime;

    @SerializedName("registerTypeId")
    public int registerTypeId;

    @SerializedName("registerType")
    public String registerType;

    @SerializedName("isMandatory")
    public boolean isMandatory;

    public int noOfPages;

    @SerializedName("attachmentSourceTypeId")
    public int attachmentSourceTypeId;

    @Ignore
    public List<RegisterAttachmentEntity> documents;

    public CheckedRegisterEntity() {
    }

    @Ignore
    public CheckedRegisterEntity(int attachmentSourceTypeId, int registerTypeId, int postId, int siteId, String registerType, boolean isMandatory, int taskId) {
        this.attachmentSourceTypeId = attachmentSourceTypeId;
        this.registerTypeId = registerTypeId;
        this.postId = postId;
        this.siteId = siteId;
        this.registerType = registerType;
        this.isMandatory = isMandatory;
        this.taskId = taskId;
    }
}
