package com.sisindia.ai.android.models;

import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.base.BaseNetworkResponse;
import com.sisindia.ai.android.room.entities.EmployeeSiteEntity;

public class EmployeeResponse extends BaseNetworkResponse {

    @SerializedName("data")
    public EmployeeSiteEntity emp;

    public EmployeeResponse() {
    }
}
