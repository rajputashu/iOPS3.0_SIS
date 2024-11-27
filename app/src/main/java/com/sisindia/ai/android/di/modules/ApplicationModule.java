package com.sisindia.ai.android.di.modules;

import android.content.Context;

import com.droidcommons.dagger.qualifier.ApplicationContext;
import com.sisindia.ai.android.IopsApplication;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    @Provides
    @ApplicationContext
    Context provideContext(IopsApplication application) {
        return application.getApplicationContext();
    }

    @Provides
    @ApplicationContext
    IopsApplication provideApplication(IopsApplication application) {
        return application;
    }

}