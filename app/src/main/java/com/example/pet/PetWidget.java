package com.example.pet;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 */
public class PetWidget extends AppWidgetProvider {

    private static final String SYNC_CLICKED    = "automaticWidgetSyncButtonClick";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.pet_widget);

        User user = User.Initialize();

        int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
        int currentMonth = Integer.parseInt(new SimpleDateFormat("M", Locale.getDefault()).format(new Date()));
        int currentDate = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));

        ArrayList<Task> currentTasks = user.getUnfinishedTasks(currentYear, currentMonth, currentDate);
        for(Task t:user.getTasks(currentYear, currentMonth, currentDate)){
            if(!t.isFininshed()){
                currentTasks.add(t);
            }
        }
        int[] textIds = {R.id.appwidget_todo_text0, R.id.appwidget_todo_text1, R.id.appwidget_todo_text2};
        if (currentTasks.size() == 0) {
            views.setTextViewText(textIds[0], "Nothing to do here.");
            views.setTextViewText(textIds[1], " ");
            views.setTextViewText(textIds[2], " ");
        } else {
            for (int i = 0; i < 3; i++) {
                try {
                    views.setTextViewText(textIds[i], currentTasks.get(i).name());
                } catch (Exception e) {
                    views.setTextViewText(textIds[i], "");
                }
            }
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews;
        ComponentName watchWidget;

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.pet_widget);
        watchWidget = new ComponentName(context, PetWidget.class);

        remoteViews.setOnClickPendingIntent(R.id.appwidget_refresh_button, getPendingSelfIntent(context, SYNC_CLICKED));
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (SYNC_CLICKED.equals(intent.getAction())) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.pet_widget);
            ComponentName watchWidget = new ComponentName(context, PetWidget.class);

//            remoteViews.setTextViewText(R.id.appwidget_refresh_button, "TESTING");

            User user = User.Initialize();

            int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
            int currentMonth = Integer.parseInt(new SimpleDateFormat("M", Locale.getDefault()).format(new Date()));
            int currentDate = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));

            ArrayList<Task> currentTasks = user.getUnfinishedTasks(currentYear, currentMonth, currentDate);
            for(Task t:user.getTasks(currentYear, currentMonth, currentDate)){
                if(!t.isFininshed()){
                    currentTasks.add(t);
                }
            }
            int[] textIds = {R.id.appwidget_todo_text0, R.id.appwidget_todo_text1, R.id.appwidget_todo_text2};
            if (currentTasks.size() == 0) {
                views.setTextViewText(textIds[0], "Nothing to do here.");
                views.setTextViewText(textIds[1], " ");
                views.setTextViewText(textIds[2], " ");
            } else {
                for (int i = 0; i < 3; i++) {
                    try {
                        views.setTextViewText(textIds[i], currentTasks.get(i).name());
                    } catch (Exception e) {
                        views.setTextViewText(textIds[i], "");
                    }
                }
            }

            appWidgetManager.updateAppWidget(watchWidget, views);

        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}