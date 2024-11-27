package com.sisindia.ai.android.uimodels;

import org.parceler.Parcel;

@Parcel
public class DayNightCheckModel {

    public int taskId;

    public String localId;

    public int siteId;

    public String siteName;

    public String siteCode;

    public String siteAddress;

    public String actualTaskExecutionStartDateTime;

    public String actualTaskExecutionEndDateTime;

    public String estimatedTaskExecutionStartDateTime;

    public String estimatedTaskExecutionEndDateTime;

    public String taskExecutionResult;

    public String taskName;

    public int taskTypeId;

    public int availablePosts;

    public int availableStrength;

    public int availableSiteCheckList;

    public int noOfCheckedPosts;

    public int noOfCheckedStrength;

    public int noOfSiteCheckListDone;

    public Integer clientHandshakeIsDone;

    public int checkInStatus;

    public String checkInDateTime;

    public DayNightCheckModel() {
    }
}
