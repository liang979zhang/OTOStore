package com.example.administrator.otostore.Utils;

import android.os.CountDownTimer;

/**
 * Created by Administrator on 2018/5/25.
 */

public class TimeCount extends CountDownTimer {

    private CountListener countListener;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public TimeCount(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        countListener.onTick(millisUntilFinished);

    }

    @Override
    public void onFinish() {
        countListener.onFinish();
    }

    public void setCountListener(CountListener countListener) {
        this.countListener = countListener;

    }

    public interface CountListener {
        public void onTick(Long time);

        public void onFinish();
    }

}
