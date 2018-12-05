package com.example.kmoue.breakingnews.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.kmoue.breakingnews.data.NewsContract;
import com.example.kmoue.breakingnews.models.NewsObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OpenNewsJsonUtils {
    private static final String TAG = OpenNewsJsonUtils.class.getSimpleName();
    private static final String KEY_ARTICLES="articles";
    private static final String KEY_SOURCE_ID="id";
    private static final String KEY_SOURCE_NAME="name";
    private static final String KEY_AUTHOR="author";
    private static final String KEY_TITLE="title";
    private static final String KEY_DESCRIPTION="description";
    private static final String KEY_URL="url";
    private static final String KEY_URL_TO_IMAGE="urlToImage";
    private static final String KEY_PUBLISHEDAT="publishedAt";
    private static final String KEY_CONTENT="content";

    public static ContentValues[] getNewsContentValueFromJson(Context context, String newsJsonStr){
         try{
            JSONObject newsHeadlinesJsonObject = new JSONObject(newsJsonStr);
            if(newsHeadlinesJsonObject.has("message")){
                String statusMessage=newsHeadlinesJsonObject.getString("message");
                Log.e(TAG, "parse json movie has an error message: " + statusMessage);
                return null;
            }
            else{
                JSONArray newsArticlesArray= newsHeadlinesJsonObject.getJSONArray(KEY_ARTICLES);
               // String[]  mNewsHeadlines= new String[newsArticlesArray.length()];
                ContentValues[] newsContentValues = new ContentValues[newsArticlesArray.length()];

                for(int i=0; i< newsArticlesArray.length(); i++){
                    JSONObject mNewsObject = newsArticlesArray.getJSONObject(i) ;
                    ContentValues newsValues = new ContentValues();
                    JSONObject source=mNewsObject.getJSONObject("source");
                    newsValues.put(NewsContract.NewsEntry.COLUMN_SOURCE_ID, source.getString(KEY_SOURCE_ID));
                    newsValues.put(NewsContract.NewsEntry.COLUMN_NEWS_ID, Integer.toString(i));
                    newsValues.put(NewsContract.NewsEntry.COLUMN_SOURCE_NAME, source.getString(KEY_SOURCE_NAME));
                    newsValues.put(NewsContract.NewsEntry.COLUMN_AUTHOR, mNewsObject.getString(KEY_AUTHOR));
                    newsValues.put(NewsContract.NewsEntry.COLUMN_TITLE, mNewsObject.getString(KEY_TITLE));
                    newsValues.put(NewsContract.NewsEntry.COLUMN_DESCRIPTION, mNewsObject.getString(KEY_DESCRIPTION));
                    newsValues.put(NewsContract.NewsEntry.COLUMN_URL, mNewsObject.getString(KEY_URL));
                    newsValues.put(NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE, mNewsObject.getString(KEY_URL_TO_IMAGE));
                    String mPublishedDate=mNewsObject.getString(KEY_PUBLISHEDAT);
                    mPublishedDate = mPublishedDate.replaceAll("[A-Z]?", " ");
                    newsValues.put(NewsContract.NewsEntry.COLUMN_PUBLISHED_DATE, mPublishedDate);
                    newsValues.put(NewsContract.NewsEntry.COLUMN_CONTENT, mNewsObject.getString(KEY_CONTENT));
                    newsContentValues[i] = newsValues;
                }
                return newsContentValues;
            }

        }catch(JSONException e){
            e.printStackTrace();
            return null;

        }
    }



}
