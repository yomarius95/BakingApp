package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import static com.example.android.bakingapp.NavActivity.RECIPE_NAME;

public class IngredientsWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String recipeName = sharedPref.getString(RECIPE_NAME, null);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        views.setTextViewText(R.id.appwidget_title, recipeName);

        Intent intent = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.appwidget_ingredients_list, intent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals("android.appwidget.action.APPWIDGET_UPDATE")){
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName appWidget = new ComponentName(context, IngredientsWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(appWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_ingredients_list);
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }
}

