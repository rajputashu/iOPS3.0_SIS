package com.sisindia.ai.android.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.parceler.Parcel;
import org.threeten.bp.LocalDateTime;

@Parcel
@Entity
public class CheckedPostEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int postId;

    public int siteId;

    public int taskId;

    public int checkedPostStatus = CheckedPostStatus.PENDING.checkedPostStatus;

    public String checkedDateTime;

    public String updatedDateTime;

    public CheckedPostEntity() {
    }

    @Ignore
    public CheckedPostEntity(int postId, int siteId, int taskId) {
        this.postId = postId;
        this.taskId = taskId;
        this.siteId = siteId;
        this.checkedPostStatus = CheckedPostStatus.PENDING.checkedPostStatus;
        this.checkedDateTime = LocalDateTime.now().toString();
        this.updatedDateTime = LocalDateTime.now().toString();
    }

    public enum CheckedPostStatus {

        PENDING(1), COMPLETED(2);

        private final int checkedPostStatus;

        CheckedPostStatus(int checkedPostStatus) {
            this.checkedPostStatus = checkedPostStatus;
        }

        public static CheckedPostStatus of(int postStatus) {
            for (CheckedPostStatus type : CheckedPostStatus.values()) {
                if (type.checkedPostStatus == postStatus) {
                    return type;
                }
            }
            return PENDING;
        }

        public int getPostStatus() {
            return checkedPostStatus;
        }
    }
}

