package com.sisindia.ai.android.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;

import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.BuildConfig;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseService;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.models.geolocation.GPSLocationMO;
import com.sisindia.ai.android.models.geolocation.GeoLocationApiBodyMO;
import com.sisindia.ai.android.rest.CoreApi;
import com.sisindia.ai.android.rest.GpsApi;
import com.sisindia.ai.android.room.dao.GeoLocationDao;
import com.sisindia.ai.android.room.entities.GeoLocationEntity;
import com.sisindia.ai.android.utils.IopsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Ashu Rajput on 4/28/2020.
 */
public final class GeoLocationService extends IopsBaseService {

    private static final String TAG = GeoLocationService.class.getSimpleName();
    private static final long LOCATION_CAPTURE_INTERVAL = 30000;
    //    private static final long ACTIVITY_CAPTURE_INTERVAL = 25000;
    private static final long LOCATION_SERVER_POOLING_INTERVAL = 60000;
    public static double latitude = 0.0;
    public static double longitude = 0.0;
    private final Handler handler = new Handler();

    @Inject
    GeoLocationDao geoLocationDao;

    @Inject
    CoreApi coreApi;

    @Inject
    GpsApi gpsApi;

    @Override
    public void onCreate() {
        super.onCreate();
        startLocationUpdates();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        // Uncomment this line
        if (intent != null)
            WakefulBroadcastReceiver.completeWakefulIntent(intent);

        final PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        final PowerManager.WakeLock wakeLock = Objects.requireNonNull(powerManager).newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, TAG);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else {
            final Notification notification = createNotification();
            final int NOTIFICATION_ID = 1337;
            startForeground(NOTIFICATION_ID, notification);
        }

        Runnable captureGps = new Runnable() {
            @SuppressLint("WakelockTimeout")
            @Override
            public void run() {
                if (Prefs.getBoolean(PrefConstants.IS_ON_DUTY)) {
                    insertGeoLocationToDB();
                    if (wakeLock != null && !wakeLock.isHeld())
                        wakeLock.acquire();
                    handler.postDelayed(this, LOCATION_CAPTURE_INTERVAL);
                } else {
                    final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Objects.requireNonNull(notificationManager).cancelAll();
                    if (wakeLock != null && wakeLock.isHeld())
                        wakeLock.release();
                    stopLocationUpdates();
                    stopForeground(true);
                    stopSelf();
                }
            }
        };

        Runnable sendToServer = new Runnable() {
            @Override
            public void run() {
                addDisposable(geoLocationDao.fetchAllGeoPings()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(list -> {
                            if (IopsUtil.isInternetAvailable(getApplication())) // Added this check
                                uploadGeoPingsToServer(list);
                        }, Timber::e));
                handler.postDelayed(this, LOCATION_SERVER_POOLING_INTERVAL);
            }
        };

        handler.post(captureGps);
        handler.post(sendToServer);
        return START_STICKY;
    }

    private void uploadGeoPingsToServer(List<GPSLocationMO> pingList) {
        if (!pingList.isEmpty()) {
            GeoLocationApiBodyMO geoLocationBody = new GeoLocationApiBodyMO(pingList);
            addDisposable(gpsApi.uploadGeoPingsToServer(geoLocationBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(task -> {
                        if (task.isDone) {
                            ArrayList<Integer> ids = new ArrayList<>(pingList.size());
                            for (GPSLocationMO model : pingList)
                                ids.add(model.pingId);
                            deletingUploadedGeoPingsToServer(ids);
                        }
                    }, Timber::e));
        }
    }

    private void deletingUploadedGeoPingsToServer(ArrayList<Integer> ids) {
        if (ids.size() > 0) {
            addDisposable(geoLocationDao.deleteUploadedPings(ids)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(count -> {
                    }, Timber::e));
        }
    }

    private Notification createNotification() {
        String channelId = "";
        return new NotificationCompat.Builder(this, channelId)
                .setContentTitle(getString(R.string.string_duty_on))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.logo_sis_splash))
                .setContentText("Have a great day at work")
//                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setColorized(true)
//                .setLargeIcon(Bitmap.createScaledBitmap(logoIcon, 128, 128, false))
                .build();
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID;
        /*String channelName = GeoLocationService.class.getCanonicalName();

        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);*/

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.logo_sis_splash))
                .setContentTitle(getString(R.string.string_duty_on))
                .setContentText("Have a great day at work \uD83D\uDE00")
//                .setContentIntent(pendingIntent)
//                .setLargeIcon(Bitmap.createScaledBitmap(logoIcon, 128, 128, false))
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setColorized(true)
                .build();
        startForeground(1010, notification);
    }

    private void insertGeoLocationToDB() {
        if (latitude != 0.0 && longitude != 0.0) {
            addDisposable(geoLocationDao.insert(new GeoLocationEntity(latitude,
                            longitude, IopsUtil.getBatteryLevel(this)))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(row -> Timber.d("inserted GPS "), Timber::e));
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //Note : alternate to SmartLocation we can use FusedLocationProviderClient
        LocationParams params = (new LocationParams.Builder()).setAccuracy(LocationAccuracy.HIGH).setDistance(0.0F).setInterval(500L).build();
        SmartLocation.with(this).location().config(params).start(location -> {
            if (location != null && location.getLatitude() != 0 && location.getLongitude() != 0) {
                Prefs.putDouble(PrefConstants.LATITUDE, location.getLatitude());
                Prefs.putDouble(PrefConstants.LONGITUDE, location.getLongitude());
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        });
    }

    private void stopLocationUpdates() {
        SmartLocation.with(this).location().stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}