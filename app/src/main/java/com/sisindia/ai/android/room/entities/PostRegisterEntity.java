package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Entity
@Parcel
public class PostRegisterEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("registerTypeId")
    public int registerTypeId;

    @SerializedName("attachmentSourceTypeId")
    public int attachmentSourceTypeId;

    public int postId;

    public int siteId;

    @SerializedName("registerType")
    public String registerType;

    @SerializedName("isMandatory")
    public boolean isMandatory;

    public PostRegisterEntity() {
    }
}
