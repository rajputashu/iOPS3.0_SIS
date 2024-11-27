package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Entity
@Parcel
public class PostCheckListEntity {


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

    @Ignore
    @SerializedName("postChecklistOptions")
    public List<PostCheckListOptionEntity> postChecklistOptions;

    @Ignore
    public PostCheckListEntity(int postChecklistId, String checklistLabel, String checklistQuestionType, int siteId, int postId) {
        this.postChecklistId = postChecklistId;
        this.checklistLabel = checklistLabel;
        this.checklistQuestionType = checklistQuestionType;
        this.siteId = siteId;
        this.postId = postId;
    }

    public PostCheckListEntity() {
    }
}
