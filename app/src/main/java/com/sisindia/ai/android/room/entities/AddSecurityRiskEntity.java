package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.parceler.Parcel;
import org.threeten.bp.LocalDateTime;

@Parcel
@Entity
public class AddSecurityRiskEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String addSecurityGuid;

    public int categoryId;

    public String remarks;

    public int siteId;

    public int taskId;

    public int postId;

    public int addSecurityRiskStatus;

    public String updateDateTime;

    public String createdDateTime;

    public AddSecurityRiskEntity() {
    }

    @Ignore
    public AddSecurityRiskEntity(int taskId, int siteId, int postId, int categoryId, String remarks) {
        this.taskId = taskId;
        this.siteId = siteId;
        this.postId = postId;
        this.categoryId = categoryId;
        this.remarks = remarks;
        this.addSecurityRiskStatus = AddSecurityRiskStatus.COMPLETED.addSecurityRiskStatus;
        this.createdDateTime = LocalDateTime.now().toString();
        this.updateDateTime = LocalDateTime.now().toString();
    }


    public enum AddSecurityRiskStatus {

        PENDING(1), COMPLETED(2);

        private final int addSecurityRiskStatus;

        AddSecurityRiskStatus(int checkedGuardStatus) {
            this.addSecurityRiskStatus = checkedGuardStatus;
        }

        public static AddSecurityRiskStatus of(int addSecurityRiskStatus) {
            for (AddSecurityRiskStatus type : AddSecurityRiskStatus.values()) {
                if (type.addSecurityRiskStatus == addSecurityRiskStatus) {
                    return type;
                }
            }
            return PENDING;
        }

        public int getAddSecurityRiskStatus() {
            return addSecurityRiskStatus;
        }
    }
}

