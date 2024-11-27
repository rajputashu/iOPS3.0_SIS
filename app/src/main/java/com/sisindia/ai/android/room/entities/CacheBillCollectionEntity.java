package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.droidcommons.preference.Prefs;
import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.constants.PrefConstants;

import org.parceler.Parcel;
import org.threeten.bp.LocalDateTime;

/**
 * Created by Ashu Rajput on 5/22/2020.
 */
@Parcel
@Entity
public class CacheBillCollectionEntity {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public int id;

    @SerializedName("billId")
    public int billId;

    @SerializedName("uuid")
    public String uuid;

    /*@SerializedName("billOutputCenterCode")
    public String billOutputCenterCode;*/

    @SerializedName("billNumber")
    public String billNumber;

    @SerializedName("billOutputCenterId")
    public int billOutputCenterId;

    /*@SerializedName("billingMonth")
    public int billingMonth;

    @SerializedName("billingYear")
    public int billingYear;*/

    /*@SerializedName("outstandingAmount")
    public int outstandingAmount;*/

    /*@SerializedName("paymentTerm")
    public int paymentTerm;*/

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

    /*@SerializedName("collectionProofPhotoId")
    public String collectionProofPhotoId;*/

    public boolean isSynced = false;

    public CacheBillCollectionEntity() {

    }

    public CacheBillCollectionEntity(int id, int collectionMode, String comments, int paidAmount, String transactionId, int billOutputCenterId) {
        this.billId = id;
        this.paidAmount = paidAmount;
        this.transactionId = transactionId;
        this.collectedDate = LocalDateTime.now().toString();
        this.collectionMode = collectionMode;
        this.comments = comments;
        this.collectionStatus = 4; // Status = 4: Done
        this.collectionShortageReason = 0;
        this.collectionMarkedById = Prefs.getInt(PrefConstants.AREA_INSPECTOR_ID);
        this.billOutputCenterId = billOutputCenterId;
        this.isSynced = false;
    }
}

