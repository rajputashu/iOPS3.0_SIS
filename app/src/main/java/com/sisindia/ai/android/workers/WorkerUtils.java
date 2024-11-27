package com.sisindia.ai.android.workers;

import android.annotation.SuppressLint;

import androidx.work.Constraints;
import androidx.work.NetworkType;

public class WorkerUtils {

    @SuppressLint("NewApi")
    public static Constraints networkConstraints() {
        return new Constraints.Builder()
                .setRequiresBatteryNotLow(false)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
    }
}
