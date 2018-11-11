package com.example.kmoue.breakingnews.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class NewsProvider extends ContentProvider {
    // define final integer constants for the directory of news and a single item
    public static final int CODE_NEWS= 100;
    public static final int CODE_NEWS_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private NewsDbHelper mOpenHelper;
    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = NewsContract.CONTENT_AUTHORITY;
        //directory
        matcher.addURI(authority, NewsContract.PATH_NEWS, CODE_NEWS);
       //single item
        matcher.addURI(authority, NewsContract.PATH_NEWS + "/#", CODE_NEWS_WITH_ID);
        return matcher;
    }
    @Override

    public boolean onCreate() {
        mOpenHelper = new NewsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor  query(@NonNull Uri uri, String[] projection, String selection,
                         String[] selectionArgs, String sortOrder) {
        //get access to underlying database (read-only for query)
        final SQLiteDatabase db= mOpenHelper.getReadableDatabase();
        //URI match code
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match) {
            //Query for the tasks directory
            case CODE_NEWS:
                retCursor = db.query(
                        NewsContract.NewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            case CODE_NEWS_WITH_ID:
                //using selection and selectionArgs
                //URI: content://<authority>/tasks/#
                String id = uri.getPathSegments().get(1);
                //Selection is the _ and the Selection argsID_coluum =?, and the selection args= the row ID from URI
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};
                retCursor = db.query(NewsContract.NewsEntry.TABLE_NAME,projection,mSelection,mSelectionArgs, null, null,sortOrder);
          // default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        //Get access to the task database (To write new data to)
        final SQLiteDatabase db= mOpenHelper.getWritableDatabase();
        // write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        //insert new values into the database
        //set the value for the returnUri and write the default case for unknown URI's
       Uri returnUri;
        switch(match){
            case CODE_NEWS:
                long  id = db.insert(NewsContract.NewsEntry.TABLE_NAME,null,contentValues);
                if(id>0){
                    //success
                    returnUri = ContentUris.withAppendedId(NewsContract.NewsEntry.CONTENT_URI,id);

                }
                else{
                    throw new android.database.SQLException("Failed to insert row into..."+uri);
                }
                break;
                default:
                    throw new UnsupportedOperationException("Unknown uri"+ uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
       return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int numRowsDeleted;
        if (null == selection) selection = "1";
        switch (sUriMatcher.match(uri)) {
            case CODE_NEWS:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        NewsContract.NewsEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsDeleted;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        return super.bulkInsert(uri, values);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
