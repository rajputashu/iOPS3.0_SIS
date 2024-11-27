package com.sisindia.ai.android.di.modules;

import android.content.Context;

import androidx.lifecycle.ViewModelProvider;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sisindia.ai.android.BuildConfig;
import com.sisindia.ai.android.commons.RequestHeaderInterceptor;
import com.sisindia.ai.android.di.components.ViewModelSubComponent;
import com.sisindia.ai.android.di.factories.AppViewModelFactory;
import com.sisindia.ai.android.rest.AttachmentUploadAPI;
import com.sisindia.ai.android.rest.AuthServerApi;
import com.sisindia.ai.android.rest.CoreApi;
import com.sisindia.ai.android.rest.GpsApi;
import com.sisindia.ai.android.rest.MastersApi;
import com.sisindia.ai.android.utils.NullOnEmptyConverterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import timber.log.Timber;

@Module(subcomponents = {ViewModelSubComponent.class})
public class RetrofitModule {

    private final int CONNECT_TIMEOUT_MILLIS = 1500 * 1000; // 15s
    private final int READ_TIMEOUT_MILLIS = 2000 * 1000; // 20s

    @Provides
    @Singleton
    Retrofit provideIopsRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder().baseUrl(BuildConfig.IOPS_HOST).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @Named("CORE_API")
    Retrofit provideCoreApiRetrofit(@Named("CoreApiOkHttp") OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder().baseUrl(BuildConfig.CORE_API).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @Named("GPS_API")
    Retrofit provideGpsApiRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder().baseUrl(BuildConfig.GPS_API).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @Named("MASTER_API")
    Retrofit provideMasterApiRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder().baseUrl(BuildConfig.MASTER_API).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @Named("AUTHORIZATION_API")
    Retrofit provideAuthApiRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder().baseUrl(BuildConfig.AUTHORIZATION_API).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @Named("AzureFileUpload")
    Retrofit provideAzureRetrofit(@Named("AzureOkHttp") OkHttpClient okHttpClient) {
        return new Retrofit.Builder().baseUrl(BuildConfig.IOPS_HOST).client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @Named("AzureOkHttp")
    OkHttpClient provideAzureOkHttpClient(ChuckerInterceptor chuckInterceptor, HttpLoggingInterceptor loggingInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(chuckInterceptor);
            builder.addInterceptor(loggingInterceptor);
        }
        builder.connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        return builder.build();
    }

    @Provides
    @Singleton
    @Named("CoreApiOkHttp")
    OkHttpClient provideCodeApiOkHttpClient(RequestHeaderInterceptor headerInterceptor,
                                            ChuckerInterceptor chuckInterceptor, HttpLoggingInterceptor loggingInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(headerInterceptor);
        builder.retryOnConnectionFailure(false);
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(chuckInterceptor);
            builder.addInterceptor(loggingInterceptor);
        }
        builder.connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        return builder.build();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(RequestHeaderInterceptor headerInterceptor,
                                     ChuckerInterceptor chuckInterceptor,
                                     HttpLoggingInterceptor loggingInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS);
        builder.addInterceptor(headerInterceptor);
//        builder.addInterceptor(analyticsEventInterceptor);
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(chuckInterceptor);
            builder.addInterceptor(loggingInterceptor);
        }
        builder.connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        return builder.build();
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> Timber.d(message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    @Provides
    @Singleton
    ChuckerInterceptor provideChuckInterceptor(Context context) {
//        return new ChuckerInterceptor(context);
        return new ChuckerInterceptor.Builder(context).build();
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setLenient()
                .create();
    }

    @Provides
    @Singleton
    AuthServerApi providePreAuthApi(@Named("AUTHORIZATION_API") Retrofit retrofit) {
        return retrofit.create(AuthServerApi.class);
    }

    @Provides
    @Singleton
    MastersApi provideMastersApi(@Named("MASTER_API") Retrofit retrofit) {
        return retrofit.create(MastersApi.class);
    }

    @Provides
    @Singleton
    CoreApi provideCommonApi(@Named("CORE_API") Retrofit retrofit) {
        return retrofit.create(CoreApi.class);
    }

    @Provides
    @Singleton
    GpsApi provideGpsApi(@Named("GPS_API") Retrofit retrofit) {
        return retrofit.create(GpsApi.class);
    }

    @Provides
    @Singleton
    AttachmentUploadAPI provideAttachmentApi(@Named("AzureFileUpload") Retrofit retrofit) {
        return retrofit.create(AttachmentUploadAPI.class);
    }

    @Singleton
    @Provides
    ViewModelProvider.Factory provideFactory(ViewModelSubComponent.Builder builder) {
        return new AppViewModelFactory(builder.build());
    }
}
