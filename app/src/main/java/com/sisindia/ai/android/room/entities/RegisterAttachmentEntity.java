package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.parceler.Parcel;

@Entity
@Parcel
public class RegisterAttachmentEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int postId;

    public int siteId;

    public int taskId;

    public int registerTypeId;

    public int registerAttachmentId;

    public String registerAttachmentGuid;

    public String localPath;

    public RegisterAttachmentEntity() {
    }

    @Ignore
    public RegisterAttachmentEntity(int taskId, int siteId, int postId, int registerTypeId, int registerAttachmentId, String localPath, String registerAttachmentGuid) {
        this.postId = postId;
        this.taskId = taskId;
        this.siteId = siteId;
        this.registerTypeId = registerTypeId;
        this.registerAttachmentId = registerAttachmentId;
        this.localPath = localPath;
        this.registerAttachmentGuid = registerAttachmentGuid;
    }
}
