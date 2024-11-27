package com.sisindia.ai.android.receivers;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import dagger.android.DaggerBroadcastReceiver;

public class OtpSmsReceiver extends DaggerBroadcastReceiver {

    public static final String BIND_OTP_TO_VIEW = "BIND_OTP_TO_VIEW";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent != null && intent.getExtras() != null) {
            Object[] pdusArray = (Object[]) intent.getExtras().get("pdus");
            if (pdusArray != null && pdusArray.length != 0) {
                SmsMessage otpMessage;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    otpMessage = SmsMessage.createFromPdu((byte[]) pdusArray[0], intent.getExtras().getString("format"));
                } else {
                    otpMessage = SmsMessage.createFromPdu((byte[]) pdusArray[0]);
                }
                String[] spliStr = otpMessage.getDisplayMessageBody().split("\\s");
                for (String piece : spliStr) {
                    if (TextUtils.isDigitsOnly(piece)) {
                        Intent otpIntent = new Intent(BIND_OTP_TO_VIEW);
                        otpIntent.putExtra(BIND_OTP_TO_VIEW, piece);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(otpIntent);
                        break;
                    }
                }
            }
        }
    }
}
