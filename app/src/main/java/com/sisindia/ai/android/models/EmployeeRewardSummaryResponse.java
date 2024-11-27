package com.sisindia.ai.android.models;

import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.base.BaseNetworkResponse;

import org.parceler.Parcel;

public class EmployeeRewardSummaryResponse extends BaseNetworkResponse {

    @SerializedName("data")
    public RewardSummary summary;

    public EmployeeRewardSummaryResponse() {
    }

    @Parcel
    public static class RewardSummary {

        @SerializedName("employeeId")
        public int employeeId;

        @SerializedName("employeeName")
        public String employeeName;

        @SerializedName("employeeNo")
        public String employeeNo;

        @SerializedName("rewardCount")
        public int rewardCount;

        @SerializedName("totalReward")
        public double totalReward;

        @SerializedName("fineCount")
        public int fineCount;

        @SerializedName("totalFine")
        public double totalFine;


        public RewardSummary() {
        }
    }

}
