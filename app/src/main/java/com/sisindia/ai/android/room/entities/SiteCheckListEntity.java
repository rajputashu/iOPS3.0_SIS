package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
@Entity
public class SiteCheckListEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("siteChecklistId")
    public int siteChecklistId;

    @SerializedName("checklistLabel")
    public String checklistLabel;

    @SerializedName("checklistQuestionType")
    public String checklistQuestionType;

    public int siteId;

    @Ignore
    @SerializedName("siteChecklistOptions")
    public List<SiteCheckListOptionEntity> siteChecklistOptions;

    @Ignore
    public SiteCheckListEntity(int siteChecklistId, String checklistLabel, String checklistQuestionType, int siteId) {
        this.siteChecklistId = siteChecklistId;
        this.checklistLabel = checklistLabel;
        this.checklistQuestionType = checklistQuestionType;
        this.siteId = siteId;
    }

    public SiteCheckListEntity() {
    }
}
