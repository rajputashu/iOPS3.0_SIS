package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Ashu Rajput on 5/21/2020.
 */
@Parcel
@Entity
public class BillCollectionsEntity {
    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("billOutputCenterCode")
    public String billOutputCenterCode;

    @SerializedName("billNumber")
    public String billNumber;

    @SerializedName("billOutputCenterId")
    public int billOutputCenterId;

    @SerializedName("billingMonth")
    public int billingMonth;

    @SerializedName("billingYear")
    public int billingYear;

    @SerializedName("outstandingAmount")
//    public int outstandingAmount;
    public double outstandingAmount;

    @SerializedName("paymentTerm")
    public int paymentTerm;

    @SerializedName("createdDateTime")
    public String createdDateTime;

    @SerializedName("updatedDateTime")
    public String updatedDateTime;

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
    public String collectionShortageReason;

    @SerializedName("collectionMarkedById")
    public int collectionMarkedById;

    @SerializedName("collectionProofPhotoId")
    public String collectionProofPhotoId;

    @Ignore
    public boolean isBillSelected=false;

    public BillCollectionsEntity(){

    }
}
