package com.sisindia.ai.android.models;

import org.parceler.Parcel;

@Parcel
public class GrievanceModel {

    public int grievanceId;

    public String targetDateTime;

    public int grievanceStatus;

    public String categoryName;

    public int categoryId;

    public String guardName;

    public String siteAddress;

    public String employeeNo;

    public GrievanceModel() {
    }
}
