package com.example.pet;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;

public class MyService extends Service {
    private NotificationManagerCompat notificationManager;
    private Context context;

    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this;
        notificationManager = NotificationManagerCompat.from(context);

        final Handler refreshHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                Notification notification = new NotificationCompat.Builder(context, NotificationApplication.CHANNEL_ID)
                        .setSmallIcon(R.drawable.snail_t)
                        .setContentTitle("Pet")
                        .setContentText("Hallo!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .build();

                notificationManager.notify(1, notification);

                refreshHandler.postDelayed(this, 60 * 1000);
            }
        };
        refreshHandler.postDelayed(runnable, 60 * 1000);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}