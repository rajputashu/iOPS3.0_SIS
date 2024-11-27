package com.sisindia.ai.android.uimodels;

import org.parceler.Parcel;

@Parcel
public class TaskTypeModel {

    public int id;

    public String name;

    public String description;

    public boolean isActive;

    public boolean isRotaType;

//    public int taskTypeConfig;

    public String taskTypeDefinition;

    public String createdDateTime;

    public int createdBy;

    public String updatedDateTime;

    public int udatedBy;

    public TaskTypeModel() {
    }
}
