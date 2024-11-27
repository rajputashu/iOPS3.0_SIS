package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class CheckedPostCheckListEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("postChecklistId")
    public int postChecklistId;

    @SerializedName("checklistLabel")
    public String checklistLabel;

    @SerializedName("checklistQuestionType")
    public String checklistQuestionType;

    public int siteId;

    public int postId;

    public boolean isEdited;

    @SerializedName("optionLabel")
    public String optionLabel;

    @SerializedName("optionResponseType")
    public String optionResponseType;

    public int imageAttachmentId;

    public int taskId;

    public String imageUri;

    public int postChecklistOptionId;

    public String remarks;

    public CheckedPostCheckListEntity() {
    }

    @Ignore
    public CheckedPostCheckListEntity(int postChecklistId, String checklistLabel, String checklistQuestionType, int postId, int siteId, int taskId) {
        this.postChecklistId = postChecklistId;
        this.checklistLabel = checklistLabel;
        this.checklistQuestionType = checklistQuestionType;
        this.postId = postId;
        this.siteId = siteId;
        this.taskId = taskId;
    }
}
