package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class SiteStrengthEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("shiftId")
    public int shiftId;

    @SerializedName("postId")
    public int postId;

    @SerializedName("authorizedStrength")
    public int authorizedStrength;

    @SerializedName("rankId")
    public int rankId;

    @SerializedName("grade")
    public String grade;

    @SerializedName("actualStrength")
    public int actualStrength;

    @SerializedName("siteId")
    public int siteId;

    public SiteStrengthEntity() {
    }
}

