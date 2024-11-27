package com.sisindia.ai.android.di.modules;

import com.sisindia.ai.android.firebase.NotificationHandler;
import com.sisindia.ai.android.rest.CoreApi;
import com.sisindia.ai.android.room.IopsDatabase;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Ashu Rajput on 1/12/2021.
 */
@Module(includes = {RetrofitModule.class, IopsDatabaseModule.class})
public class NotificationHandlerModule {

    @Singleton
    @Provides
    @Named("@NotificationHandler")
    NotificationHandler getNotificationHandler(CoreApi coreApi, IopsDatabase iopsDatabase) {
        return new NotificationHandler(coreApi, iopsDatabase);
    }
}
