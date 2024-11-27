package com.sisindia.ai.android.uimodels;

import org.parceler.Parcel;

@Parcel
public class SitePostModel {

    public int id;

    public String postName;

    public String dutyPostCode;

    public String siteAddress;

    public String description;

    public boolean qRenabled;

    public boolean isActive;

    public String createdDateTime;

    public int createdBy;

    public String updatedDateTime;

    public int updatedBy;

    public boolean isMainPost;

    public int siteId;

    public boolean spiEnabled;

    public SitePostModel() {
    }
}
