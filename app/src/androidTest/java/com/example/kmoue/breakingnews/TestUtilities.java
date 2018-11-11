package com.example.kmoue.breakingnews;

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.example.kmoue.breakingnews.data.NewsContract.NewsEntry;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class TestUtilities {
    static final int ID = 101;

    static final int BULK_INSERT_RECORDS_TO_INSERT = 10;
    static void validateThenCloseCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertNotNull(
                "This cursor is null. Did you make sure to register your ContentProvider in the manifest?",
                valueCursor);

        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }


    static String getStaticStringField(Class clazz, String variableName)
            throws NoSuchFieldException, IllegalAccessException {
        Field stringField = clazz.getDeclaredField(variableName);
        stringField.setAccessible(true);
        String value = (String) stringField.get(null);
        return value;
    }
    static Integer getStaticIntegerField(Class clazz, String variableName)
            throws NoSuchFieldException, IllegalAccessException {
        Field intField = clazz.getDeclaredField(variableName);
        intField.setAccessible(true);
        Integer value = (Integer) intField.get(null);
        return value;
    }
    static String studentReadableNoSuchField(NoSuchFieldException e) {
        String message = e.getMessage();

        Pattern p = Pattern.compile("No field (\\w*) in class L.*/(\\w*\\$?\\w*);");

        Matcher m = p.matcher(message);

        if (m.find()) {
            String missingFieldName = m.group(1);
            String classForField = m.group(2).replaceAll("\\$", ".");
            String fieldNotFoundReadableMessage = "Couldn't find "
                    + missingFieldName + " in class " + classForField + "."
                    + "\nPlease make sure you've declared that field and followed the TODOs.";
            return fieldNotFoundReadableMessage;
        } else {
            return e.getMessage();
        }
    }
    static String studentReadableClassNotFound(ClassNotFoundException e) {
        String message = e.getMessage();
        int indexBeforeSimpleClassName = message.lastIndexOf('.');
        String simpleClassNameThatIsMissing = message.substring(indexBeforeSimpleClassName + 1);
        simpleClassNameThatIsMissing = simpleClassNameThatIsMissing.replaceAll("\\$", ".");
        String fullClassNotFoundReadableMessage = "Couldn't find the class "
                + simpleClassNameThatIsMissing
                + ".\nPlease make sure you've created that class and followed the TODOs.";
        return fullClassNotFoundReadableMessage;
    }
    static String getConstantNameByStringValue(Class klass, String value)  {
        for (Field f : klass.getDeclaredFields()) {
            int modifiers = f.getModifiers();
            Class<?> type = f.getType();
            boolean isPublicStaticFinalString = Modifier.isStatic(modifiers)
                    && Modifier.isFinal(modifiers)
                    && Modifier.isPublic(modifiers)
                    && type.isAssignableFrom(String.class);

            if (isPublicStaticFinalString) {
                String fieldName = f.getName();
                try {
                    String fieldValue = (String) klass.getDeclaredField(fieldName).get(null);
                    if (fieldValue.equals(value)) return fieldName;
                } catch (IllegalAccessException e) {
                    return null;
                } catch (NoSuchFieldException e) {
                    return null;
                }
            }
        }

        return null;
    }


    static ContentValues createTestNewsContentValues() {

        ContentValues testNewsValues = new ContentValues();

        testNewsValues.put(NewsEntry.COLUMN_SOURCE_ID, "null");
        testNewsValues.put(NewsEntry.COLUMN_SOURCE_NAME, "Windowscentral.com");
        testNewsValues.put(NewsEntry.COLUMN_AUTHOR, "Asher Madan");
        testNewsValues.put(NewsEntry.COLUMN_TITLE, "Joker and Harley Quinn join PlayerUnknown's Battlegrounds (PUBG) soon");
        testNewsValues.put(NewsEntry.COLUMN_DESCRIPTION, "PlayerUnknown's Battlegrounds (PUBG) is a battle royale game focused on scavenging weapons and using stealth or other tactics to be the last person alive. The title has see unprecedented popularity on Steam and other platforms like the Xbox One.");
        testNewsValues.put(NewsEntry.COLUMN_URL, "https://www.windowscentral.com/joker-and-harley-quinn-join-playerunknowns-battlegrounds-pubg-soon");
        testNewsValues.put(NewsEntry.COLUMN_URL_TO_IMAGE, "https://www.windowscentral.com/sites/wpcentral.com/files/styles/large/public/field/image/2018/11/th.jpg?itok=SM8C6dFg");
        testNewsValues.put(NewsEntry.COLUMN_PUBLISHED_DATE, "2018-11-06T05:42:12Z");
        testNewsValues.put(NewsEntry.COLUMN_CONTENT, "PlayerUnknown's Battlegrounds (PUBG) is a battle royale game focused on scavenging weapons and using stealth or other tactics to be the last person alive. The title has see unprecedented popularity on Steam and other platforms like the Xbox One over the past … [+1454 chars]");
        return testNewsValues;
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int index = valueCursor.getColumnIndex(columnName);

            /* Test to see if the column is contained within the cursor */
            String columnNotFoundError = "Column '" + columnName + "' not found. " + error;
            assertFalse(columnNotFoundError, index == -1);

            /* Test to see if the expected value equals the actual value (from the Cursor) */
            String expectedValue = entry.getValue().toString();
            String actualValue = valueCursor.getString(index);

            String valuesDontMatchError = "Actual value '" + actualValue
                    + "' did not match the expected value '" + expectedValue + "'. "
                    + error;

            assertEquals(valuesDontMatchError,
                    expectedValue,
                    actualValue);
        }
    }

}
