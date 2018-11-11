package com.example.kmoue.breakingnews;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.kmoue.breakingnews.data.NewsContract;

import static com.example.kmoue.breakingnews.TestUtilities.getConstantNameByStringValue;
import static com.example.kmoue.breakingnews.TestUtilities.getStaticIntegerField;
import static com.example.kmoue.breakingnews.TestUtilities.getStaticStringField;
import static com.example.kmoue.breakingnews.TestUtilities.studentReadableClassNotFound;
import static com.example.kmoue.breakingnews.TestUtilities.studentReadableNoSuchField;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;

@RunWith(AndroidJUnit4.class)
public class TestNewsDatabase {
    private static final String packageName = "com.example.kmoue.breakingnews";
    private static final String dataPackageName = packageName + ".data";
    private static final String newsContractName = ".NewsContract";
    private static final String newsEntryName = newsContractName + "$NewsEntry";
    private static final String newsDbHelperName = ".NewsDbHelper";
    private static final String databaseNameVariableName = "DATABASE_NAME";
    private static final String databaseVersionVariableName = "DATABASE_VERSION";
    private static final String tableNameVariableName = "TABLE_NAME";
    private static final String columnWeatherIdVariableName = "_ID";
    private static final String columnSourceIdVariableName = "COLUMN_SOURCE_ID";
    private static final String columnSourceNameVariableName = "COLUMN_SOURCE_NAME";
    private static final String columnAuthorVariableName = "COLUMN_AUTHOR";
    private static final String columnTitleVariableName = "COLUMN_TITLE";
    private static final String columnDescriptionVariableName = "COLUMN_DESCRIPTION";
    private static final String columnUrlVariableName = "COLUMN_URL";
    private static final String columnUrlToImageVariableName = "COLUMN_URL_TO_IMAGE";
    private static final String columnPublishedDateVariableName = "COLUMN_PUBLISHED_DATE";
    private static final String columnContentVariableName = "COLUMN_CONTENT";
    static String REFLECTED_COLUMN_ID;
    static String REFLECTED_COLUMN_SOURCE_ID;
    static String REFLECTED_COLUMN_SOURCE_NAME;
    static String REFLECTED_COLUMN_AUTHOR;
    static String REFLECTED_COLUMN_TITLE;
    static String REFLECTED_COLUMN_DESCRIPTION;
    static String REFLECTED_COLUMN_URL;
    static String REFLECTED_COLUMN_URL_TO_IMAGE;
    static String REFLECTED_COLUMN_PUBLISHED_DATE;
    static String REFLECTED_COLUMN_CONTENT;
    private static String REFLECTED_DATABASE_NAME;
    private static int REFLECTED_DATABASE_VERSION;
    private static String REFLECTED_TABLE_NAME;
    private final Context context = InstrumentationRegistry.getTargetContext();
    private Class newsEntryClass;
    private Class newsDbHelperClass;
    private SQLiteDatabase database;
    private SQLiteOpenHelper dbHelper;

