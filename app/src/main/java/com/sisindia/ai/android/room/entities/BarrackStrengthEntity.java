package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class BarrackStrengthEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("barrackId")
    public int barrackId;

    @SerializedName("rankId")
    public int rankId;

    @SerializedName("authorizedStrength")
    public int authorizedStrength;

    @SerializedName("grade")
    public String grade;


    public BarrackStrengthEntity() {
    }
}
