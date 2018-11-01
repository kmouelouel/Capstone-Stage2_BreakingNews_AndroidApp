package com.example.kmoue.breakingnews.data;

import android.content.Context;

public class NewsCategoryPreferences {
    private String category;
    Context mContext;
    public NewsCategoryPreferences(Context context){
        mContext = context;
        category = "";
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
