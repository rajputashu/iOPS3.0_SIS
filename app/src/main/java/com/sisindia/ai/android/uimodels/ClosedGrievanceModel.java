package com.sisindia.ai.android.uimodels;

import org.parceler.Parcel;

@Parcel
public class ClosedGrievanceModel {

    public int grievanceId;

    public String categoryName;

    public String siteName;

    public int pendingGrievances;

    public int totalGrievances;

    public int completedGrievances;

    public ClosedGrievanceModel() {
    }
}
