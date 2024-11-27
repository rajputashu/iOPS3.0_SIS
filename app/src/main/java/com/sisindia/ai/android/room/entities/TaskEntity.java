package com.sisindia.ai.android.room.entities;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.commons.CheckInStatus;

import org.parceler.Parcel;
import org.threeten.bp.LocalDateTime;

import java.util.UUID;


@Parcel
@Entity(indices = {@Index(value = {"localId"}, unique = true)})
public class TaskEntity {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public int id;

    @SerializedName("isSynced")
    public boolean isSynced = false;

    @SerializedName("localId")
    public String localId;

    @SerializedName("siteId")
    public int siteId;

    @SerializedName("barrackId")
    public Integer barrackId;

    @SerializedName("serverId")
    public int serverId;

    @SerializedName("siteName")
    public String siteName;

    @SerializedName("siteAddress")
    public String siteAddress;

    @SerializedName("reasonText")
    public String reasonText;

    @SerializedName("reasonId") //todo remove value to reasonId
    public int reasonLookUpIdentifier;

//    @SerializedName("reasonId")
//    public int reasonId;

    @SerializedName("otherTaskTypeId")//todo value to otherTaskTypeId
    public Integer otherTaskTypeLookUpIdentifier;

//    @SerializedName("otherTaskTypeId")
//    public Integer otherTaskTypeId;

    @SerializedName("actualTaskExecutionStartDateTime")
    public String actualTaskExecutionStartDateTime;

    @SerializedName("actualTaskExecutionEndDateTime")
    public String actualTaskExecutionEndDateTime;

    @SerializedName("estimatedTaskExecutionStartDateTime")
    public String estimatedTaskExecutionStartDateTime;

    @SerializedName("estimatedTaskExecutionEndDateTime")
    public String estimatedTaskExecutionEndDateTime;

    @SerializedName("taskTypeId")
    public int taskTypeId;

    @SerializedName("taskStatus")
    public int taskStatus;

    @SerializedName("description")
    public String description;

    @SerializedName("taskExecutionResult")
    public String taskExecutionResult;

    @SerializedName("approvedDateTime")
    public String approvedDateTime;

    @SerializedName("approvedBy")
    public Integer approvedBy;

    @SerializedName("isAutoCreation")
    public boolean isAutoCreation = false;

    @SerializedName("taskLocation")
    public String taskGeoLocation;

    @SerializedName("timeElapsed")
    public long timeElapsed;

    //Last Elapsed time in millis(i.e. before pausing or quiting task in between or before completing)
    @SerializedName("lastElapsedTime")
    public long lastElapsedTime;

    @SerializedName("checkInStatus")
    public int checkInStatus = CheckInStatus.DEFAULT.getStatus();

    @SerializedName("checkInDateTime")
    public String checkInDateTime;

    public TaskEntity() {
    }

    @Ignore
    public TaskEntity(int reasonLookUpIdentifier, int taskTypeId, LocalDateTime taskStartDateTime, LocalDateTime taskEndDateTime, String siteName) {

        this.siteName = siteName;
        this.reasonLookUpIdentifier = reasonLookUpIdentifier;
        this.taskTypeId = taskTypeId;
        this.isAutoCreation = false;
        this.isSynced = false;
        this.estimatedTaskExecutionStartDateTime = taskStartDateTime.toString();
        this.estimatedTaskExecutionEndDateTime = taskEndDateTime.toString();
        this.taskStatus = TaskEntity.TaskStatus.NEW.getTaskStatus();
        /*String start = LocalDateTime.of(taskStartDateTime.getYear(), taskStartDateTime.getMonth(), taskStartDateTime.getDayOfMonth(), taskStartDateTime.getHour(), taskStartDateTime.getMinute()).toString();
        String end = LocalDateTime.of(taskEndDateTime.getYear(), taskEndDateTime.getMonth(), taskEndDateTime.getDayOfMonth(), taskEndDateTime.getHour(), taskEndDateTime.getMinute()).toString();
        this.localId = UUID.nameUUIDFromBytes(start.concat(end).getBytes()).toString();*/
        this.localId = UUID.randomUUID().toString();
    }

