package com.sisindia.ai.android.rest;

import com.sisindia.ai.android.models.CommonMasterDataResponse;
import com.sisindia.ai.android.models.UserMasterDataResponse;
import com.sisindia.ai.android.models.UserMasterDataResponseV2;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface MastersApi {

    String COMMON_MASTER_DATA = "api/MasterData/CommonMasterData";

    String USER_MASTER_DATA = "api/MasterData/UserMasterData";
    String USER_MASTER_DATA_V2 = "api/MasterData/UserMasterDataV2";


    @GET(COMMON_MASTER_DATA)
    Single<CommonMasterDataResponse> getCommonMasterData();

    @GET(USER_MASTER_DATA)
    Single<UserMasterDataResponse> getUserMasterData();

    @GET(USER_MASTER_DATA_V2)
    Single<UserMasterDataResponseV2> getUserMasterDataV2();

}
