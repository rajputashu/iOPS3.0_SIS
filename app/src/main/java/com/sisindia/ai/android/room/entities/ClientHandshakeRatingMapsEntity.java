package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Ashu Rajput on 4/14/2020.
 */
@Parcel
@Entity
public class ClientHandshakeRatingMapsEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("clientHandshakeQuestionId")
    public int clientHandshakeQuestionId;

    @SerializedName("ratingValue")
    public int ratingValue;

    @SerializedName("isActive")
    public boolean isActive;

    public ClientHandshakeRatingMapsEntity() {

    }
}
