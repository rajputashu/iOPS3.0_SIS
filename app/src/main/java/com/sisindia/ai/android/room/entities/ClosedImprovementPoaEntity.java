package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.threeten.bp.LocalDateTime;

/**
 * Created by Ashu Rajput on 12/26/2020.
 */
@Parcel
@Entity
public class ClosedImprovementPoaEntity {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public int id;

    @SerializedName("poaId")
    public int poaId;

    @SerializedName("closingDate")
    public String closingDate;

    @SerializedName("closingRemarks")
    public String closingRemarks;

    @SerializedName("isSynced")
    public boolean isSynced = false;

    public ClosedImprovementPoaEntity() {

    }

    public ClosedImprovementPoaEntity(int poaId, String closingRemarks) {
        this.poaId = poaId;
        this.closingRemarks = closingRemarks;
        this.closingDate = LocalDateTime.now().toString();
    }
}
