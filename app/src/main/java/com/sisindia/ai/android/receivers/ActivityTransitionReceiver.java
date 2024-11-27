package com.sisindia.ai.android.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionResult;

import java.util.Objects;

import dagger.android.DaggerBroadcastReceiver;
import timber.log.Timber;

/**
 * Created by Ashu Rajput on 12/28/2020.
 */
//public class ActivityTransitionReceiver extends DaggerBroadcastReceiver {
public class ActivityTransitionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Timber.e("ActivityDetection Coming in ActivityTransitionReceiver ");
        if (ActivityTransitionResult.hasResult(intent)) {
            ActivityTransitionResult result = ActivityTransitionResult.extractResult(intent);
            for (ActivityTransitionEvent event : Objects.requireNonNull(result).getTransitionEvents()) {
                Timber.e("ActivityDetection :TYPE ID %s", event.getActivityType());
            }
        }
    }
}
