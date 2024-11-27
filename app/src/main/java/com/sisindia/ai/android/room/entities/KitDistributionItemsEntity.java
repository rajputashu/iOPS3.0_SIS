package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Ashu Rajput on 8/20/2020.
 */

@Parcel
@Entity
public class KitDistributionItemsEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("kitDistributionId")
    public int kitDistributionId;

    @SerializedName("isIssued")
    public boolean isIssued;

    @SerializedName("issuedDate")
    public String issuedDate;

    @SerializedName("nonIssueReason")
    public String nonIssueReason;

    @SerializedName("nonIssueComments")
    public String nonIssueComments;

    @SerializedName("kitItemId")
    public int kitItemId;

    @SerializedName("itemSizeCode")
    public String itemSizeCode;

    @SerializedName("itemName")
    public String itemName;

    @SerializedName("itemCode")
    public String itemCode;

    @SerializedName("isSynced")
//    public boolean isSynced = false;
    public boolean isSynced = true;

    /*@SerializedName("reasonIdentifierId")
    public int reasonIdentifierId;*/

    public KitDistributionItemsEntity() {

    }
}
