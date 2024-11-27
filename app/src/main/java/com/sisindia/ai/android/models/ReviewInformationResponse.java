package com.sisindia.ai.android.models;

import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.base.BaseNetworkResponse;

import org.parceler.Parcel;

@Parcel
public class ReviewInformationResponse extends BaseNetworkResponse {

    @SerializedName("data")
    public IssueSummary issueSummary;

    public ReviewInformationResponse() {
    }

    @Parcel
    public static class IssueSummary {

        @SerializedName("grievance")
        public int grievances;

        @SerializedName("complaint")
        public int complaints;

        @SerializedName("collectionDue")
        public int collectionDues;

        @SerializedName("poAs")
        public int poAs;

        @SerializedName("lastTaskDone")
        public String lastTaskDone;

        @SerializedName("taskType")
        public String taskType;

        public int checkedGuards;

        public int authorizedGuards;

        public IssueSummary() {
        }


    }

}
