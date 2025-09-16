package com.droidcommons.permissions;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

public class RunTimePermissions {

    //    public static final int RC_SMS_PERMISSION = 2001;
//    public static final int RC_CAMERA_PERMISSION = 2002;
    //    public static final int RC_PHONE_STATE_PERMISSION = 2003;
    public static final int RC_LOCATION_PERMISSION = 2004;
    public static final int RC_AUDIO_RECORD = 2005;
    public static final int RC_APP_PERMISSION = 2006;
    public static final int BG_LOCATION_PERMISSION = 2007;
    public static final int POST_NOTIFICATION_PERMISSION = 2008;

   /* public static boolean hasAppPermissions(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static String[] getAllAppPermissions() {
        return new String[]{
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
    }*/

    public static boolean hasAppPermissions(Context context) {
        return
//                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        hasFGSLocationPermission(context); // include check for FGS permission on Android 14+
    }

    public static String[] getAllAppPermissions() {
        if (Build.VERSION.SDK_INT >= 34) {
            return new String[]{
//                    Manifest.permission.READ_SMS,
//                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    "android.permission.FOREGROUND_SERVICE_LOCATION"
            };
        } else {
            return new String[]{
//                    Manifest.permission.READ_SMS,
//                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static String[] getBGLocationPermissions() {
        return new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION};
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] getPostNotificationPermissions() {
        return new String[]{Manifest.permission.POST_NOTIFICATIONS};
    }

    public static boolean hasBGLocationGranted(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static boolean hasPostNotificationGranted(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static boolean checkAudioRecordPermissions(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    public static String[] getAudioRecordPermissions() {
        return new String[]{Manifest.permission.RECORD_AUDIO};
    }

    public static boolean checkLocationPermissions(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static String[] getLocationPermissions() {
        return new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    }

    public static boolean hasFGSLocationPermission(Context context) {
        if (Build.VERSION.SDK_INT >= 34) {
            String permission = "android.permission.FOREGROUND_SERVICE_LOCATION";
            return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }
}
