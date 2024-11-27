package com.sisindia.ai.android.uimodels;

import org.parceler.Parcel;

@Parcel
public class ComplaintModel {

    public int id;

    public int customerId;

    public int siteId;

    public int taskId;

    public int complaintAudioAttachmentId;

    public int modeLookupIdentifier;

    public int typeLookupIdentifier;

    public int natureLookupIdentifier;

    public int actionPlanId;

    public int status;

    public int serverId;

    public String description;

    public String targetCompletionDate;

    public String createdDateTime;

    public String closureComment;

    public String siteName;

    public String modeName;

    public String typeName;

    public String natureName;

    public String title;

    public String contactFullName;

    public ComplaintModel() {
    }
}
