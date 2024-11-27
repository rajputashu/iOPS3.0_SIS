package com.sisindia.ai.android.di.modules;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Message;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;

import androidx.databinding.ObservableField;

import com.droidcommons.base.SingleLiveEvent;
import com.droidcommons.base.timer.CountUpTimer;
import com.droidcommons.base.timer.SingleLiveTimerEvent;
import com.droidcommons.preference.Prefs;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.di.components.ViewModelSubComponent;
import com.sisindia.ai.android.firebase.NotificationHandler;
import com.sisindia.ai.android.models.DeviceInfo;
import com.sisindia.ai.android.models.SisCountry;

import java.util.ArrayList;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(subcomponents = {ViewModelSubComponent.class})
public class BaseModule {

    @Provides
    @Singleton
    SingleLiveEvent<Message> provideSingleLiveEvent() {
        return new SingleLiveEvent<>();
    }

    @Provides
    @Singleton
    SingleLiveTimerEvent<Message> provideSingleLiveTimerEvent() {
        return new SingleLiveTimerEvent<>();
    }

    @Provides
    @Singleton
    FirebaseAnalytics provideFirebaseAnalytics(Context context) {
        return FirebaseAnalytics.getInstance(context);
    }

    @Provides
    @Singleton
    CountUpTimer provideCountDownTimer(SingleLiveTimerEvent<Message> liveData) {
        return new CountUpTimer(liveData);
    }


    @Provides
    @Singleton
    @SuppressLint("MissingPermission")
    DeviceInfo providesDeviceInfo(Context context) {
        DeviceInfo deviceInfo = new DeviceInfo();
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        if (subscriptionManager != null && subscriptionManager.getActiveSubscriptionInfoList() != null) {
            for (int i = 0; i < subscriptionManager.getActiveSubscriptionInfoList().size(); i++) {
                SubscriptionInfo info = subscriptionManager.getActiveSubscriptionInfoList().get(i);
                if (i == 0) {
                    deviceInfo.imei1 = info.getIccId();
                    deviceInfo.sim1PhoneNo = info.getNumber();
                } else {
                    deviceInfo.imei2 = info.getIccId();
                    deviceInfo.sim2PhoneNo = info.getNumber();
                }
            }
        }
        try {
            deviceInfo.appVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        deviceInfo.phoneNumber = Prefs.getString(PrefConstants.COUNTRY_CODE).concat("-").concat(Prefs.getString(PrefConstants.USER_MOBILE_NUMBER));
        deviceInfo.deviceId = androidId;
        deviceInfo.appToken = Prefs.getString(PrefConstants.FCM_TOKEN);
        return deviceInfo;
    }

    @Provides
    @Singleton
    ObservableField<ArrayList<SisCountry>> provideCountries() {
        ObservableField<ArrayList<SisCountry>> list = new ObservableField<>(new ArrayList<>());
        ArrayList<SisCountry> countries = new ArrayList<>();

        countries.add(
                new SisCountry(1, "India", "+91", 10, 10, 0, "")
        );
        countries.add(
                new SisCountry(2, "Australia", "+61", 12, 8, 0, "")
        );
        countries.add(
                new SisCountry(3, "New Zealand", "+64", 1, 1, 0, "")
        );
        countries.add(
                new SisCountry(4, "Singapore", "+65", 1, 1, 0, "")
        );
        list.set(countries);
        return list;
    }

    @Provides
    @Singleton
    @Named("PerformanceSortItems")
    ArrayList<String> getPerformanceSortItems() {
        ArrayList<String> sortItems = new ArrayList<>();
        sortItems.add("Today");
        sortItems.add("Yesterday");
        sortItems.add("This Week");
        sortItems.add("Last Week");
        sortItems.add("This Month");
        sortItems.add("Last Month");
        return sortItems;
    }
}
