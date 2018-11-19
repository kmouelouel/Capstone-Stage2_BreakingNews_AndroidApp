package com.example.kmoue.breakingnews.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.kmoue.breakingnews.data.NewsCategoryPreferences;
import com.example.kmoue.breakingnews.data.NewsContract;
import com.example.kmoue.breakingnews.utilities.NetworkUtils;
import com.example.kmoue.breakingnews.utilities.OpenNewsJsonUtils;

import java.net.URL;

public class BreakingNewsSyncTask {

    synchronized public static void syncNews(Context context){
        try{
            NewsCategoryPreferences newsCategory= new NewsCategoryPreferences(context);
            String category= newsCategory.getCategory();
            URL newsRequestUrl = NetworkUtils.buildUrl(category);
            String JsonNewsResponse = NetworkUtils.getResponseFromHttpUrl(newsRequestUrl);
            ContentValues[] newsValues = OpenNewsJsonUtils.getNewsContentValueFromJson(context,JsonNewsResponse);
            if(newsValues != null && newsValues.length != 0){
                ContentResolver newsContentResolver = context.getContentResolver();
                newsContentResolver.delete(NewsContract.NewsEntry.CONTENT_URI,
                        null,
                        null);
                newsContentResolver.bulkInsert(
                        NewsContract.NewsEntry.CONTENT_URI,
                        newsValues);
                }

        }catch(Exception e){
            e.printStackTrace();

        }

    }
}
