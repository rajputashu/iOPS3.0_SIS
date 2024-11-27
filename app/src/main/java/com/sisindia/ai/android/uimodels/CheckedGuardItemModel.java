package com.sisindia.ai.android.uimodels;

import org.parceler.Parcel;

@Parcel
public class CheckedGuardItemModel {

    public int kitItems;
    public int grievances;
    public int checkedGuardStatus;
    public int totalTurnOut;
    public int turnOutScore;
    public int employeeId;
    public int rewardType;
    public int guardStatus;
    public int currentState;
    public int taskId;
    public int siteId;
    public int postId;
    public String employeeName;
    public String employeeNo;
    public String postName;
    public String phone;
    public String deployedDate;
    public String siteName;

    public CheckedGuardItemModel() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CheckedGuardItemModel)) return false;
        CheckedGuardItemModel it = (CheckedGuardItemModel) obj;
        return employeeId == it.employeeId;
    }
}
