package com.sisindia.ai.android.features.units.entity;

import androidx.room.Ignore;

/**
 * Created by Ashu Rajput on 3/31/2020.
 */
public class SiteTaskDetailsMO {
    public int UnitId;

    @Ignore
    public int TaskStatus;

    public String UnitName;
    public String UnitCode;
    public String SiteAddress;
    public String siteGeoPointString;
    public Boolean isUnitTagged = false;
    public String UpcomingTask;
    public int NoOfPost;
    public int billCollectionDay;
    public int billGenerationDay;
    public int billSubmissionDay;
    public int wageDay;
    public Integer parentSiteId;
    public int postTagged;

    @Ignore
    public Boolean isExpandable = false;

    //{With Default Values}
    @Ignore
    public UnitsUpcomingDetailsMO pendingTasksMO = new UnitsUpcomingDetailsMO("NO", "NA", "NA", "NA", "NA");

    public SiteTaskDetailsMO() {

    }
}
