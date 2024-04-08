package com.example.amc_app.Utills;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class MyServices extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this,MyServices.class));
        Timer t = new Timer();
        final Handler handler = new Handler();


        // Timer task makes your service will repeat after every 20 Sec.
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        //Do network call here
                    }

                });
            }
        };
        //Starts after 20 sec and will repeat on every 20 sec of time interval.
        t.schedule(doAsynchronousTask, 3000,3000);  // 20 sec timer
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }
}
