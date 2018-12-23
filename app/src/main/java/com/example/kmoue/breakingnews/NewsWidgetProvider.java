package com.example.kmoue.breakingnews;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.example.kmoue.breakingnews.data.NewsContract;
import com.example.kmoue.breakingnews.sync.BreakingNewsSyncIntentService;
import com.example.kmoue.breakingnews.widget.BreakingNewsWidgetService;
import com.squareup.picasso.Picasso;

/**
 * Implementation of App Widget functionality.
 */
public class NewsWidgetProvider extends AppWidgetProvider {
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context,  AppWidgetManager appWidgetManager,String title,String source,String date,String image, int appWidgetId)
    {
        // Get current width to decide on single plant vs garden grid view
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        int height = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        RemoteViews views;
        if(width<=60 && height <=58){
            views = getSmallIconRemoteView(context);
        }else {
            views = getBigGridRemoteView(context, title, source, date);
        }

       // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public  static  RemoteViews getSmallIconRemoteView(Context context){
        Intent intentToStartMainActivity = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentToStartMainActivity, PendingIntent.FLAG_UPDATE_CURRENT);

        // Construct the RemoteViews object
        RemoteViews  views = new RemoteViews(context.getPackageName(), R.layout.news_widget_icon_provider);
        // Update image
        views.setImageViewResource(R.id.widget_news_image,R.drawable.news_icon);
        views.setOnClickPendingIntent(R.id.widget_news_image, pendingIntent);

        return views;
    }
    public  static  RemoteViews getBigGridRemoteView(Context context,String title,String source,String date){
        // Create an Intent to launch MainActivity when clicked
        Intent intentToStartDetailActivity = new Intent(context, DetailActivity.class);
        Uri uriForNewsClicked = NewsContract.NewsEntry.buildNewsUriWithID(0);
        intentToStartDetailActivity.setData(uriForNewsClicked);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentToStartDetailActivity, PendingIntent.FLAG_UPDATE_CURRENT);

        // Construct the RemoteViews object
        RemoteViews  views = new RemoteViews(context.getPackageName(), R.layout.news_widget_provider);
        // Update image
         views.setImageViewResource(R.id.widget_news_image,R.drawable.news_icon);
        views.setOnClickPendingIntent(R.id.widget_news_image, pendingIntent);
        views.setTextViewText(R.id.widget_title, title);
        views.setTextViewText(R.id.widget_source, source);
        views.setTextViewText(R.id.widget_date, date);
        Intent breakingNewsIntent = new Intent(context,BreakingNewsSyncIntentService.class);
        breakingNewsIntent.setAction(BreakingNewsWidgetService.ACTION_GET_BREAKING_NEWS);
        PendingIntent newsPendingIntent = PendingIntent.getService(context, 0, breakingNewsIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_title, newsPendingIntent);

        return views;
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        BreakingNewsWidgetService.startActionBreakingNews(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
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

