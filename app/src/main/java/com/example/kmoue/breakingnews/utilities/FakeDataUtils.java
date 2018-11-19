package com.example.kmoue.breakingnews.utilities;

import android.content.ContentValues;
import android.content.Context;

import com.example.kmoue.breakingnews.data.NewsContract;

import java.util.ArrayList;
import java.util.List;

public class FakeDataUtils {
    public static void insertFakeData(Context context) {
        List<ContentValues> fakeValues = new ArrayList<ContentValues>();
        for(int i=0; i<7; i++) {
            fakeValues.add(createTestNewsContentValues(i));
        }
        context.getContentResolver().bulkInsert(
                NewsContract.NewsEntry.CONTENT_URI,
                fakeValues.toArray(new ContentValues[7]));
    }
    private static ContentValues createTestNewsContentValues(int index) {
        ContentValues testNewsValues = new ContentValues();

     testNewsValues.put(NewsContract.NewsEntry.COLUMN_NEWS_ID,index);
        testNewsValues.put(NewsContract.NewsEntry.COLUMN_SOURCE_ID, "source id "+index);
        testNewsValues.put(NewsContract.NewsEntry.COLUMN_SOURCE_NAME, "source name "+index);
        testNewsValues.put(NewsContract.NewsEntry.COLUMN_AUTHOR, "author "+index);
        testNewsValues.put(NewsContract.NewsEntry.COLUMN_TITLE, "title "+index);
        testNewsValues.put(NewsContract.NewsEntry.COLUMN_DESCRIPTION, "description "+index);
        testNewsValues.put(NewsContract.NewsEntry.COLUMN_URL, "url "+index);
        testNewsValues.put(NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE, "urlToImage "+index);
        testNewsValues.put(NewsContract.NewsEntry.COLUMN_PUBLISHED_DATE, "date "+index);
        testNewsValues.put(NewsContract.NewsEntry.COLUMN_CONTENT, "content "+index);
        return testNewsValues;
    }
}
