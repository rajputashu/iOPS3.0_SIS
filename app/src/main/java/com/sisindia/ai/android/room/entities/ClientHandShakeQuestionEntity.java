package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
@Entity
public class ClientHandShakeQuestionEntity {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("companyId")
    public int companyId;

    @SerializedName("appTypeId")
    public int appTypeId;

    @SerializedName("questionTypeId")
    public int questionTypeId;

    @SerializedName("question")
    public String question;

    @SerializedName("answerLookupTypeId")
    public int answerLookupTypeId;

    @SerializedName("isActive")
    public boolean isActive;

    @Ignore
    @SerializedName("clientHandshakeRatingMaps")
    public List<ClientHandshakeRatingMapsEntity> clientHandshakeRatingMaps;

    public ClientHandShakeQuestionEntity() {
    }
}
