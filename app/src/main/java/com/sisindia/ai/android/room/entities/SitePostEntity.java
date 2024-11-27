package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class SitePostEntity {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public int id;

    @SerializedName("postName")
    public String postName;

    @SerializedName("dutyPostCode")
    public String dutyPostCode;

    @SerializedName("description")
    public String description;//

    @SerializedName("qRenabled")
    public boolean qRenabled;

    @SerializedName("isActive")
    public boolean isActive;//

    @SerializedName("createdDateTime")
    public String createdDateTime;//

    @SerializedName("createdBy")
    public int createdBy;//

    @SerializedName("updatedDateTime")
    public String updatedDateTime;//

    @SerializedName("updatedBy")
    public int updatedBy;//

    @SerializedName("isMainPost")
    public boolean isMainPost;

    @SerializedName("siteId")
    public int siteId;

    @SerializedName("spiEnabled")
    public boolean spiEnabled = false;

    @SerializedName("postConfig")
    public String postConfig;

    @SerializedName("srNumber")
    public String srNumber;

    @SerializedName("postGeoPointString")
    public String postGeoPointString;

    @SerializedName("isSynced")
    public int isSynced = 1;

    @SerializedName("isCreated")
    public int isCreated = 1;

    @SerializedName("isDelete")
    public int isDelete = 0;

    @Ignore
    @SerializedName("postConfiguration")
    public PostConfiguration postConfiguration;

    public SitePostEntity() {
    }

}
