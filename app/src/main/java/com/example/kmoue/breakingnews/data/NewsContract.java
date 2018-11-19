package com.example.kmoue.breakingnews.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class NewsContract {
    // the authority, which is how your code knows which content provider to access.
    public static final String CONTENT_AUTHORITY = "com.example.kmoue.breakingnews";
    // the base content URI ="content://+ <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
   //define the possible paths for accessing data in this contract
    //this is the path for the "tasks" directory
    public static final String PATH_NEWS = "news";

    public static final class NewsEntry implements BaseColumns {
        /* The base CONTENT_URI used to query the News table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_NEWS)
                .build();
        public static final String TABLE_NAME = "news";
        public static final String COLUMN_NEWS_ID = "news_id";
        public static final String COLUMN_SOURCE_ID = "source_id";
        public static final String COLUMN_SOURCE_NAME = "source_name";
        public static final String COLUMN_AUTHOR =  "author";
        public static final String COLUMN_TITLE =  "title";
        public static final String COLUMN_DESCRIPTION =  "description";
        public static final String COLUMN_URL =  "url";
        public static final String COLUMN_URL_TO_IMAGE= "urlToImage";
        public static final String COLUMN_PUBLISHED_DATE = "publishedAt";
        public static final String COLUMN_CONTENT="content";

        public static Uri buildNewsUriWithID(int id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }
    }
}
