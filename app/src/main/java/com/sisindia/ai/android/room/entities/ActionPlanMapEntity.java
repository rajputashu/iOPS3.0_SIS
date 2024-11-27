package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class ActionPlanMapEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("actionPlanMapTypeId")
    public int actionPlanMapTypeId;

    @SerializedName("actionPlanId")
    public int actionPlanId;

    @SerializedName("categoryId")
    public int categoryId;

    @SerializedName("subCategoryId")
    public int subCategoryId;

    @SerializedName("isActive")
    public boolean isActive;

    public ActionPlanMapEntity() {
    }

    @Ignore
    public ActionPlanMapEntity(int id, int actionPlanMapTypeId, int actionPlanId, int categoryId, int subCategoryId, boolean isActive) {
        this.id = id;
        this.actionPlanMapTypeId = actionPlanMapTypeId;
        this.actionPlanId = actionPlanId;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.isActive = isActive;
    }

    /**
     * actionPlanMapTypeId
     * 1->GRIEVANCE
     * 2->COMPLAINT
     * 3->IMPROVEMENT_PLAN
     * 4->POA
     * */
}
