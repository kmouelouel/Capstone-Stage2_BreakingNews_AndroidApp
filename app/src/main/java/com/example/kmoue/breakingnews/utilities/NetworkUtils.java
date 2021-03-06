package com.example.kmoue.breakingnews.utilities;

import android.net.Uri;

import com.example.kmoue.breakingnews.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
   // private static final String STATIC_NEWS_URL ="https://newsapi.org/v2/top-headlines?country=us";
   private static final String API_KEY = BuildConfig.API_KEY;
    private final static String API_KEY_PARAM = "apiKey";
    private final static String COUNTRY_PARAM = "country";
    private final static String CATEGORY_PARAM = "category";
    private final static String COUNTRY_VALUE = "us";

    public static URL buildUrl(String category) {
        Uri.Builder builtUri = new Uri.Builder();
        builtUri.scheme("https")
                    .authority("newsapi.org")
                    .appendPath("v2")
                    .appendPath("top-headlines")
                    .appendQueryParameter(COUNTRY_PARAM, COUNTRY_VALUE)
                    .appendQueryParameter(API_KEY_PARAM, API_KEY);
         if(!category.isEmpty())       builtUri.appendQueryParameter(CATEGORY_PARAM, category);

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url){
        HttpURLConnection urlConnection= null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }
        return null;
    }
}
