package com.sisindia.ai.android.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class AuthResponse {

    @SerializedName("access_token")
    public String accessToken;

    @SerializedName("expires_in")
    public Long expiresIn;

    @SerializedName("token_type")
    public String tokenType;

    @SerializedName("scope")
    public String scope;

    @SerializedName("refresh_token")
    public String refreshToken;


    public AuthResponse() {
    }
}
