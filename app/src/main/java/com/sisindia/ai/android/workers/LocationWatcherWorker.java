package com.sisindia.ai.android.workers;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import com.droidcommons.dagger.worker.AndroidWorkerInjection;
import com.droidcommons.permissions.RunTimePermissions;
import com.droidcommons.preference.Prefs;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.sisindia.ai.android.base.BaseWorker;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.services.GeoLocationService;

import javax.inject.Inject;

import timber.log.Timber;

public class LocationWatcherWorker extends BaseWorker {

    private final Context context;
    private SettingsClient settingsClient;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationRequest locationRequest;

    @Inject
    public LocationWatcherWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        AndroidWorkerInjection.inject(this);
    }

    @NonNull
    @Override
    public Result doWork() {
        boolean isDutyOn = Prefs.getBoolean(PrefConstants.IS_ON_DUTY);
        if (!isDutyOn) {
            Timber.e("LocationWatcherWorker coming to stop the geo service");
            stopService();
        } else {
            if (!isMyServiceRunning()) {
                settingsClient = LocationServices.getSettingsClient(context);
                buildLocationSettings();
                createLocationRequest();
                fetchUserLocation();
            }
        }
        return Result.success();
    }

    private void buildLocationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.setAlwaysShow(true).build();
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(30000); //{30 Seconds}
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void fetchUserLocation() {
        if (RunTimePermissions.checkLocationPermissions(context)) {
            // Begin by checking if the device has the necessary location settings.
            settingsClient.checkLocationSettings(locationSettingsRequest)
                    .addOnSuccessListener(command -> {
                        Timber.d("Location Settings Success..");
                        startLocationService();
                    })
                    .addOnFailureListener(command -> Timber.e("Location Setting Failed"));
        }
    }

    private void startLocationService() {
        if (Prefs.getInt(PrefConstants.APPLICATION_STATE) == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context.startForegroundService(new Intent(context, GeoLocationService.class));
            else
                context.startService(new Intent(context, GeoLocationService.class));
        }
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null)
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (GeoLocationService.class.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        return false;
    }

    private void stopService() {
        context.stopService(new Intent(context, GeoLocationService.class));
    }

    @Override
    public void onStopped() {
        super.onStopped();
    }
}