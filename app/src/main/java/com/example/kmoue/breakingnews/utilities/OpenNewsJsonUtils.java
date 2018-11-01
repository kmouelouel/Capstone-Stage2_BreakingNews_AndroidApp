package com.example.kmoue.breakingnews.utilities;

import android.content.Context;
import android.util.Log;

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
    private static List<NewsObject> newsHeadlines;

    public static String[] getNewsFromJson(Context context, String newsJsonStr){
         try{
            JSONObject newsHeadlinesJsonObject = new JSONObject(newsJsonStr);
            if(newsHeadlinesJsonObject.has("message")){
                String statusMessage=newsHeadlinesJsonObject.getString("message");
                Log.e(TAG, "parse json movie has an error message: " + statusMessage);
                return null;
            }
            else{
                JSONArray newsArticlesArray= newsHeadlinesJsonObject.getJSONArray(KEY_ARTICLES);
                String[]  mNewsHeadlines= new String[newsArticlesArray.length()];
                for(int i=0; i< newsArticlesArray.length(); i++){
                    JSONObject mNewsObject = newsArticlesArray.getJSONObject(i) ;
                    NewsObject newResult=getNewElement(mNewsObject);
                    String title=newResult.getTitle();
                    mNewsHeadlines[i]=title;
                }
                return mNewsHeadlines;
            }

        }catch(JSONException e){
            e.printStackTrace();
            return null;

        }
    }
    public static NewsObject getNewElement(JSONObject inNewsObject) {
        NewsObject currentNews = new NewsObject();
        try {
            JSONObject source=inNewsObject.getJSONObject("source");
            currentNews.setSourceId(source.getString(KEY_SOURCE_ID));
            currentNews.setSourceName(source.getString(KEY_SOURCE_NAME));
            currentNews.setAuthor(inNewsObject.getString(KEY_AUTHOR));
            currentNews.setContent(inNewsObject.getString(KEY_CONTENT));
            currentNews.setDescription(inNewsObject.getString(KEY_DESCRIPTION));
            currentNews.setPublishedAt(inNewsObject.getString(KEY_PUBLISHEDAT));
            currentNews.setTitle(inNewsObject.getString(KEY_TITLE));
            currentNews.setUrl(inNewsObject.getString(KEY_URL));
            currentNews.setUrlToImage(inNewsObject.getString(KEY_URL_TO_IMAGE));
            return currentNews;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

}