    @Ignore
    public TaskEntity(int siteId, int taskTypeId, int taskStatus, String description,
                      String estimatedTaskExecutionStartDateTime, String actualTaskExecutionStartDateTime,
                      String estimatedTaskExecutionEndDateTime, String actualTaskExecutionEndDateTime,
                      boolean isAutoCreation, int reasonId,
                      int serverId, Integer barrackId, String taskExecutionResult, int otherTaskTypeId) {
        this.siteId = siteId;
        this.taskTypeId = taskTypeId;
        this.otherTaskTypeLookUpIdentifier = otherTaskTypeId;
        this.taskStatus = taskStatus;
        this.description = description;
        this.estimatedTaskExecutionStartDateTime = estimatedTaskExecutionStartDateTime;
        this.estimatedTaskExecutionEndDateTime = estimatedTaskExecutionEndDateTime;
        this.actualTaskExecutionStartDateTime = actualTaskExecutionStartDateTime;
        this.actualTaskExecutionEndDateTime = actualTaskExecutionEndDateTime;
        this.isAutoCreation = isAutoCreation;
        this.reasonLookUpIdentifier = reasonId;
        this.serverId = serverId;
        this.barrackId = barrackId;
        /*LocalDateTime taskStartDateTime = LocalDateTime.parse(estimatedTaskExecutionStartDateTime);
        LocalDateTime taskEndDateTime = LocalDateTime.parse(estimatedTaskExecutionEndDateTime);
        String start = LocalDateTime.of(taskStartDateTime.getYear(), taskStartDateTime.getMonth(), taskStartDateTime.getDayOfMonth(), taskStartDateTime.getHour(), taskStartDateTime.getMinute()).toString();
        String end = LocalDateTime.of(taskEndDateTime.getYear(), taskEndDateTime.getMonth(), taskEndDateTime.getDayOfMonth(), taskEndDateTime.getHour(), taskEndDateTime.getMinute()).toString();
        this.localId = UUID.nameUUIDFromBytes(start.concat(end).getBytes()).toString();*/
        this.localId = UUID.randomUUID().toString();
        this.taskExecutionResult = taskExecutionResult;
        this.isSynced = true;
    }

    //#AR : Added another constructor to check pending issue
    @Ignore
    public TaskEntity(int siteId, int taskTypeId, int taskStatus, String estimatedTaskExecutionStartDateTime, String actualTaskExecutionStartDateTime,
                      String estimatedTaskExecutionEndDateTime, String actualTaskExecutionEndDateTime, boolean isAutoCreation, int reasonId,
                      int serverId, Integer barrackId, String taskExecutionResult, int otherTaskTypeId, String description) {
        this.siteId = siteId;
        this.taskTypeId = taskTypeId;
        this.otherTaskTypeLookUpIdentifier = otherTaskTypeId;
        this.taskStatus = taskStatus;
        this.description = description;
        this.estimatedTaskExecutionStartDateTime = estimatedTaskExecutionStartDateTime;
        this.estimatedTaskExecutionEndDateTime = estimatedTaskExecutionEndDateTime;
        this.actualTaskExecutionStartDateTime = actualTaskExecutionStartDateTime;
        this.actualTaskExecutionEndDateTime = actualTaskExecutionEndDateTime;
        this.isAutoCreation = isAutoCreation;
        this.reasonLookUpIdentifier = reasonId;
        this.serverId = serverId;
        this.barrackId = barrackId;
        this.localId = UUID.randomUUID().toString();
        this.taskExecutionResult = taskExecutionResult;
        this.isSynced = true;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof TaskEntity)) return false;
        TaskEntity it = (TaskEntity) obj;
        return serverId == it.serverId;
    }

    public enum TaskStatus {
        NEW(1),
        IN_PROGRESS(2),
        INACTIVE(3),
        COMPLETED(4);

        private final int taskStatus;

        TaskStatus(int taskStatus) {
            this.taskStatus = taskStatus;
        }

        public static TaskStatus of(int taskStatus) {
            for (TaskStatus type : TaskStatus.values()) {
                if (type.taskStatus == taskStatus) {
                    return type;
                }
            }
            return INACTIVE;
        }

        public int getTaskStatus() {
            return taskStatus;
        }
    }
}
