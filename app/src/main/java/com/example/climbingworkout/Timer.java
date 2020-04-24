package com.example.climbingworkout;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.TextView;

public class Timer extends Service {





    @Override
    public IBinder onBind(Intent i) {
        return null;
    }
    @Override
    public int onStartCommand(Intent i, int flags, int startId) {

        new CountDownTimer(180 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                double minutes = secondsRemaining / 60;
                double seconds = secondsRemaining % 60;
            }

            public void onFinish() {
            }


        }.start();


        return flags;
    }

}
