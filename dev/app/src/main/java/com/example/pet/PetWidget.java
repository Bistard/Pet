package com.example.pet;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
/**
 * Implementation of App Widget functionality.
 */
public class PetWidget extends AppWidgetProvider {
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.pet_widget);

        Log.i("widget", "updateAppWidget");


        User user = User.Initialize();

        int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
        int currentMonth = Integer.parseInt(new SimpleDateFormat("M", Locale.getDefault()).format(new Date()));
        int currentDate = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));

        ArrayList<Task> currentTasks = user.getUnfinishedTasks(currentYear, currentMonth, currentDate);
        for (Task t : user.getTasks(currentYear, currentMonth, currentDate)) {
            if (!t.isFininshed()) {
                currentTasks.add(t);
            }
        }
        int[] textIds = {R.id.appwidget_todo_text0, R.id.appwidget_todo_text1, R.id.appwidget_todo_text2};
        int[] iconIds = {R.id.appwidget_todo_icon0, R.id.appwidget_todo_icon1, R.id.appwidget_todo_icon2};
        if (currentTasks.size() == 0) {
            views.setTextViewText(textIds[0], "Nothing to do today.");
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
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        RemoteViews remoteViews;
        ComponentName watchWidget;

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.pet_widget);
        watchWidget = new ComponentName(context, PetWidget.class);

        Log.i("widget", "Initialized");
        remoteViews.setOnClickPendingIntent(R.id.appwidget_refresh_button, getPendingSelfIntent(context));
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.pet_widget);
        ComponentName watchWidget = new ComponentName(context, PetWidget.class);

        Log.i("widget", "onReceive");
        User user = User.Initialize();

        int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
        int currentMonth = Integer.parseInt(new SimpleDateFormat("M", Locale.getDefault()).format(new Date()));
        int currentDate = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));

        ArrayList<Task> currentTasks = user.getUnfinishedTasks(currentYear, currentMonth, currentDate);
        for (Task t : user.getTasks(currentYear, currentMonth, currentDate)) {
            if (!t.isFininshed()) {
                currentTasks.add(t);
            }
        }
        int[] textIds = {R.id.appwidget_todo_text0, R.id.appwidget_todo_text1, R.id.appwidget_todo_text2};
        int[] iconIds = {R.id.appwidget_todo_icon0, R.id.appwidget_todo_icon1, R.id.appwidget_todo_icon2};
        int[] IMAGE_SOURCE = new int[]{R.drawable.ic_baseline_education_24, R.drawable.ic_baseline_habbit_24, R.drawable.ic_baseline_sport_24, R.drawable.ic_baseline_work_24};
        if (currentTasks.size() == 0) {
            views.setViewVisibility(iconIds[0], View.INVISIBLE);
            views.setViewVisibility(iconIds[1], View.INVISIBLE);
            views.setViewVisibility(iconIds[2], View.INVISIBLE);
            views.setTextViewText(textIds[0], "Nothing to do here.");
            views.setTextViewText(textIds[1], " ");
            views.setTextViewText(textIds[2], " ");
        } else {
            for (int i = 0; i < 3; i++) {
                try {
                    views.setTextViewText(textIds[i], currentTasks.get(i).name());
                    views.setViewVisibility(iconIds[i], View.VISIBLE);
                    views.setImageViewResource(iconIds[i], IMAGE_SOURCE[currentTasks.get(i).parentType()]);
                } catch (Exception e) {
                    views.setTextViewText(textIds[i], "");
                    views.setViewVisibility(iconIds[i], View.INVISIBLE);
                }
            }
        }

        appWidgetManager.updateAppWidget(watchWidget, views);
    }

    protected PendingIntent getPendingSelfIntent(Context context) {
        Intent intent = new Intent(context, getClass());
        intent.setAction("Refresh");
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}