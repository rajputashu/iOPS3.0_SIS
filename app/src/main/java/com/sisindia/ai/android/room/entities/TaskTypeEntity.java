package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class TaskTypeEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public String description;

    @SerializedName("isActive")
    public boolean isActive;

    @SerializedName("isRotaType")
    public boolean isRotaType;

    /*@SerializedName("taskTypeConfig")
    public int taskTypeConfig;*/

    @Ignore
    @SerializedName("taskTypeConfig")
    public String taskTypeConfig;

    @SerializedName("taskTypeDefinition")
    public String taskTypeDefinition;

    @SerializedName("createdDateTime")
    public String createdDateTime;

    @SerializedName("createdBy")
    public int createdBy;

    @SerializedName("updatedDateTime")
    public String updatedDateTime;

    @SerializedName("udatedBy")
    public int udatedBy;


    public TaskTypeEntity() {
    }
}
