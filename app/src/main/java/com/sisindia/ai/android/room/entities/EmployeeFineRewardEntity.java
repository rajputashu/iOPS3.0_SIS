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
public class EmployeeFineRewardEntity {

    @PrimaryKey(autoGenerate = true)
    public int ids;

    @SerializedName("id")
    public int rfId; // Storing RewardFine Id from API

    @SerializedName("guardId")
    public int employeeId; // Guard Employee Id eg. '12345'

    @SerializedName("employeeNo")
    public String employeeNo; //Guard Employee Number eg. 'Pat098767'

    @SerializedName("postId")
    public int postId;//postId where guard checked and reward fine added

    @SerializedName("siteId")
    public int siteId;//siteId where guard checked and reward fine added

    @SerializedName("taskId")
    public int taskId;//taskId where guard checked and reward fine added

    @SerializedName("typeId")
    public int rewardType = RewardType.UNKNOWN.rewardType; //UNKNOWN(0), REWARD(1), FINE(2);

    @SerializedName("rewardTypeId")
    public int rewardTypeId = RewardType.UNKNOWN.rewardType; //UNKNOWN(0), REWARD(1), FINE(2);

    @SerializedName("rewardLookUpId")
    public int rewardLookupIdentifier;

    @SerializedName("amount")
    public Double amount;

    @SerializedName("rewardFineValue")
    public String rewardFineValue;

    @SerializedName("status")
    public int status;

    @SerializedName("serverId")
    public int serverId; // {Will use serverId for RewardFine's id}

//    @SerializedName("isSync") // removing this to set default value of isSync = 1
    public boolean isSync = true;

    @SerializedName("rewardFineRequestId")
    public String localId;//UUID

    @SerializedName("reasonId")
    public int reasonId;

    @SerializedName("createdDateTime")
    public String createdDateTime;

    @SerializedName("updatedDateTime")
    public String updatedDateTime;

    @SerializedName("reasonName")
    public String reasonName;

    @SerializedName("sourceId")
    public int sourceId;

    @Ignore
    public EmployeeFineRewardEntity(int taskId, int postId, int siteId, int employeeId, int rewardType, int rewardLookupIdentifier, String rewardFineValue,
                                    int rewardTypeId, int reasonId, String reasonName, String employeeNo) {
        this.taskId = taskId;
        this.postId = postId;
        this.siteId = siteId;
        this.employeeId = employeeId;
        this.rewardLookupIdentifier = rewardLookupIdentifier;
        this.rewardType = rewardType;
        this.rewardFineValue = rewardFineValue;
        this.createdDateTime = LocalDateTime.now().toString();
        this.updatedDateTime = LocalDateTime.now().toString();
        this.localId = UUID.randomUUID().toString();
        this.isSync = false;
        this.rewardTypeId = rewardTypeId;
        this.sourceId = 1;
        this.reasonId = reasonId;
        this.reasonName = reasonName;
        this.employeeNo = employeeNo;
    }

    public EmployeeFineRewardEntity() {
    }

    public enum RewardType {

        UNKNOWN(0), REWARD(1), FINE(2);

        private final int rewardType;

        RewardType(int rewardType) {
            this.rewardType = rewardType;
        }

        public static RewardType of(int rewardType) {
            for (RewardType type : RewardType.values()) {
                if (type.rewardType == rewardType) {
                    return type;
                }
            }
            return UNKNOWN;
        }

        public int getRewardType() {
            return rewardType;
        }
    }
}

