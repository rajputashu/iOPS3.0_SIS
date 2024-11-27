package com.sisindia.ai.android.services;

import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.sisindia.ai.android.base.IopsBaseIntentService;

public class LocalDBInitService extends IopsBaseIntentService {

    public LocalDBInitService() {
        super(LocalDBInitService.class.getSimpleName());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }


}
