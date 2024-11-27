package com.sisindia.ai.android.commons;

import android.text.TextUtils;

import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.constants.PrefConstants;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestHeaderInterceptor implements Interceptor {

    @Inject
    public RequestHeaderInterceptor() {
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request.Builder requestBuilder = chain.request().newBuilder();

        requestBuilder.addHeader(HeaderConstants.CONTETN_TYPE_KEY, HeaderConstants.APPLICATION_JSON_VALUE);
        requestBuilder.addHeader(HeaderConstants.LATITUDE, String.valueOf(Prefs.getDouble(PrefConstants.LATITUDE)));
        requestBuilder.addHeader(HeaderConstants.LONGITUDE, String.valueOf(Prefs.getDouble(PrefConstants.LONGITUDE)));

        if (!TextUtils.isEmpty(Prefs.getString(PrefConstants.AUTH_TOKEN_KEY))) {
            requestBuilder.addHeader(HeaderConstants.AUTORIZATION_KEY, Prefs.getString(PrefConstants.AUTH_TOKEN_KEY));
        }

        Request request = requestBuilder.build();

        return chain.proceed(request);
    }

    public interface HeaderConstants {
        String CONTETN_TYPE_KEY = "Content-Type";
        String AUTORIZATION_KEY = "Authorization";
        String APPLICATION_JSON_VALUE = "application/json; charset=utf-8";
        String LATITUDE = "lat";
        String LONGITUDE = "long";
    }
}
