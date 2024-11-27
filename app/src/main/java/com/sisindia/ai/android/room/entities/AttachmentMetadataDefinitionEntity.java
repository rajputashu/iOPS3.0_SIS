package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.uimodels.attachments.AttachmentMetadataMO;
import com.sisindia.ai.android.utils.MetaDataConverter;

import org.parceler.Parcel;

/**
 * Created by Ashu Rajput on 5/11/2020.
 */

@Parcel
@Entity
public class AttachmentMetadataDefinitionEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("companyId")
    public int companyId;

    @SerializedName("appTypeId")
    public int appTypeId;

    @SerializedName("attachmentTypeId")
    public int attachmentTypeId;

    @SerializedName("attachmentSourceTypeId")
    public int attachmentSourceTypeId;

    @SerializedName("attachmentSourceType")
    public String attachmentSourceType;

    /*@SerializedName("metadataDefinition")
    public String metadataDefinition;*/

    @SerializedName("versionNo")
    public String versionNo;

    @SerializedName("isActive")
    public boolean isActive;

    @SerializedName("metadataJsonFormat")
    public String metadataJsonFormat;

    @TypeConverters(MetaDataConverter.class)
    @SerializedName("attachmentMetadataObject")
    public AttachmentMetadataMO attachmentMetadataObject;

    public AttachmentMetadataDefinitionEntity() {

    }
}
