package com.sisindia.ai.android.uimodels.attachments;


import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class RegisterAttachmentMetaData extends BaseAttachmentMetadata {


    @SerializedName("siteId")
    public int siteId;


    @SerializedName("postId")
    public int postId;


    @SerializedName("taskId")
    public int taskId;


    @SerializedName("sequenceNo")
    public int sequenceNo;


    public RegisterAttachmentMetaData() {
    }
}
