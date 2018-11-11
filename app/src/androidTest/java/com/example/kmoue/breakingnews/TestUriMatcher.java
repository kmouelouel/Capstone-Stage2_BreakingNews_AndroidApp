package com.example.kmoue.breakingnews;


import android.content.UriMatcher;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import com.example.kmoue.breakingnews.data.NewsContract;
import com.example.kmoue.breakingnews.data.NewsProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.example.kmoue.breakingnews.TestUtilities.getStaticIntegerField;
import static com.example.kmoue.breakingnews.TestUtilities.studentReadableNoSuchField;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class TestUriMatcher {

    private static final Uri TEST_NEWS_DIR = NewsContract.NewsEntry.CONTENT_URI;
    private static final Uri TEST_NEWS_WITH_ID_DIR = NewsContract.NewsEntry
            .buildNewsUriWithID(TestUtilities.ID);

    private static final String newsCodeVariableName = "CODE_NEWS";
    private static int REFLECTED_NEWS_CODE;

    private static final String newsCodeWithIDVariableName = "CODE_NEWS_WITH_ID";
    private static int REFLECTED_NEWS_WITH_ID_CODE;


    private UriMatcher testMatcher;

    @Before
    public void before() {
        try {
            Method buildUriMatcher = NewsProvider.class.getDeclaredMethod("buildUriMatcher");
            testMatcher = (UriMatcher) buildUriMatcher.invoke(NewsProvider.class);
            REFLECTED_NEWS_CODE = getStaticIntegerField(
                    NewsProvider.class,
                    newsCodeVariableName);
            REFLECTED_NEWS_WITH_ID_CODE= getStaticIntegerField(
                    NewsProvider.class,
                    newsCodeWithIDVariableName);

        } catch (NoSuchFieldException e) {
            fail(studentReadableNoSuchField(e));
        } catch (IllegalAccessException e) {
                fail(e.getMessage());
            }catch (NoSuchMethodException e) {
            String noBuildUriMatcherMethodFound =
                    "It doesn't appear that you have created a method called buildUriMatcher in " +
                            "the NewsProvider class.";
            fail(noBuildUriMatcherMethodFound);
        } catch (InvocationTargetException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testUriMatcher() {
        String NewsUriDoesNotMatch = "Error: The CODE_NEWS URI was matched incorrectly.";
        int actualNewsCode = testMatcher.match(TEST_NEWS_DIR);
        int expectedNewsCode = REFLECTED_NEWS_CODE;
        assertEquals(NewsUriDoesNotMatch,
                expectedNewsCode,
                actualNewsCode);

        String newsWithDateUriCodeDoesNotMatch =
                "Error: The CODE_NEWS WITH DATE URI was matched incorrectly.";
        int actualNewsWithDateCode = testMatcher.match(TEST_NEWS_WITH_ID_DIR);
        int expectedNewsWithDateCode = REFLECTED_NEWS_WITH_ID_CODE;
        assertEquals(newsWithDateUriCodeDoesNotMatch,
                expectedNewsWithDateCode,
                actualNewsWithDateCode);

    }




}
