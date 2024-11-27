package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class BranchExtensionEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    public BranchExtensionEntity() {
    }
}
