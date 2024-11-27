package com.sisindia.ai.android.rest;

import com.sisindia.ai.android.models.CommonResponse;
import com.sisindia.ai.android.models.geolocation.GeoLocationApiBodyMO;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GpsApi {

    String POST_GEO = "api/GPS/PostGeo";


    @POST(POST_GEO)
    Single<CommonResponse> uploadGeoPingsToServer(@Body GeoLocationApiBodyMO item);
}
