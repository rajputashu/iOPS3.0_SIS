package com.droidcommons.base;

import android.content.Context;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import dagger.android.support.DaggerApplication;

public abstract class BaseApplication extends DaggerApplication implements LifecycleObserver {

    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        setUpApplicationClass();
        initDebugLibs();
        initSharedPrefs();
        clearSharedPrefs();
        initApplication();

    }

    protected abstract void initApplication();

    protected abstract void setUpApplicationClass();

    protected abstract void initDebugLibs();

    protected abstract void initSharedPrefs();

    protected abstract void clearSharedPrefs();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
