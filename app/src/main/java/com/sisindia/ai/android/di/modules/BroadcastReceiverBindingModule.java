package com.sisindia.ai.android.di.modules;

import com.droidcommons.dagger.qualifier.ReceiverScope;
import com.sisindia.ai.android.receivers.ActivityTransitionReceiver;
import com.sisindia.ai.android.receivers.MySISReceiver;
import com.sisindia.ai.android.receivers.OtpSmsReceiver;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BroadcastReceiverBindingModule {

    @ReceiverScope
    @ContributesAndroidInjector
    abstract OtpSmsReceiver bindOtpSmsReceiver();

    @ReceiverScope
    @ContributesAndroidInjector
    abstract ActivityTransitionReceiver bindTransitionReceiver();

    @ReceiverScope
    @ContributesAndroidInjector
    abstract MySISReceiver bindMySISReceiver();
}
