package com.example.kmoue.breakingnews.data;

import android.content.Context;

public class NewsCategoryPreferences {

    private static String  category;


    public static String  returnCategory(Context context) {
        return category;
    }

    public static  void updateCategory(String mCategory) {
        category = mCategory;
    }
}
