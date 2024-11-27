package com.sisindia.ai.android.di.modules;

import com.droidcommons.dagger.qualifier.ServiceScoped;
import com.sisindia.ai.android.firebase.IopsMessagingService;
import com.sisindia.ai.android.services.GeoLocationService;
import com.sisindia.ai.android.services.LocalDBInitService;
import com.sisindia.ai.android.services.LocationService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceBindingModule {

    @ServiceScoped
    @ContributesAndroidInjector
    abstract LocationService bindLocationService();

    @ServiceScoped
    @ContributesAndroidInjector
    abstract LocalDBInitService bindLocalDBInitService();

    @ServiceScoped
    @ContributesAndroidInjector
    abstract GeoLocationService bindGeoLocationService();

    @ServiceScoped
    @ContributesAndroidInjector
    abstract IopsMessagingService bindFCMNotificationService();
}
