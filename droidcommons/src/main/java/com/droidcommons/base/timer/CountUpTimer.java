package com.droidcommons.base.timer;

import android.os.CountDownTimer;
import android.os.Message;

public class CountUpTimer extends CountDownTimer {

    public static final int REVIEW_INFORMATION_TIME_SPENT = 201;
    private static final long INTERVAL_MS = 1000;
    private final long duration = Long.MAX_VALUE;
    private SingleLiveTimerEvent<Message> liveData;

    public CountUpTimer(SingleLiveTimerEvent<Message> liveData) {
        super(Long.MAX_VALUE, INTERVAL_MS);
        this.liveData = liveData;
    }


    @Override
    public void onTick(long msUntilFinished) {
        int sec = (int) ((duration - msUntilFinished) / INTERVAL_MS);
        Message message = new Message();
        message.arg1 = sec;
        message.what = REVIEW_INFORMATION_TIME_SPENT;
        liveData.postValue(message);
    }

    @Override
    public void onFinish() {

    }
}