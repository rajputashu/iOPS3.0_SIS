package com.sisindia.ai.android.room.entities;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class SiteConfiguration {

    @SerializedName("siteChecklists")
    public List<SiteCheckListEntity> siteChecklists;

    public SiteConfiguration() {
    }
}
