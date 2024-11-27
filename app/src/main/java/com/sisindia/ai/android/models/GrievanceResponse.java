package com.sisindia.ai.android.models;

import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.room.entities.GrievanceEntity;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class GrievanceResponse {

    @SerializedName("data")
    public List<GrievanceEntity> grievances = new ArrayList<>();


    public GrievanceResponse() {
    }
}
