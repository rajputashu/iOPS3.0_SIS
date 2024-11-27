package com.sisindia.ai.android.uimodels.attachments;

import com.droidcommons.preference.Prefs;
import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.constants.PrefConstants;

import org.parceler.Parcel;

/**
 * Created by Ashu Rajput on 1/29/2021.
 */
@Parcel
public class SelfieAttachmentMetadata {

    @SerializedName("sourceId")
    public int sourceId;

    @SerializedName("isFileRecieved")
    public boolean isFileRecieved = true;

    @SerializedName("attachmentSourceCode")
    public String attachmentSourceCode = "";

    @SerializedName("attachmentMetadata")
    public String attachmentMetadata = "";

    @SerializedName("attachmentGuid")
    public String attachmentGuid;

    @SerializedName("siteTaskId")
    public int siteTaskId = 0;

    @SerializedName("sequenceNo")
    public int sequenceNo = 1;

    @SerializedName("typeId")
    public int typeId = 1;

    @SerializedName("title")
    public String title;

    @SerializedName("path")
    public String path;

    @SerializedName("extension")
    public String extension;

    @SerializedName("sizeInKB")
    public int sizeInKB;

    @SerializedName("sourceTypeId")
    public int sourceTypeId = 101;

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
    public int companyId = Prefs.getInt(PrefConstants.COMPANY_ID, 1);

    @SerializedName("appTypeId")
    public int appTypeId = 1;

    @SerializedName("storagePath")
    public String storagePath;

    public SelfieAttachmentMetadata() {

    }
}

