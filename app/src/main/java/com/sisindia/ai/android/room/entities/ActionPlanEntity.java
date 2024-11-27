package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
@Entity
public class ActionPlanEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("actionPlanName")
    public String actionPlanName;

    @SerializedName("closureDays")
    public int closureDays;

    @SerializedName("isActive")
    public boolean isActive;

    @SerializedName("companyId")
    public int companyId;

    @Ignore
    @SerializedName("actionPlanMaps")
    public List<ActionPlanMapEntity> actionPlanMaps;


    public ActionPlanEntity() {
    }


}
