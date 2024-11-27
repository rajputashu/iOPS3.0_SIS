package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity
public class PractoQuestionEntity {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public int id;

    @SerializedName("taskId")
    public int taskId;

    @SerializedName("siteId")
    public int siteId;

    @SerializedName("employeeId")
    public int employeeId;

    @SerializedName("employeeNo")
    public String employeeNo;

    @SerializedName("questionsJson")
    public String questionsJson;

    public PractoQuestionEntity() {
    }

}
