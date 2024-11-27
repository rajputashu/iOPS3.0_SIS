package com.sisindia.ai.android.models;

import com.google.gson.annotations.SerializedName;
import com.sisindia.ai.android.base.BaseNetworkResponse;

/**
 * Created by Ashu Rajput on 4/27/2020.
 */
public class CommonResponse extends BaseNetworkResponse {

    @SerializedName("data")
    public boolean isDone;

    public CommonResponse() {
    }
}
