package com.sisindia.ai.android.uimodels.attachments;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class GuardAttachmentMetaData extends BaseAttachmentMetadata {

    @SerializedName("siteId")
    public int siteId;

    @SerializedName("postId")
    public int postId;

    @SerializedName("taskId")
    public int taskId;

    @SerializedName("employeeId")
    public int employeeId;

    @SerializedName("employeeNo")
    public String employeeNo;

    public GuardAttachmentMetaData() {
    }
}
