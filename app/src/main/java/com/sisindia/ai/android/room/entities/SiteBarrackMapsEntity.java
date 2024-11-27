package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Ashu Rajput on 5/20/2020.
 */
@Parcel
@Entity
public class SiteBarrackMapsEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("siteId")
    public int siteId;

    @SerializedName("barrackId")
    public int barrackId;

    @SerializedName("isActive")
    public boolean isActive;

    public SiteBarrackMapsEntity() {

    }
}
