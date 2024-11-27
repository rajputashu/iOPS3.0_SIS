package com.sisindia.ai.android.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.sisindia.ai.android.BuildConfig;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseService;

import timber.log.Timber;

public class LocationService extends IopsBaseService {


    private final long UPDATE_INTERVAL_IN_MILLISECONDS = 2000;
    private final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Handler serviceHandler;
    private PowerManager.WakeLock wakeLock;
    public static Double latitude = 0.00, longitude = 0.00; //{Temporary solution: need to remove later}

    @Override
    public void onDestroy() {
        super.onDestroy();
        wakeLock.release();
        fusedLocationClient.removeLocationUpdates(locationCallback);
        serviceHandler.removeCallbacksAndMessages(null);
        stopForeground(STOP_FOREGROUND_REMOVE);
        stopSelf();

    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                Timber.e("Last location " + location.getLatitude() + "," + location.getLongitude());
                latitude = location.getLatitude(); //{Temporary solution: need to remove later}
                longitude = location.getLongitude();//{Temporary solution: need to remove later}
            }
        };
    }

    @SuppressLint("WakelockTimeout")
    private void startLocationUpdates() {
        wakeLock.acquire();
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback, Looper.myLooper());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if (powerManager != null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "iops:wakeLock");
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        createLocationCallback();

        createLocationRequest();


        HandlerThread handlerThread = new HandlerThread(LocationService.class.getSimpleName());
        handlerThread.start();
        serviceHandler = new Handler(handlerThread.getLooper());

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID;
        String channelName = LocationService.class.getCanonicalName();
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        channel.setLightColor(Color.BLUE);
        channel.setDescription(getString(R.string.string_duty_on));
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(channel);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle(getString(R.string.string_duty_on))
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startLocationUpdates();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}