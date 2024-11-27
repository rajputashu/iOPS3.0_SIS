package com.sisindia.ai.android.uimodels;

import androidx.room.Ignore;

import org.parceler.Parcel;

@Parcel
public class GuardSuggestionItem {

    public int employeeId;

    public String employeeName;

    public String employeeNo;

    public String phone;

    public String emailId;

    public int siteId;

    public int rankId;

    public boolean isActive;


    public GuardSuggestionItem() {
    }

    @Ignore
    public GuardSuggestionItem(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    @Ignore
    public GuardSuggestionItem(int employeeId) {
        this.employeeId = employeeId;
    }

    public GuardSuggestionItem(int employeeId, String employeeNo, String employeeName) {
        this.employeeId = employeeId;
        this.employeeNo = employeeNo;
        this.employeeName = employeeName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof GuardSuggestionItem)) return false;
        GuardSuggestionItem it = (GuardSuggestionItem) obj;
        return employeeNo.equalsIgnoreCase(it.employeeNo);
    }
}
