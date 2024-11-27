package com.sisindia.ai.android.uimodels;

import org.parceler.Parcel;

@Parcel
public class RotaTaskItemModel {

    public int taskId;

    public int serverId;

    public String name;

    public boolean isSynced;

    public String localId;

    public int siteId;

    public String siteName;

    public String siteAddress;

    public String reasonText;

    public String siteCode;

    public int reasonLookUpIdentifier;

    public String actualTaskExecutionStartDateTime;

    public String actualTaskExecutionEndDateTime;

    public String estimatedTaskExecutionStartDateTime;

    public String estimatedTaskExecutionEndDateTime;

    public int taskTypeId;

    public int taskStatus;

    public String description;

    public String taskExecutionResult;

    public int barrackId;

    public String barrackName;

    public String barrackCode;

    public String estimatedTravelTime = "";

    public String estimatedDistance = "";

    public int checkInStatus;

    public String dutyPostCode = "";

    public String estimatedTime;

    public int minGuard;

    public String siteGeoPointString; // Adding this for checking distance 400m

    public String approvedDateTime;

    public Boolean isAutoCreation;

    public RotaTaskItemModel() {
    }
}
