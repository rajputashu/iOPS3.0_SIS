package com.sisindia.ai.android.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ashu Rajput on 5/23/2020.
 */
public class BillCollectionApiBodyMO {
    @SerializedName("id")
//    public int id;
    public int billId;

    @SerializedName("billOutputCenterId")
    public int billOutputCenterId;

    @SerializedName("collectionStatus")
    public int collectionStatus;

    @SerializedName("paidAmount")
    public int paidAmount;

    @SerializedName("collectedDate")
    public String collectedDate;

    @SerializedName("collectionMode")
    public int collectionMode;

    @SerializedName("transactionId")
    public String transactionId;

    @SerializedName("comments")
    public String comments;

    @SerializedName("collectionShortageReason")
    public int collectionShortageReason;

    @SerializedName("collectionMarkedById")
    public int collectionMarkedById;
}
