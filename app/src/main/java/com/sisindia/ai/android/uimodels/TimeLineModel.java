package com.sisindia.ai.android.uimodels;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class TimeLineModel {

    public String dutyOnDateTime;

    public String dutyOffDateTime;

    public List<CompletedTask> tasks;

    public TimeLineModel() {
    }

    @Parcel
    public static class CompletedTask {

        public String siteName;

        public String taskType;

        public String taskStartDateTime;

        public String taskEndDateTime;

        public String siteCode;


        public CompletedTask() {
        }
    }
}
