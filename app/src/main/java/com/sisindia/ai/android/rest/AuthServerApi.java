package com.sisindia.ai.android.rest;

import com.sisindia.ai.android.models.AuthResponse;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.sisindia.ai.android.models.AuthorizationValues.CLIENT_ID_KEY;
import static com.sisindia.ai.android.models.AuthorizationValues.CLIENT_SECRETE_KEY;
import static com.sisindia.ai.android.models.AuthorizationValues.GRANT_TYPE_KEY;
import static com.sisindia.ai.android.models.AuthorizationValues.GRANT_TYPE_REFRESH_TOKEN_VALUE;
import static com.sisindia.ai.android.models.AuthorizationValues.PASSWORD;
import static com.sisindia.ai.android.models.AuthorizationValues.SCOPE;
import static com.sisindia.ai.android.models.AuthorizationValues.USER_NAME;

public interface AuthServerApi {

    String PRE_AUTH = "Connect/token";


    @POST(PRE_AUTH)
    @FormUrlEncoded
    Single<AuthResponse> getPreAuth(@Field(CLIENT_ID_KEY) String clientId,
                                    @Field(CLIENT_SECRETE_KEY) String clientSecrete,
                                    @Field(GRANT_TYPE_KEY) String grantType);

    @POST(PRE_AUTH)
    @FormUrlEncoded
    Single<AuthResponse> submitOtp(@Field(GRANT_TYPE_KEY) String grantType,
                                   @Field(USER_NAME) String userName,
                                   @Field(PASSWORD) String otp,
                                   @Field(CLIENT_ID_KEY) String roClientId,
                                   @Field(CLIENT_SECRETE_KEY) String clientSecrete,
                                   @Field(SCOPE) String appScope);

    @POST(PRE_AUTH)
    @FormUrlEncoded
    Single<AuthResponse> refreshToken(@Field(GRANT_TYPE_KEY) String grantType,
                                      @Field(CLIENT_ID_KEY) String roClientId,
                                      @Field(CLIENT_SECRETE_KEY) String clientSecrete,
                                      @Field(GRANT_TYPE_REFRESH_TOKEN_VALUE) String refreshToken);
}
