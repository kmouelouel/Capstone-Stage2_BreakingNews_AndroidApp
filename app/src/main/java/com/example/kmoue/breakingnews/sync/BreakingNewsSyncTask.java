package com.example.kmoue.breakingnews.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.text.format.DateUtils;
import android.widget.Toast;

import com.example.kmoue.breakingnews.data.NewsCategoryPreferences;
import com.example.kmoue.breakingnews.data.NewsContract;
import com.example.kmoue.breakingnews.utilities.NetworkUtils;
import com.example.kmoue.breakingnews.utilities.NotificationUtils;
import com.example.kmoue.breakingnews.utilities.OpenNewsJsonUtils;
import com.example.kmoue.breakingnews.widget.BreakingNewsWidgetService;

import java.net.URL;

import static com.example.kmoue.breakingnews.data.NewsCategoryPreferences.areNotificationsEnabled;
import static com.example.kmoue.breakingnews.data.NewsCategoryPreferences.getEllapsedTimeSinceLastNotification;
import static com.example.kmoue.breakingnews.data.NewsCategoryPreferences.returnCategory;
import static com.example.kmoue.breakingnews.data.NewsCategoryPreferences.saveLastNotificationTime;

public class BreakingNewsSyncTask {

    synchronized public static void syncNews(Context context){
        try{

            String category= returnCategory(context);
            URL newsRequestUrl = NetworkUtils.buildUrl(category);
            String JsonNewsResponse = NetworkUtils.getResponseFromHttpUrl(newsRequestUrl);
            ContentValues[] newsValues = OpenNewsJsonUtils.getNewsContentValueFromJson(context,JsonNewsResponse);
            if(newsValues != null && newsValues.length != 0){

                ContentResolver newsContentResolver = context.getContentResolver();
                newsContentResolver.delete(
                        NewsContract.NewsEntry.CONTENT_URI,
                        null,
                        null);
                newsContentResolver.bulkInsert(
                        NewsContract.NewsEntry.CONTENT_URI,
                        newsValues);
                }
            BreakingNewsWidgetService.startActionBreakingNews(context);

            NotificationUtils.notifyUserOfNewBreakingNews(context);

        }catch(Exception e){
            e.printStackTrace();

        }

    }
}
