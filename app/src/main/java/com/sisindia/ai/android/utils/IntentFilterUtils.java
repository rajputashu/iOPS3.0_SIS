package com.sisindia.ai.android.utils;

import android.content.IntentFilter;

public class IntentFilterUtils {

    public static IntentFilter getReadSmsIntentFilter() {
        return new IntentFilter("BIND_OTP_TO_VIEW");
    }
}
