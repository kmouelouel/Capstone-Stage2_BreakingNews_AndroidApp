package com.example.kmoue.breakingnews;

import android.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kmoue.breakingnews.data.NewsContract;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity
        implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    private TextView mNewsDisplay;
    private TextView mSourceId;
    private TextView mSourceName;
    private TextView mTitle;
    private TextView mAuthor;
    private TextView mDescription;
    private TextView mUrlLink;
    private ImageView mUrlImage;
    private TextView mContent;
    private TextView mPublishedAt;


    private static final int ID_DETAIL_LOADER = 33;
    private String mNewsSummary;
    private Uri mUri;
    public static final String[] NEWS_DETAIL_PROJECTION = {
            NewsContract.NewsEntry.COLUMN_SOURCE_ID,
            NewsContract.NewsEntry.COLUMN_SOURCE_NAME,
            NewsContract.NewsEntry.COLUMN_TITLE,
            NewsContract.NewsEntry.COLUMN_AUTHOR,
            NewsContract.NewsEntry.COLUMN_URL,
            NewsContract.NewsEntry.COLUMN_PUBLISHED_DATE,
            NewsContract.NewsEntry.COLUMN_URL,
            NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE,
            NewsContract.NewsEntry.COLUMN_CONTENT
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mSourceId = (TextView) findViewById(R.id.news_source_id);
        mSourceName = (TextView) findViewById(R.id.news_source_name);
        mTitle = (TextView) findViewById(R.id.news_title);
        mAuthor = (TextView) findViewById(R.id.news_author);
        mDescription = (TextView) findViewById(R.id.news_description);
        mUrlLink = (TextView) findViewById(R.id.news_url);
        mUrlImage = (ImageView) findViewById(R.id.news_urlToImage);
        mContent = (TextView) findViewById(R.id.news_content);
        mPublishedAt= (TextView)  findViewById(R.id.tv_published_date);
       mUri = getIntent().getData();
       if(mUri == null) throw new NullPointerException("URI for DetailActivity cannot be null");

        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        switch (loaderId) {
            case ID_DETAIL_LOADER:
            return new CursorLoader(this,
                    mUri,
                    null,
                    null,
                    null,
                    null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            return;
        }

        int titleCol=data.getColumnIndex(NewsContract.NewsEntry.COLUMN_TITLE);
        int authorCol= data.getColumnIndex(NewsContract.NewsEntry.COLUMN_AUTHOR);
        int publishedDateCol= data.getColumnIndex(NewsContract.NewsEntry.COLUMN_PUBLISHED_DATE);
        int descriptionCol= data.getColumnIndex(NewsContract.NewsEntry.COLUMN_DESCRIPTION);
        int sourceIdCol= data.getColumnIndex(NewsContract.NewsEntry.COLUMN_SOURCE_ID);
        int sourceNameCol= data.getColumnIndex(NewsContract.NewsEntry.COLUMN_SOURCE_NAME);
        int contentCol= data.getColumnIndex(NewsContract.NewsEntry.COLUMN_CONTENT);
        int urlLinkCol= data.getColumnIndex(NewsContract.NewsEntry.COLUMN_PUBLISHED_DATE);
       // int urlImageCol= data.getColumnIndex(NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE);
        mTitle.setText(data.getString(titleCol));
        mAuthor.setText(data.getString(authorCol));
        mPublishedAt.setText(data.getString(publishedDateCol));
        mDescription.setText(data.getString(descriptionCol));
        mSourceId.setText(data.getString(sourceIdCol));
        mSourceName.setText(data.getString(sourceNameCol));
        mContent.setText(data.getString(contentCol));
        mUrlLink.setText(data.getString(urlLinkCol));
       // Picasso.with(this).load(data.getString(urlImageCol)).into(mUrlImage);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
