package com.sisindia.ai.android.models;

import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.base.BaseNetworkResponse;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class ConveyanceResponse extends BaseNetworkResponse {

    @SerializedName("data")
    public Conveyance conveyance;

    public ConveyanceResponse() {

    }

    @Parcel
    public static class Conveyance {

        @SerializedName("aoTimeLine")
        public List<AoTimeLine> aoTimeLine;

        @SerializedName("conveyanceSummary")
        public ConveyanceSummary conveyanceSummary;

        public Conveyance() {
        }
    }

    @Parcel
    public static class AoTimeLine {

        @SerializedName("id")
        public int id;

        @SerializedName("taskId")
        public int taskId;

        @SerializedName("siteId")
        public int siteId;

        @SerializedName("aoId")
        public int aoId;

        @SerializedName("taskName")
        public String taskName;

        @SerializedName("siteName")
        public String siteName;

        @SerializedName("siteCode")
        public String siteCode;

        @SerializedName("actualStartDateTime")
        public String actualStartDateTime;

        @SerializedName("actualEndDateTime")
        public String actualEndDateTime;

        @SerializedName("distance")
        public String distance;

        @SerializedName("arialDistance")
        public String arialDistance;

        @SerializedName("timelineDate")
        public String timelineDate;

        public AoTimeLine() {
        }

    }

    @Parcel
    public static class ConveyanceSummary {

        @SerializedName("totalTask")
        public int totalTask;

        @SerializedName("distanceTravelled")
        public double distanceTravelled;

        @SerializedName("kmApproved")
        public double kmApproved;

        @SerializedName("deduction")
        public int deduction;

        public ConveyanceSummary() {
        }
    }

/*    @Parcel
    public static class AoActivity {

        @SerializedName("taskId")
        public int taskId;

        @SerializedName("siteId")
        public int siteId;

        @SerializedName("aoId")
        public int aoId;

        @SerializedName("taskName")
        public String taskName;

        @SerializedName("siteName")
        public String siteName;

        @SerializedName("siteCode")
        public String siteCode;

        @SerializedName("actualStartDateTime")
        public String actualStartDateTime;

        @SerializedName("actualEndDateTime")
        public String actualEndDateTime;

        public AoActivity() {
        }
    }*/

}
