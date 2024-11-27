package com.sisindia.ai.android.uimodels;

import androidx.room.Ignore;

import org.parceler.Parcel;

@Parcel
public class ActionPlanModel {

    public int id;

    public String actionPlanName;

    public int closureDays;

    public boolean isActive;

    public int companyId;

    public ActionPlanModel() {
    }

    @Ignore
    public ActionPlanModel(String actionPlanName) {
        this.id = 0;
        this.actionPlanName = actionPlanName;
        this.closureDays = 0;
        this.isActive = false;
        this.companyId = 0;
    }
}
