package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.parceler.Parcel;
import org.threeten.bp.LocalDateTime;

@Parcel
@Entity
public class CheckedGuardEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int employeeId;

    public String employeeNo;

    public int postId;

    public int siteId;

    public int taskId;

    public int serverId;

    public int checkedGuardStatus = CheckedGuardStatus.PENDING.checkedGuardStatus;

    public int guardStatus = GuardStatus.UNKNOWN.guardStatus;

    public String sleepingGuardGuid;

    public String guardEvaluationGuid;

    public String guardSignatureGuid;

    public int fineRewardId;

    public int turnOutScore;

    public int totalTurnOut;

    public int notAvailableStatus;

    public String guardNotAvailableRemarks;

    public String guardEvaluationResult;
    public String mlGuardEvaluationResult;
    public String checkedDateTime;

    public String updatedDateTime;

    public int currentState = CurrentState.UNKNOWN.currentState;

    public boolean isFakeGuardImage = false;

    public CheckedGuardEntity() {
    }

    /**
     * ON GUARD AVAILABLE
     */
    @Ignore
    public CheckedGuardEntity(int employeeId, int postId, int siteId, int taskId, int serverId) {
        this.employeeId = employeeId;
        this.postId = postId;
        this.siteId = siteId;
        this.taskId = taskId;
        this.serverId = serverId;
        this.guardStatus = GuardStatus.AVAILABLE.guardStatus;
        this.checkedGuardStatus = CheckedGuardStatus.PENDING.checkedGuardStatus;
        this.checkedDateTime = LocalDateTime.now().toString();
        this.updatedDateTime = LocalDateTime.now().toString();
    }

    /**
     * ON GUARD NOT AVAILABLE
     */
    @Ignore
    public CheckedGuardEntity(int taskId, int postId, int siteId, int employeeId, int notAvailableStatus,
                              String notAvailableRemarks, int serverId, String employeeNo) {
        this.taskId = taskId;
        this.postId = postId;
        this.siteId = siteId;
        this.employeeId = employeeId;
        this.serverId = serverId;
        this.guardStatus = GuardStatus.NOT_AVAILABLE.guardStatus;
        this.notAvailableStatus = notAvailableStatus;
        this.guardNotAvailableRemarks = notAvailableRemarks;
        this.checkedGuardStatus = CheckedGuardStatus.COMPLETED.checkedGuardStatus;
        this.checkedDateTime = LocalDateTime.now().toString();
        this.updatedDateTime = LocalDateTime.now().toString();
        this.employeeNo = employeeNo;
    }

    /**
     * ON GUARD SLEEPING
     */
    @Ignore
    public CheckedGuardEntity(int employeeId, int postId, int siteId, int taskId, String sleepingGuardGuid, int serverId) {
        this.employeeId = employeeId;
        this.postId = postId;
        this.siteId = siteId;
        this.taskId = taskId;
        this.serverId = serverId;
        this.sleepingGuardGuid = sleepingGuardGuid;
        this.guardStatus = GuardStatus.SLEEPING.guardStatus;
        this.checkedGuardStatus = CheckedGuardStatus.PENDING.checkedGuardStatus;
        this.checkedDateTime = LocalDateTime.now().toString();
        this.updatedDateTime = LocalDateTime.now().toString();
    }

    public enum CheckedGuardStatus {

        PENDING(1), COMPLETED(2);

        private final int checkedGuardStatus;

        CheckedGuardStatus(int checkedGuardStatus) {
            this.checkedGuardStatus = checkedGuardStatus;
        }

        public static CheckedGuardStatus of(int checkedGuardStatus) {
            for (CheckedGuardStatus type : CheckedGuardStatus.values()) {
                if (type.checkedGuardStatus == checkedGuardStatus) {
                    return type;
                }
            }
            return PENDING;
        }

        public int getCheckedGuardStatus() {
            return checkedGuardStatus;
        }
    }


    public enum GuardStatus {

        UNKNOWN(1),
        SLEEPING(2),
        NOT_AVAILABLE(3),
        AVAILABLE(4);

        private final int guardStatus;

        GuardStatus(int guardStatus) {
            this.guardStatus = guardStatus;
        }

        public static GuardStatus of(int guardStatus) {
            for (GuardStatus type : GuardStatus.values()) {
                if (type.guardStatus == guardStatus) {
                    return type;
                }
            }
            return UNKNOWN;
        }

        public int getGuardStatus() {
            return guardStatus;
        }
    }

    public enum CurrentState {

        UNKNOWN(0),
        EVALUATION(1),
        REWARD_FINE(2),
        SIGNATURE(3);

        private final int currentState;

        CurrentState(int currentState) {
            this.currentState = currentState;
        }

        public static CurrentState of(int currentState) {
            for (CurrentState type : CurrentState.values()) {
                if (type.currentState == currentState) {
                    return type;
                }
            }
            return UNKNOWN;
        }

        public int getGuardStatus() {
            return currentState;
        }


    }
}

