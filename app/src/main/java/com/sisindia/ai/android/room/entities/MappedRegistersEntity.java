package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Ashu Rajput on 10/27/2020.
 */
@Parcel
@Entity
public class MappedRegistersEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("registerName")
    public String registerName;

    @SerializedName("scopeId")
    public int scopeId;

    @SerializedName("scopeLevel")
    public int scopeLevel;

    @SerializedName("isSynced")
    public boolean isSynced;

    @SerializedName("isMapped")
    public boolean isMapped;

    public MappedRegistersEntity() {
    }
}
