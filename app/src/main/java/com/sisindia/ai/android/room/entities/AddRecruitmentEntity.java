package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.droidcommons.preference.Prefs;
import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.constants.PrefConstants;

import org.parceler.Parcel;
import org.threeten.bp.LocalDateTime;

import java.util.UUID;

/**
 * Created by Ashu Rajput on 5/7/2020.
 */
@Parcel
@Entity
public class AddRecruitmentEntity {

    public AddRecruitmentEntity() {

    }

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public int id;

    @SerializedName("recruitGuid")
    public String recruitGuid;

    @SerializedName("fullName")
    public String recruitName;

    @SerializedName("mobileNo")
    public String recruitPhoneNumber;

    @SerializedName("referredById")
    public int referredById;

    @SerializedName("referredByName")
    public String referredByName;

    @SerializedName("referredByEmployeeNo")
    public String referredByEmployeeNo;

    @SerializedName("recruitStatus")
    public int recruitStatus;

    @SerializedName("selectedEmployeeId")
    public int selectedEmployeeId;

    @SerializedName("referredDateTime")
    public String referredDateTime;

    @Ignore
    @SerializedName("PanCard")
    public String panCard;

    @SerializedName("dateofBirth")
    public String dateOfBirth;

    @SerializedName("isSynced")
    public boolean isSynced;

    @Ignore
    public AddRecruitmentEntity(String recruitName, String recruitPhoneNumber, String panCard, String dateOfBirth) {
        this.recruitName = recruitName;
        this.referredById = Prefs.getInt(PrefConstants.AREA_INSPECTOR_ID);
        this.referredByName = Prefs.getString(PrefConstants.AREA_INSPECTOR_NAME);
        this.referredByEmployeeNo = panCard;
        this.recruitGuid = UUID.randomUUID().toString();
        this.recruitPhoneNumber = recruitPhoneNumber;
//        this.panCard = panCard;
        this.dateOfBirth = dateOfBirth;
//        this.dateOfBirth = "2090-05-19T12:32:18.591";
        this.isSynced = false;
        this.selectedEmployeeId = 0;
        this.recruitStatus = 1;
        this.referredDateTime = LocalDateTime.now().toString();
    }
}
