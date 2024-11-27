package com.droidcommons.dagger.module;

import dagger.Module;

//import com.readystatesoftware.chuck.ChuckInterceptor;
//import timber.log.Timber;

@Module
public class BaseRetrofitModule {

   /* @Provides
    @Singleton
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> Log.e("HttpLogs", message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    *//*@Provides
    @Singleton
    ChuckInterceptor provideChuckInterceptor(Context context) {
        return new ChuckInterceptor(context);
    }*//*

    @Provides
    @Singleton
    Gson provideGson() {

        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }*/
}
