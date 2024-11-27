package com.sisindia.ai.android.uimodels;

import org.parceler.Parcel;

@Parcel
public class ReviewInformationModel {

    public int siteId;

    public String siteName;

    public int taskId;

    public int taskTypeId;

    public String taskName;

    public int serverId;

    public int taskStatus;

    public int pendingGrievances;

    public int pendingComplaints;

    public int checkInStatus;

    public String dutyPostCode = "";

    public ReviewInformationModel() {
    }

    @Parcel
    public static class LastVisitDetail {

        public String lastVisitDateTime;

        public int checkedGuards;

        public int authorizedGuards;

        public int taskTypeId;

        public int taskId;

        public String activityName;

        public String siteName;
    }
}
