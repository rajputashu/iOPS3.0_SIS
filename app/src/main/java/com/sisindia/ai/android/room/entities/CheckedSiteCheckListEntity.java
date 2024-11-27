package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class CheckedSiteCheckListEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("siteChecklistId")
    public int siteChecklistId;

    @SerializedName("checklistLabel")
    public String checklistLabel;

    @SerializedName("checklistQuestionType")
    public String checklistQuestionType;

    public boolean isEdited;

    public int siteChecklistOptionId;

    @SerializedName("optionLabel")
    public String optionLabel;

    @SerializedName("optionResponseType")
    public String optionResponseType;

    public String imageAttachmentId;

    public int siteId;

    public int taskId;

    public String imageUri;

    public CheckedSiteCheckListEntity() {

    }

    @Ignore
    public CheckedSiteCheckListEntity(int siteChecklistId, String checklistLabel, String checklistQuestionType, int siteId, int taskId) {
        this.siteChecklistId = siteChecklistId;
        this.checklistLabel = checklistLabel;
        this.checklistQuestionType = checklistQuestionType;
        this.siteId = siteId;
        this.taskId = taskId;
    }
}
