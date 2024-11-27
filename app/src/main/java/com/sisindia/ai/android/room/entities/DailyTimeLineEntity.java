package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.droidcommons.preference.Prefs;
import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.commons.RequestHeaderInterceptor;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.services.GeoLocationService;

import org.parceler.Parcel;
import org.threeten.bp.LocalDateTime;

import java.util.UUID;

@Parcel
@Entity
public class DailyTimeLineEntity {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public int id;

    @SerializedName("localId")
    public String localId;

    @SerializedName("activityType")
    public String activityType;

    @SerializedName("createdDateTime")
    public String createdDateTime;

    @SerializedName("unitCode")
    public String unitCode;

    @SerializedName("geoLocation")
    public String geoLocation;

    @SerializedName("serverId")
    public int serverId;

    @SerializedName("isSynced")
    public boolean isSynced;

    public DailyTimeLineEntity() {
    }

    @Ignore
    public DailyTimeLineEntity(String activityType, String unitCode) {
        this.activityType = activityType;
        this.unitCode = unitCode;
        this.localId = UUID.randomUUID().toString();
        this.createdDateTime = LocalDateTime.now().toString();
        this.geoLocation = Prefs.getDouble(PrefConstants.LATITUDE) + ", " + Prefs.getDouble(PrefConstants.LONGITUDE);
    }
}
