package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Ashu Rajput on 12/15/2020.
 */
@Parcel
@Entity
public class RotaTasksEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("rotaId")
    public int rotaId;

    @SerializedName("siteTaskId")
    public int siteTaskId;

    @SerializedName("estimatedTravelTime")
    public int estimatedTravelTime;

    @SerializedName("estimatedDistance")
    public Double estimatedDistance;

    @SerializedName("sourceGeoString")
    public String sourceGeoString;

    @SerializedName("destinationGeoString")
    public String destinationGeoString;

    public RotaTasksEntity() {

    }
}