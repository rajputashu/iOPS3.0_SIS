package com.sisindia.ai.android.uimodels.attachments;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Ashu Rajput on 3/7/2021.
 */
@Parcel
public class GrievanceAttachmentMetaData extends BaseAttachmentMetadata {

    @SerializedName("siteId")
    public int siteId;

    @SerializedName("taskId")
    public int taskId;

    @SerializedName("employeeId")
    public int employeeId;

    @SerializedName("employeeNo")
    public String employeeNo;

    public GrievanceAttachmentMetaData() {
    }
}