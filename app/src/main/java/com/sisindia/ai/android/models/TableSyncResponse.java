package com.sisindia.ai.android.models;

import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.base.BaseNetworkResponse;

import org.parceler.Parcel;

@Parcel
public class TableSyncResponse extends BaseNetworkResponse {

    @SerializedName("data")
    public TableSyncData data;

    public TableSyncResponse() {
    }


    @Parcel
    public static class TableSyncData {

        @SerializedName("serverId")
        public int serverId;

        @SerializedName("localId")
        public String localId;

        public TableSyncData() {
        }
    }
}
