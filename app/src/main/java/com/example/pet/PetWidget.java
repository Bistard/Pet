package com.example.pet;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
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
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.pet_widget);

        User user = User.Initialize();

        int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
        int currentMonth = Integer.parseInt(new SimpleDateFormat("M", Locale.getDefault()).format(new Date()));
        int currentDate = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));

        ArrayList<Task> currentTasks = user.getUnfinishedTasks(currentYear, currentMonth, currentDate);
        currentTasks.addAll(user.getTasks(currentYear, currentMonth, currentDate));

        int[] ids = {R.id.appwidget_todo_text0, R.id.appwidget_todo_text1, R.id.appwidget_todo_text2};
        if (currentTasks.size() == 0) {
            views.setTextViewText(ids[0], "You have finished everything!");
            views.setTextViewText(ids[1], " ");
            views.setTextViewText(ids[2], " ");
        } else {
            for (int i = 0; i < 3; i++) {
                try {
                    views.setTextViewText(ids[i], currentTasks.get(i).name());
                } catch (Exception e) {
                    views.setTextViewText(ids[i], "");
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
    }
}