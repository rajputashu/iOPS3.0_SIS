package com.sisindia.ai.android.models.rota;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.base.BaseNetworkResponse;
import com.sisindia.ai.android.room.entities.RotaTasksEntity;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class RotaResponse extends BaseNetworkResponse {

    @SerializedName("data")
    public RotaResponseData rota;


    public RotaResponse() {
    }

    @Parcel
    public static class RotaResponseData {

        @SerializedName("id")
        public int id;

        @SerializedName("employeeId")
        public int employeeId;

        @SerializedName("rotaTypeId")
        public int rotaTypeId;

        @SerializedName("rotaWeek")
        public int rotaWeek;

        @SerializedName("rotaStatus")
        public int rotaStatus;

        @SerializedName("rotaMonth")
        public int rotaMonth;

        @SerializedName("rotaYear")
        public int rotaYear;

        @SerializedName("rotaDate")
        public String rotaDate;

        @SerializedName("rotaPublishedDate")
        public String rotaPublishedDate;

        @SerializedName("rotaTasks")
        public List<RotaTasksEntity> rotaTasks;
//        public List<RotaTaskResponse> rotaTasks;

        @SerializedName("siteTasks")
        public List<SiteTaskResponse> siteTasks;

        @SerializedName("inActiveRota")
        public List<Integer> inActiveRota;

        public RotaResponseData() {
        }
    }

   /* @Parcel
    public static class RotaTaskResponse {

        @SerializedName("id")
        public int id;

        @SerializedName("rotaId")
        public int rotaId;

        @SerializedName("siteTaskId")
        public int siteTaskId;

        @SerializedName("estimatedTravelTime")
        public int estimatedTravelTime;

        @SerializedName("sourceType")
        public int sourceType;

        @SerializedName("destinationType")
        public int destinationType;

        public RotaTaskResponse() {
        }
    }*/

    @Parcel
    public static class SiteTaskResponse {

        @SerializedName("id")
        public int id;

        @SerializedName("siteId")
        public int siteId;

        @SerializedName("taskTypeId")
        public int taskTypeId;

        @SerializedName("taskStatus")
        public int taskStatus;

        @SerializedName("estimatedExecutionTime")
        public int estimatedExecutionTime;

        @SerializedName("description")
        public String description;

        @SerializedName("estimatedTaskExecutionStartDateTime")
        public String estimatedTaskExecutionStartDateTime;

        @SerializedName("actualTaskExecutionStartDateTime")
        public String actualTaskExecutionStartDateTime;

        @SerializedName("estimatedTaskExecutionEndDateTime")
        public String estimatedTaskExecutionEndDateTime;

        @SerializedName("actualTaskExecutionEndDateTime")
        public String actualTaskExecutionEndDateTime;

        @SerializedName("createdDateTime")
        public String createdDateTime;

        @SerializedName("isAutoCreation")
        public boolean isAutoCreation;

        @SerializedName("isAllPostCheck")
        public boolean isAllPostCheck;

        @SerializedName("createdBy")
        public int createdBy;

        @SerializedName("reasonId")
        public int reasonLookUpIdentifier;

        @SerializedName("serverId")
        public int serverId;

        @SerializedName("otherTaskTypeId")
        public int otherTaskTypeLookUpIdentifier;

        @SerializedName("barrackId")
        public Integer barrackId;

        @SerializedName("taskExecutionResult")
        public String taskExecutionResult;

        @SerializedName("reasonText")
        public String reasonText;

        @SerializedName("approvedDateTime")
        public String approvedDateTime;

        @SerializedName("approvedBy")
        public Integer approvedBy;

        public SiteTaskResponse(int serverId) {
            this.serverId = serverId;
        }


        public SiteTaskResponse() {
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof SiteTaskResponse)) return false;
            SiteTaskResponse it = (SiteTaskResponse) obj;
            return serverId == it.serverId;
        }
    }
}
