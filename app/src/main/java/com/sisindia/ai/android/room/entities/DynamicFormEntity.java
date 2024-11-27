package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Ashu_Rajput on 6/26/2021.
 */
@Parcel
@Entity
public class DynamicFormEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("dynamicTaskTypeId")
    public int dynamicTaskTypeId;

    @SerializedName("form")
    public String form;

    public DynamicFormEntity() {
    }

    public DynamicFormEntity(int dynamicTaskTypeId, String form) {
        this.id = dynamicTaskTypeId;
        this.dynamicTaskTypeId = dynamicTaskTypeId;
        this.form = form;
    }
}
