package com.sisindia.ai.android.base;

import android.content.Intent;
import android.os.Message;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.droidcommons.base.BaseActivity;
import com.droidcommons.base.SingleLiveEvent;
import com.droidcommons.base.timer.SingleLiveTimerEvent;
import com.droidcommons.preference.Prefs;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.sisindia.ai.android.constants.IntentConstants;
import com.sisindia.ai.android.features.onboard.IntroActivity;

import javax.inject.Inject;

public abstract class IopsBaseActivity extends BaseActivity {

    @Inject
    protected ViewModelProvider.Factory viewModelFactory;

    @Inject
    protected SingleLiveEvent<Message> liveData;


    @Inject
    protected FirebaseAnalytics analytics;

    @Inject
    protected SingleLiveTimerEvent<Message> liveTimerEvent;

    protected AndroidViewModel getAndroidViewModel(Class type) {
        return (AndroidViewModel) new ViewModelProvider(this, viewModelFactory).get(type);
    }

    protected void openIntroScreens(String moduleNameKey) {
//        Timber.e("Intro Key Name : %s", moduleNameKey);
        if (!Prefs.getBoolean(moduleNameKey, false))
            startActivity(new Intent(this, IntroActivity.class)
                    .putExtra(IntentConstants.MODULE_NAME, moduleNameKey));
    }
}
