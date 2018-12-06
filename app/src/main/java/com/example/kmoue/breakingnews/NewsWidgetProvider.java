package com.example.kmoue.breakingnews;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.kmoue.breakingnews.widget.BreakingNewsWidgetService;
import com.squareup.picasso.Picasso;

/**
 * Implementation of App Widget functionality.
 */
public class NewsWidgetProvider extends AppWidgetProvider {
    static void updateAppWidget(Context context,  AppWidgetManager appWidgetManager,String title,String source,String date,String image, int appWidgetId)
    {
        // Create an Intent to launch MainActivity when clicked
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Construct the RemoteViews object
        RemoteViews  views = new RemoteViews(context.getPackageName(), R.layout.news_widget_provider);
            // Update image
            views.setTextViewText(R.id.widget_title, title);
            views.setTextViewText(R.id.widget_source, source);
            views.setTextViewText(R.id.widget_date, date);
            views.setOnClickPendingIntent(R.id.widget_news_image, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        //Start the intent service update widget action,
        // //the service takes care of updating the widgets UI

        BreakingNewsWidgetService.startActionBreakingNews(context);
    }


    public static void updateBreakingNewsWidgets(Context context, AppWidgetManager appWidgetManager,
                                                 String title,String source,String date,String image, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager,title,source,date,image, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


}

