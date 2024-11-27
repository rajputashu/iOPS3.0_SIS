package com.sisindia.ai.android;

import static com.sisindia.ai.android.constants.PrefConstants.CLEAR_PREFS;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraXConfig;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.work.Worker;

import com.droidcommons.base.BaseApplication;
import com.droidcommons.dagger.worker.HasWorkerInjector;
import com.droidcommons.preference.Prefs;
import com.facebook.stetho.Stetho;
import com.google.firebase.FirebaseApp;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.di.components.DaggerIopsApplicationComponent;
import com.sisindia.ai.android.di.components.IopsApplicationComponent;
import com.sisindia.ai.android.services.GeoLocationService;
//import com.tspoon.traceur.Traceur;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.DaggerApplication;
import timber.log.Timber;

public class IopsApplication extends BaseApplication implements HasWorkerInjector, CameraXConfig.Provider {

    @Inject
    DispatchingAndroidInjector<Worker> workerDispatchingAndroidInjector;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        IopsApplicationComponent applicationComponent = DaggerIopsApplicationComponent.builder().application(this).build();
        applicationComponent.inject(this);
        return applicationComponent;
    }

    @Override
    protected void initApplication() {
        /*String currentVersion = "";
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            currentVersion = (info != null && !TextUtils.isEmpty(info.versionName)) ? info.versionName : "";
            String lastVersion = Prefs.getString(PrefConstants.LAST_VERSION, "");
            if (!currentVersion.equals(lastVersion)) {
                Prefs.clear();
                Prefs.putBoolean(CLEAR_PREFS, false);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Prefs.clear();
            Prefs.putBoolean(CLEAR_PREFS, false);
        } finally {
            Prefs.putString(LAST_VERSION, currentVersion);
        }*/
    }

    @Override
    protected void setUpApplicationClass() {
//        Application application = this;
        FirebaseApp.initializeApp(this);
        AndroidThreeTen.init(this);
        setUpNotification();
    }

    private void setUpNotification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID;
            String channelName = GeoLocationService.class.getCanonicalName();

            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
        }
    }

    @Override
    protected void initDebugLibs() {
        if (!BuildConfig.DEBUG)
            return;
        Timber.plant(new Timber.DebugTree());
//        Traceur.enableLogging();
        Stetho.initializeWithDefaults(this);
    }

    @Override
    protected void initSharedPrefs() {
        new Prefs.Builder().setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(BuildConfig.APPLICATION_ID)
                .setUseDefaultSharedPreference(true)
                .build();
    }

    @Override
    protected void clearSharedPrefs() {
        if (Prefs.getBoolean(CLEAR_PREFS, true)) {
            Timber.e("Clear Preference Called from Application...!!!");
            Prefs.clear();
            Prefs.putBoolean(CLEAR_PREFS, false);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        Prefs.putInt(PrefConstants.APPLICATION_STATE, 2);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {
        Prefs.putInt(PrefConstants.APPLICATION_STATE, 1);
    }

    @Override
    public AndroidInjector<Worker> workerInjector() {
        return workerDispatchingAndroidInjector;
    }

    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
    }
}
