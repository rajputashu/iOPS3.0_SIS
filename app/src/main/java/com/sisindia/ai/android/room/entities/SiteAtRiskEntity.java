package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Ashu Rajput on 4/6/2020.
 */
@Parcel
@Entity
public class SiteAtRiskEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("siteId")
    public int siteId;

    @SerializedName("riskMonth")
    public int riskMonth;

    @SerializedName("riskYear")
    public int riskYear;

    @SerializedName("riskStatus")
    public int riskStatus;

    @SerializedName("finalClosureDate")
    public String finalClosureDate;

    @SerializedName("createdByAppId")
    public int createdByAppId;

    @Ignore
    @SerializedName("siteRiskPos")
    public List<SiteRiskPoaEntity> siteRiskPos;

    public SiteAtRiskEntity() {
    }
}
