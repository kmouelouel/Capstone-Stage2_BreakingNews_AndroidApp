package com.example.kmoue.breakingnews;


import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.kmoue.breakingnews.data.NewsContract;
import com.example.kmoue.breakingnews.data.NewsDbHelper;
import com.example.kmoue.breakingnews.data.NewsProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;


@RunWith(AndroidJUnit4.class)
public class TestNewsProvider {

    /* Context used to access various parts of the system */
    private final Context mContext = InstrumentationRegistry.getTargetContext();

    @Before
    public void setUp() {
        deleteAllRecordsFromWeatherTable();
    }
    @Test
    public void testProviderRegistry() {
        String packageName = mContext.getPackageName();
        String newsProviderClassName = NewsProvider.class.getName();
        ComponentName componentName = new ComponentName(packageName, newsProviderClassName);

        try {
            PackageManager pm = mContext.getPackageManager();
            /* The ProviderInfo will contain the authority, which is what we want to test */
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            String actualAuthority = providerInfo.authority;
            String expectedAuthority = NewsContract.CONTENT_AUTHORITY;

            /* Make sure that the registered authority matches the authority from the Contract */
            String incorrectAuthority =
                    "Error: NewsProvider registered with authority: " + actualAuthority +
                            " instead of expected authority: " + expectedAuthority;
            assertEquals(incorrectAuthority,
                    actualAuthority,
                    expectedAuthority);

        } catch (PackageManager.NameNotFoundException e) {
            String providerNotRegisteredAtAll =
                    "Error: NewsProvider not registered at " + mContext.getPackageName();
            /*
             * This exception is thrown if the ContentProvider hasn't been registered with the
             * manifest at all. If this is the case, you need to double check your
             * AndroidManifest file
             */
            fail(providerNotRegisteredAtAll);
        }
    }

    @Test
    public void testBasicWeatherQuery() {
        /* Use WeatherDbHelper to get access to a writable database */
        NewsDbHelper dbHelper = new NewsDbHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        /* Obtain weather values from TestUtilities */
        ContentValues testNewsValues = TestUtilities.createTestNewsContentValues();
        /* Insert ContentValues into database and get a row ID back */
        long newsRowId = database.insert(
                /* Table to insert values into */
               NewsContract.NewsEntry.TABLE_NAME,
                null,
                /* Values to insert into table */
                testNewsValues);
        String insertFailed = "Unable to insert into the database";
        assertTrue(insertFailed, newsRowId != -1);

        /* We are done with the database, close it now. */
        database.close();

        Cursor newsCursor = mContext.getContentResolver().query(
                NewsContract.NewsEntry.CONTENT_URI,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Sort order to return in Cursor */
                null);

        /* This method will ensure that we  */
        TestUtilities.validateThenCloseCursor("testBasicWeatherQuery",
                newsCursor,
                testNewsValues);

    }

    private void deleteAllRecordsFromWeatherTable() {
        /* Access writable database through WeatherDbHelper */
       NewsDbHelper helper = new NewsDbHelper(InstrumentationRegistry.getTargetContext());
        SQLiteDatabase database = helper.getWritableDatabase();

        /* The delete method deletes all of the desired rows from the table, not the table itself */
        database.delete(NewsContract.NewsEntry.TABLE_NAME, null, null);

        /* Always close the database when you're through with it */
        database.close();
    }

}
