package com.example.kmoue.breakingnews.utilities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.kmoue.breakingnews.DetailActivity;
import com.example.kmoue.breakingnews.MainActivity;
import com.example.kmoue.breakingnews.R;
import com.example.kmoue.breakingnews.data.NewsContract;
import com.example.kmoue.breakingnews.sync.BreakingNewsSyncIntentService;
import com.example.kmoue.breakingnews.sync.BreakingNewsSyncTask;

public class NotificationUtils {

    public static final String[] NEWS_NOTIFICATION_PROJECTION = {
            NewsContract.NewsEntry.COLUMN_NEWS_ID,
            NewsContract.NewsEntry.COLUMN_TITLE,
            NewsContract.NewsEntry.COLUMN_AUTHOR,
            NewsContract.NewsEntry.COLUMN_SOURCE_NAME

    };
    private static final int NEWS_NOTIFICATION_ID = 2;
    private static final int NEWS_NOTIFICATION_INTENT_ID = 111;
    private static final String NOTIFICATION_CHANNEL_ID = "notification_Channel";
    private static final int ACTION_SHOW_NEWS_PENDING_INTENT_ID = 1;
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 14;

    public static void notifyUserOfNewBreakingNews(Context context) {
        /* Build the URI for today's weather in order to show up to date data in notification */
        Uri lastBreakingNewsUri = NewsContract.NewsEntry.buildNewsUriWithID(0);
        // Toast.makeText(this, lastBreakingNewsUri., Toast.LENGTH_SHORT).show();
        Cursor lastBreakingNewsCursor = context.getContentResolver().query(
                lastBreakingNewsUri,
                null,
                null,
                null,
                null);
        if (lastBreakingNewsCursor.moveToFirst()) {
            int titleCol = lastBreakingNewsCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_TITLE);
            int authorCol = lastBreakingNewsCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_AUTHOR);
            int sourceCol = lastBreakingNewsCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_SOURCE_NAME);
            String title = lastBreakingNewsCursor.getString(titleCol);
            String author = lastBreakingNewsCursor.getString(authorCol);
            String source = lastBreakingNewsCursor.getString(sourceCol);
            Resources resources = context.getResources();

            String notificationTitle = context.getString(R.string.app_name);

            String notificationText = getNotificationText(context, title, author, source);

            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            // COMPLETED (9) Create a notification channel for Android O devices
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        NOTIFICATION_CHANNEL_ID,
                        context.getString(R.string.main_notification_channel_name),
                        NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(mChannel);
            }
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                            .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                            .setSmallIcon(R.drawable.news)
                            .setLargeIcon(largeIcon(context))
                            .setContentTitle(notificationTitle)
                            .setContentText(notificationText)
                            .setContentIntent(contentIntent(context, lastBreakingNewsUri))
                            .setAutoCancel(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                    && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            }
            notificationManager.notify(NEWS_NOTIFICATION_ID, notificationBuilder.build());

        }
        lastBreakingNewsCursor.close();

    }

    private static String getNotificationText(Context context, String title, String author, String source) {
        String notificationText = title + "\n written by: " + author + " \nSourced :" + source;

        return notificationText;


    }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        return BitmapFactory.decodeResource(res, R.drawable.news);

    }

    private static PendingIntent contentIntent(Context context, Uri path) {
        Intent startActivityIntent = new Intent(context, DetailActivity.class);
        startActivityIntent.setData(path);
        return PendingIntent.getActivity(
                context,
                NEWS_NOTIFICATION_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }


}
