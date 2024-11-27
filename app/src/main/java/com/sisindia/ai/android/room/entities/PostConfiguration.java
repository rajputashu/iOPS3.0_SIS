package com.sisindia.ai.android.room.entities;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class PostConfiguration {

    @SerializedName("postRegisters")
    public List<PostRegisterEntity> postRegisters;

    @SerializedName("postChecklists")
    public List<PostCheckListEntity> postChecklists;

    public PostConfiguration() {
    }
}
