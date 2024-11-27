package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class PostCheckListOptionEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("optionLabel")
    public String optionLabel;

    @SerializedName("optionResponseType")
    public String optionResponseType;

    public int siteId;

    public int postId;
    public int postChecklistId;

    public PostCheckListOptionEntity() {
    }

    @Ignore
    public PostCheckListOptionEntity(int id, String optionLabel, String optionResponseType, int siteId, int postId) {
        this.id = id;
        this.optionLabel = optionLabel;
        this.optionResponseType = optionResponseType;
        this.siteId = siteId;
        this.postId = postId;
    }
}
