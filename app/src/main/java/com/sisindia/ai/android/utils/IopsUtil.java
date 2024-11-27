package com.sisindia.ai.android.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.BatteryManager;

import java.util.Objects;

public class IopsUtil {

    /*public static Boolean checkPreDashBoardAvailabilty() {
        String preDashboardLastLaunchDate = Prefs.getString(PRE_DASH_BOARD_LAST_LAUNCH_DATE);
        Boolean isSameDay = TimeUtils.compareDateWithCurrentDate(preDashboardLastLaunchDate);
        return isSameDay;
    }*/

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            }
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                    return true;
                else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                    return true;
                else return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
            }
        }
        return false;
    }

  /*  public static boolean isLocationOn(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false, network_enabled = false;

        try {
            if (locationManager != null) {
                gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }
        } catch (Exception e) {
            Timber.e(e);
        }

        try {
            if (locationManager != null) {
                network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }
        } catch (Exception e) {
            Timber.e(e);
        }

        return !gps_enabled && !network_enabled;
    }*/


    public static int getBatteryLevel(Context context) {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent status = context.registerReceiver(null, intentFilter);
        return Objects.requireNonNull(status).getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
    }

}
