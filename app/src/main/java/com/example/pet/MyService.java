package com.example.pet;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

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
                User user = User.Initialize();
                String str = null;
                try {
                    str = user.pet.getPhrase();
                } catch (Exception e) {
                    Log.i("home", e.toString());
                }
                if (str != null) {
                    RemoteViews view = new RemoteViews(getPackageName(), R.layout.notification);
                    view.setTextViewText(R.id.notification_text, str);

                    Notification notification = new NotificationCompat.Builder(context, NotificationApplication.CHANNEL_ID)
                            .setSmallIcon(R.drawable.snail_t)
                            .setContentTitle(user.getPetName() + ": ")
                            .setContentText(str)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCustomHeadsUpContentView(view)
                            .setAutoCancel(true)
                            .build();

                    notificationManager.notify(0, notification);
                }
                refreshHandler.postDelayed(this, 5 * 60 * 1000);
            }
        };
        refreshHandler.postDelayed(runnable, 5 * 60 * 1000);

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