package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity(indices = {@Index(value = {"optionLabel", "optionResponseType", "siteId", "siteChecklistId"}, unique = true)})
public class SiteCheckListOptionEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("optionLabel")
    public String optionLabel;

    @SerializedName("optionResponseType")
    public String optionResponseType;

    public int siteId;

    @SerializedName("siteChecklistId")
    public int siteChecklistId;

    public SiteCheckListOptionEntity() {
    }

    @Ignore
    public SiteCheckListOptionEntity(int id, String optionLabel, String optionResponseType, int siteId) {
        this.id = id;
        this.optionLabel = optionLabel;
        this.optionResponseType = optionResponseType;
        this.siteId = siteId;
    }
}
