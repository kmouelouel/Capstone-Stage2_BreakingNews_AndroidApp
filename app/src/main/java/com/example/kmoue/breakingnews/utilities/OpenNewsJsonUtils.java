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

    public static List<NewsObject> getNewsFromJson(Context context, String newsJsonStr){
        newsHeadlines = new ArrayList<>();
        try{
            JSONObject newsHeadlinesJsonObject = new JSONObject(newsJsonStr);
            if(newsHeadlinesJsonObject.has("message")){
                String statusMessage=newsHeadlinesJsonObject.getString("message");
                Log.e(TAG, "parse json movie has an error message: " + statusMessage);
                return null;
            }
            else{
                JSONArray newsArticlesArray= newsHeadlinesJsonObject.getJSONArray(KEY_ARTICLES);
                NewsObject newResult;
                for(int i=0; i< newsArticlesArray.length(); i++){
                    JSONObject mNewsObject = newsArticlesArray.getJSONObject(i) ;
                    newResult=getNewElement(mNewsObject);
                    newsHeadlines.add(newResult);
                }
                return newsHeadlines;
            }

        }catch(JSONException e){
            e.printStackTrace();
            return null;

        }
    }
    public static NewsObject getNewElement(JSONObject inNewsObject) {
        NewsObject currentNews = new NewsObject();
        try {

            currentNews.setSourceId(inNewsObject.getString(KEY_SOURCE_ID));
            currentNews.setSourceName(inNewsObject.getString(KEY_SOURCE_NAME));
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
