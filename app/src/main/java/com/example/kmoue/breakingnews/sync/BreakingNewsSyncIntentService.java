package com.example.kmoue.breakingnews.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class BreakingNewsSyncIntentService extends IntentService {
    public BreakingNewsSyncIntentService(){
        super("BreakingNewsSyncIntentService");

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        BreakingNewsSyncTask.syncNews(this);
    }
}
