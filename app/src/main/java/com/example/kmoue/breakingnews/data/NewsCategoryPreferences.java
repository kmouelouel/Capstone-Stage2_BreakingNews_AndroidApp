package com.example.kmoue.breakingnews.data;

import android.content.Context;

public class NewsCategoryPreferences {

    private static String  category="";
    private static Boolean notificationsEnabled = true;
    private static long timeLastNotification =0;
    private static long lastNotificationTimeMillis=0;

    public static Boolean areNotificationsEnabled(Context context){
        return notificationsEnabled;
    }

    public static void saveLastNotificationTime(long time){
        lastNotificationTimeMillis=time;
    }
    public static long getEllapsedTimeSinceLastNotification(Context context) {
       long timeSinceLastNotification = System.currentTimeMillis() - lastNotificationTimeMillis;
        return timeSinceLastNotification;
    }
    public static String  returnCategory(Context context) {
        return category;
    }

    public static  void updateCategory(String mCategory) {
        category = mCategory;
    }
}
