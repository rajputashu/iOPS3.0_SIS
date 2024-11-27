package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Ashu Rajput on 4/22/2020.
 */
@Parcel
@Entity
public class BarrackTaggingEntity {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public int id;

    @SerializedName("localId")
    public String localId;

    @SerializedName("barrackId")
    public int barrackId;

    @SerializedName("barrackName")
    public String barrackName;

    @SerializedName("barrackUnitCode")
    public String barrackUnitCode;

    @SerializedName("barrackLat")
    public Double barrackLat;

    @SerializedName("barrackLong")
    public Double barrackLong;

    @SerializedName("srNumber")
    public String srNumber;

    @SerializedName("createdDateTime")
    public String createdDateTime;

    @SerializedName("updatedDateTime")
    public String updatedDateTime;

    @SerializedName("isSynced")
    public int isSynced;

    public BarrackTaggingEntity() {

    }
}
