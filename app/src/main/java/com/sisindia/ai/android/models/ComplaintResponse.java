package com.sisindia.ai.android.models;

import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.room.entities.ComplaintEntity;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class ComplaintResponse {

    @SerializedName("data")
    public List<ComplaintEntity> complaints = new ArrayList<>();


    public ComplaintResponse() {
    }
}
