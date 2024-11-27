package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class LanguageEntity {

    @PrimaryKey
    @SerializedName("languageId")
    public int languageId;

    @SerializedName("languageName")
    public String languageName;

    @SerializedName("cultureCode")
    public String cultureCode;

    @SerializedName("countryId")
    public int countryId;

    @SerializedName("createdDateTime")
    public String createdDateTime;

    @SerializedName("updatedDateTime")
    public String updatedDateTime;

    @SerializedName("createdBy")
    public int createdBy;

    @SerializedName("updatedBy")
    public int updatedBy;

    public LanguageEntity() {
    }
}
