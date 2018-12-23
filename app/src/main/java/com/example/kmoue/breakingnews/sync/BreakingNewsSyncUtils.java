package com.example.kmoue.breakingnews.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.kmoue.breakingnews.data.NewsContract;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BreakingNewsSyncUtils {
    private static final int SYNC_INTERVAL_HOURS = 3;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;
    private static final String NEWS_SYNC_TAG = "breakingNews-sync";
    private static boolean sInitialized;
    // Create a method to schedule our periodic breakingNews sync

    static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context) {

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        /* Create the Job to periodically sync Sunshine */
        Job syncNewsJob = dispatcher.newJobBuilder()
                .setService(BreakingNewsFirebaseJobService.class)
                .setTag(NEWS_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(syncNewsJob);
    }

    synchronized public static void initialize(@NonNull final Context context, boolean inputInitialisation) {
        if (inputInitialisation) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    BreakingNewsSyncTask.syncNews(context);
                    return null;
                }
            }.execute();

        } else {
            if (sInitialized) return;
            sInitialized = true;
            scheduleFirebaseJobDispatcherSync(context);

            new AsyncTask<Void, Void, Void>() {
                @Override
                public Void doInBackground( Void... voids ) {
                    try{
                        /* URI for every row of news data in our news table*/
                        Uri newsQueryUri = NewsContract.NewsEntry.CONTENT_URI;

                        String[] projectionColumns = {NewsContract.NewsEntry._ID};
                        Cursor cursor = context.getContentResolver().query(
                                newsQueryUri,
                                null,
                                null,
                                null,
                                null);
                        if (null == cursor || cursor.getCount() == 0) {
                           startImmediateSync(context);
                        }

                        /* Make sure to close the Cursor to avoid memory leaks! */
                        cursor.close();

                    }
                    catch(Exception e){
                        Log.e("BreakingNewsSyncUtils",e.getMessage());
                    }
                    return null;
                }

           }.execute();

        }
    }




    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, BreakingNewsSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
