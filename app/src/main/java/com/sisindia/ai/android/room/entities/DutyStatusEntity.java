package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.threeten.bp.LocalDateTime;

import java.util.UUID;

@Parcel
@Entity
public class DutyStatusEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("IsDutyON")
    public boolean isDutyOn;

    @SerializedName("DutyOnDateTime")
    public String dutyOnDateTime;

    @SerializedName("DutyOffDateTime")
    public String dutyOffDateTime;

    @SerializedName("DutyOnLocationString")
    public String dutyOnLocation;

    @SerializedName("DutyOffLocationString")
    public String dutyOffLocation;

    @SerializedName("IsSync")
    public boolean isSync;

    @SerializedName("DutyRequestId")
    public String localId;

    @SerializedName("ServerId")
    public int serverId;

    public DutyStatusEntity() {
    }

    @Ignore
    public DutyStatusEntity(boolean isDutyOn) {
        this.isDutyOn = isDutyOn;
        this.dutyOnDateTime = LocalDateTime.now().toString();
        this.isSync = false;
        this.localId = UUID.randomUUID().toString();
    }
}