    @Before
    public void before() {
        try {
            newsEntryClass = Class.forName(dataPackageName + newsEntryName);
            if (!BaseColumns.class.isAssignableFrom(newsEntryClass)) {
                String newsEntryDoesNotImplementBaseColumns = "NewsEntry class needs to " +
                        "implement the interface BaseColumns, but does not.";
                fail(newsEntryDoesNotImplementBaseColumns);
            }
            REFLECTED_TABLE_NAME = getStaticStringField(newsEntryClass, tableNameVariableName);
            REFLECTED_COLUMN_SOURCE_ID = getStaticStringField(newsEntryClass, columnSourceIdVariableName);
            REFLECTED_COLUMN_SOURCE_NAME = getStaticStringField(newsEntryClass, columnSourceNameVariableName);
            REFLECTED_COLUMN_AUTHOR = getStaticStringField(newsEntryClass, columnAuthorVariableName);
            REFLECTED_COLUMN_TITLE = getStaticStringField(newsEntryClass, columnTitleVariableName);
            REFLECTED_COLUMN_DESCRIPTION = getStaticStringField(newsEntryClass, columnDescriptionVariableName);
            REFLECTED_COLUMN_PUBLISHED_DATE = getStaticStringField(newsEntryClass, columnPublishedDateVariableName);
            REFLECTED_COLUMN_CONTENT = getStaticStringField(newsEntryClass, columnContentVariableName);
            REFLECTED_COLUMN_URL = getStaticStringField(newsEntryClass, columnUrlVariableName);
            REFLECTED_COLUMN_URL_TO_IMAGE = getStaticStringField(newsEntryClass, columnUrlToImageVariableName);

            newsDbHelperClass = Class.forName(dataPackageName + newsDbHelperName);

            Class newsDbHelperSuperclass = newsDbHelperClass.getSuperclass();
            if (newsDbHelperSuperclass == null || newsDbHelperSuperclass.equals(Object.class)) {
                String noExplicitSuperclass =
                        "NewsDbHelper needs to extend SQLiteOpenHelper, but yours currently doesn't extend a class at all.";
                fail(noExplicitSuperclass);
            } else if (newsDbHelperSuperclass != null) {
                String newsDbHelperSuperclassName = newsDbHelperSuperclass.getSimpleName();
                String doesNotExtendOpenHelper =
                        "NewsDbHelper needs to extend SQLiteOpenHelper but yours extends "
                                + newsDbHelperSuperclassName;

                assertTrue(doesNotExtendOpenHelper,
                        SQLiteOpenHelper.class.isAssignableFrom(newsDbHelperSuperclass));

            }
            REFLECTED_DATABASE_NAME = getStaticStringField(
                    newsDbHelperClass, databaseNameVariableName);

            REFLECTED_DATABASE_VERSION = getStaticIntegerField(
                    newsDbHelperClass, databaseVersionVariableName);
            Constructor newsDbHelperCtor = newsDbHelperClass.getConstructor(Context.class);

            dbHelper = (SQLiteOpenHelper) newsDbHelperCtor.newInstance(context);
            context.deleteDatabase(REFLECTED_DATABASE_NAME);
            Method getWritableDatabase = SQLiteOpenHelper.class.getDeclaredMethod("getWritableDatabase");
            database = (SQLiteDatabase) getWritableDatabase.invoke(dbHelper);

        } catch (ClassNotFoundException e) {
            fail(studentReadableClassNotFound(e));
        } catch (NoSuchFieldException e) {
            fail(studentReadableNoSuchField(e));
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (InstantiationException e) {
            fail(e.getMessage());
        } catch (InvocationTargetException e) {
            fail(e.getMessage());
        }


    }

    @Test
    public void testDatabaseVersionWasIncremented() {
        int expectedDatabaseVersion = 1;
        String databaseVersionShouldBe1 = "Database version should be "
                + expectedDatabaseVersion + " but isn't."
                + "\n Database version: ";

        assertEquals(databaseVersionShouldBe1,
                expectedDatabaseVersion,
                REFLECTED_DATABASE_VERSION);

    }

    @Test
    public void testDuplicateDateInsertBehaviorShouldReplace() {
        /* Obtain weather values from TestUtilities */
        ContentValues testNewsValues = TestUtilities.createTestNewsContentValues();
        database.insert(
                NewsContract.NewsEntry.TABLE_NAME,
                null,
                testNewsValues);
        /* Query for a weather record with our new weather ID */
        Cursor newNewsIdCursor = database.query(
                REFLECTED_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        String recordWithNewIdNotFound =
                "New record did not overwrite the previous record for the same date.";
        assertTrue(recordWithNewIdNotFound,
                newNewsIdCursor.getCount() == 1);

        /* Always close the cursor after you're done with it */
        newNewsIdCursor.close();


    }

    @Test
    public void testNullColumnConstraints() {
        Cursor newsTableCursor = database.query(
                REFLECTED_TABLE_NAME,
                /* We don't care about specifications, we just want the column names */
                null, null, null, null, null, null);

        /* Store the column names and close the cursor */
        String[] newsTableColumnNames = newsTableCursor.getColumnNames();
        newsTableCursor.close();
        ContentValues testValues = TestUtilities.createTestNewsContentValues();
        ContentValues testValuesReferenceCopy = new ContentValues(testValues);
        for (String columnName : newsTableColumnNames) {
            if (columnName.equals(NewsContract.NewsEntry._ID)) continue;
            testValues.putNull(columnName);
            /* Insert ContentValues into database and get a row ID back */
            long shouldFailRowId = database.insert(
                    REFLECTED_TABLE_NAME,
                    null,
                    testValues);

            String variableName = getConstantNameByStringValue(
                    NewsContract.NewsEntry.class,
                    columnName);
            /* If the insert fails, which it should in this case, database.insert returns -1 */
            String nullRowInsertShouldFail =
                    "Insert should have failed due to a null value for column: '" + columnName + "'"
                            + ", but didn't."
                            + "\n Check that you've added NOT NULL to " + variableName
                            + " in your create table statement in the WeatherEntry class."
                            + "\n Row ID: ";
            assertEquals(nullRowInsertShouldFail,
                    -1,
                    shouldFailRowId);

            /* "Restore" the original value in testValues */
            testValues.put(columnName, testValuesReferenceCopy.getAsString(columnName));
        }
        // close the database
        dbHelper.close();

    }

    @Test
    public void testIntegerAutoincrement() {

        /* First, let's ensure we have some values in our table initially */
        testInsertSingleRecordIntoNewsTable();
        /* Obtain weather values from TestUtilities */
        ContentValues testWeatherValues = TestUtilities.createTestNewsContentValues();
        /* Get the date of the testWeatherValues to ensure we use a different date later */
        String originalID = testWeatherValues.getAsString(REFLECTED_COLUMN_SOURCE_ID);

        /* Insert ContentValues into database and get a row ID back */
        long firstRowId = database.insert(
                REFLECTED_TABLE_NAME,
                null,
                testWeatherValues);

        /* Delete the row we just inserted to see if the database will reuse the rowID */
        database.delete(
                REFLECTED_TABLE_NAME,
                "_ID == " + firstRowId,
                null);


    }

    @Test
    public void testOnUpgradeBehavesCorrectly() {
        testInsertSingleRecordIntoNewsTable();
        dbHelper.onUpgrade(database, 13, 14);
        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='" + REFLECTED_TABLE_NAME + "'",
                null);

        int expectedTableCount = 1;
        String shouldHaveSingleTable = "There should only be one table returned from this query.";
        assertEquals(shouldHaveSingleTable,
                expectedTableCount,
                tableNameCursor.getCount());

        /* We are done verifying our table names, so we can close this cursor */
        tableNameCursor.close();

        Cursor shouldBeEmptyNewsCursor = database.query(
                REFLECTED_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        int expectedRecordCountAfterUpgrade = 0;

        String newsTableShouldBeEmpty =
                "news table should be empty after upgrade, but wasn't."
                        + "\nNumber of records: ";
        assertEquals(newsTableShouldBeEmpty,
                expectedRecordCountAfterUpgrade,
                shouldBeEmptyNewsCursor.getCount());

        /* Test is over, close the cursor */
        database.close();

    }

    @Test
    public void testCreateDb() {
        final HashSet<String> tableNameHashSet = new HashSet<>();
        /* Here, we add the name of our only table in this particular database */
        tableNameHashSet.add(REFLECTED_TABLE_NAME);

        /* Here, we add the name of our only table in this particular database */
        tableNameHashSet.add(REFLECTED_TABLE_NAME);
        /* We think the database is open, let's verify that here */
        String databaseIsNotOpen = "The database should be open and isn't";
        assertEquals(databaseIsNotOpen,
                true,
                database.isOpen());

        /* This Cursor will contain the names of each table in our database */
        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table'",
                null);
        String errorInCreatingDatabase =
                "Error: This means that the database has not been created correctly";
        assertTrue(errorInCreatingDatabase,
                tableNameCursor.moveToFirst());

        do {
            tableNameHashSet.remove(tableNameCursor.getString(0));
        } while (tableNameCursor.moveToNext());
        assertTrue("Error: Your database was created without the expected tables.",
                tableNameHashSet.isEmpty());
        /* Always close the cursor when you are finished with it */
        tableNameCursor.close();

    }


    @Test
    public void testInsertSingleRecordIntoNewsTable() {
        /* Obtain weather values from TestUtilities */
        ContentValues testNewsValues = TestUtilities.createTestNewsContentValues();

        /* Insert ContentValues into database and get a row ID back */
        long newsRowId = database.insert(
                REFLECTED_TABLE_NAME,
                null,
                testNewsValues);

        /* If the insert fails, database.insert returns -1 */
        int valueOfIdIfInsertFails = -1;
        String insertFailed = "Unable to insert into the database";
        assertNotSame(insertFailed,
                valueOfIdIfInsertFails,
                newsRowId);

        Cursor newsCursor = database.query(
                /* Name of table on which to perform the query */
                REFLECTED_TABLE_NAME,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Columns to group by */
                null,
                /* Columns to filter by row groups */
                null,
                /* Sort order to return in Cursor */
                null);

        /* Cursor.moveToFirst will return false if there are no records returned from your query */
        String emptyQueryError = "Error: No Records returned from news query";
        assertTrue(emptyQueryError,
                newsCursor.moveToFirst());

        /* Verify that the returned results match the expected results */
        String expectedNewsDidntMatchActual =
                "Expected news values didn't match actual values.";
        TestUtilities.validateCurrentRecord(expectedNewsDidntMatchActual,
                newsCursor,
                testNewsValues);
        assertFalse("Error: More than one record returned from news query",
                newsCursor.moveToNext());

        /* Close cursor */
        newsCursor.close();


    }


}
