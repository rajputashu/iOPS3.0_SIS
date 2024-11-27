package com.sisindia.ai.android.uimodels.attachments;


import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class AKRAttachmentMetadata {

    @SerializedName("companyId")
    public int companyId = 1;

    @SerializedName("appTypeId")
    public int appTypeId = 1;

    @SerializedName("sequenceNo")
    public int sequenceNo = 1;

    @SerializedName("typeId")
    public int typeId = 1;

    @SerializedName("extension")
    public String extension = ".jpg";

    @SerializedName("title")
    public String title;

    @SerializedName("path")
    public String path;

    @SerializedName("sourceId")
    public int sourceId;

    @SerializedName("sizeInKB")
    public int sizeInKB;

    @SerializedName("sourceTypeId")
    public int sourceTypeId = 102;

    @SerializedName("fileName")
    public String fileName;

/*    @SerializedName("isFileRecieved")
    public boolean isFileReceived = true;

    @SerializedName("attachmentSourceCode")
    public String attachmentSourceCode = "";

    @SerializedName("attachmentMetadata")
    public String attachmentMetadata = "";

    @SerializedName("attachmentGuid")
    public String attachmentGuid;

    @SerializedName("siteTaskId")
    public int siteTaskId = 0;

    @SerializedName("attachmentTypeId")
    public int attachmentTypeId;

    @SerializedName("attachmentSourceTypeId")
    public int attachmentSourceTypeId;

    @SerializedName("uuid")
    public String uuid;

    @SerializedName("fileSize")
    public String fileSize;

    @SerializedName("storagePath")
    public String storagePath;*/

    public AKRAttachmentMetadata() {

    }
}
