package com.sisindia.ai.android.uimodels.attachments;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Ashu Rajput on 5/13/2020.
 */
@Parcel
public class BaseAttachmentMetadata {

    @SerializedName("attachmentTypeId")
    public int attachmentTypeId;

    @SerializedName("attachmentSourceTypeId")
    public int attachmentSourceTypeId;

    @SerializedName("uuid")
    public String uuid;

    @SerializedName("fileName")
    public String fileName;

    @SerializedName("fileSize")
    public String fileSize;

    @SerializedName("companyId")
    public int companyId = 1;

    @SerializedName("appTypeId")
    public int appTypeId = 1;

    @SerializedName("storagePath")
    public String storagePath;


    public BaseAttachmentMetadata() {

    }
}


