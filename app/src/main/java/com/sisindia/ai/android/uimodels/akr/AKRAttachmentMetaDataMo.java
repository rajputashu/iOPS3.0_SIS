package com.sisindia.ai.android.uimodels.akr;

import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.uimodels.attachments.BaseAttachmentMetadata;

public class AKRAttachmentMetaDataMo extends BaseAttachmentMetadata {

    @SerializedName("kitId")
    public int kitId;

    @SerializedName("employeeId")
    public int employeeId;

    @SerializedName("siteId")
    public int siteId;

    public AKRAttachmentMetaDataMo() {
    }
}
