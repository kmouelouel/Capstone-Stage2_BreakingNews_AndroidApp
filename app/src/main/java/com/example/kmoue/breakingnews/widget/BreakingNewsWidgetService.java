package com.example.kmoue.breakingnews.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.kmoue.breakingnews.NewsWidgetProvider;
import com.example.kmoue.breakingnews.data.NewsContract;

public class BreakingNewsWidgetService extends IntentService {
    public static final String ACTION_GET_BREAKING_NEWS = "com.example.kmoue.breakingnews.action.breaking_news";

    public BreakingNewsWidgetService(){
        super("BreakingNewsWidgetService");
    }

    public static void startActionBreakingNews(Context context) {
        Intent intent = new Intent(context, BreakingNewsWidgetService.class);
        intent.setAction(ACTION_GET_BREAKING_NEWS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_BREAKING_NEWS.equals(action)) {
                handleActionUpdateNewsWidgets();
            }
        }

    }

    private void handleActionUpdateNewsWidgets() {
        Uri lastBreakingNewsUri = NewsContract.NewsEntry.buildNewsUriWithID(0);
        Cursor lastBreakingNewsCursor = getContentResolver().query(
                lastBreakingNewsUri,
                null,
                null,
                null,
                null);
        String title="",source="",date="",image="";
        if (lastBreakingNewsCursor.moveToFirst()) {
            int titleCol = lastBreakingNewsCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_TITLE);
            int dateCol = lastBreakingNewsCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_PUBLISHED_DATE);
            int sourceCol = lastBreakingNewsCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_SOURCE_NAME);
            int imageUrlCol=lastBreakingNewsCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE);
            title = lastBreakingNewsCursor.getString(titleCol);
            date = lastBreakingNewsCursor.getString(dateCol);
            source = lastBreakingNewsCursor.getString(sourceCol);
            image = lastBreakingNewsCursor.getString(imageUrlCol);
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, NewsWidgetProvider.class));
        //Now update all widgets
        NewsWidgetProvider.updateBreakingNewsWidgets(this, appWidgetManager, title,source,date,image, appWidgetIds);

    }
}
