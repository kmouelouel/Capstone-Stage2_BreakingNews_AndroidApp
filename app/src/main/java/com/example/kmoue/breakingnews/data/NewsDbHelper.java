package com.example.kmoue.breakingnews.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NewsDbHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "news.db";
    private static final int DATABASE_VERSION = 1;
    public NewsDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_NEWS_TABLE =
                "CREATE TABLE " + NewsContract.NewsEntry.TABLE_NAME + " (" +
                        NewsContract.NewsEntry._ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        NewsContract.NewsEntry.COLUMN_SOURCE_ID     + " TEXT NOT NULL, "     +
                        NewsContract.NewsEntry.COLUMN_SOURCE_NAME     + " TEXT NOT NULL, "   +
                        NewsContract.NewsEntry.COLUMN_AUTHOR     + " TEXT NOT NULL, "        +
                        NewsContract.NewsEntry.COLUMN_TITLE   + " TEXT NOT NULL, "           +
                        NewsContract.NewsEntry.COLUMN_DESCRIPTION   + " TEXT NOT NULL, "     +
                        NewsContract.NewsEntry.COLUMN_URL  + " TEXT NOT NULL, "              +
                        NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE  + " TEXT NOT NULL, "     +
                        NewsContract.NewsEntry.COLUMN_PUBLISHED_DATE + " TEXT NOT NULL,"    +
                        NewsContract.NewsEntry.COLUMN_CONTENT+ " TEXT NOT NULL"              +  ");";
        sqLiteDatabase.execSQL(SQL_CREATE_NEWS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsContract.NewsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
