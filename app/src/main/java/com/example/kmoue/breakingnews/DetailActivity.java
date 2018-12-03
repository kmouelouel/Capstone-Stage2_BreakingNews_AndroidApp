package com.example.kmoue.breakingnews;

import android.app.LoaderManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kmoue.breakingnews.data.NewsContract;
import com.example.kmoue.breakingnews.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity
        implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

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
    private static final int ID_DETAIL_LOADER = 33;
    private static String news_Link = "";
    ActivityDetailBinding mBinding;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        mUri = getIntent().getData();
        if (mUri == null) throw new NullPointerException("URI for DetailActivity cannot be null");

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

        int titleCol = data.getColumnIndex(NewsContract.NewsEntry.COLUMN_TITLE);
        int authorCol = data.getColumnIndex(NewsContract.NewsEntry.COLUMN_AUTHOR);
        int publishedDateCol = data.getColumnIndex(NewsContract.NewsEntry.COLUMN_PUBLISHED_DATE);
        int descriptionCol = data.getColumnIndex(NewsContract.NewsEntry.COLUMN_DESCRIPTION);
        int sourceNameCol = data.getColumnIndex(NewsContract.NewsEntry.COLUMN_SOURCE_NAME);
        int contentCol = data.getColumnIndex(NewsContract.NewsEntry.COLUMN_CONTENT);
        int urlImageCol = data.getColumnIndex(NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE);
        int linkCol = data.getColumnIndex(NewsContract.NewsEntry.COLUMN_URL);
        if (data.getString(urlImageCol).equals("null")) {
            Picasso.with(this).load(R.drawable.breakingnewsfeature).into(mBinding.newsUrlToImage);
        } else {
            Picasso.with(this).load(data.getString(urlImageCol)).into(mBinding.newsUrlToImage);
        }


        mBinding.newsTitle.setText(data.getString(titleCol));
        //author
        if (data.getString(authorCol).equals("null")) {
            mBinding.newsAuthor.setVisibility(View.GONE);
            mBinding.authorLabel.setVisibility(View.GONE);
        } else {
            mBinding.newsAuthor.setText(data.getString(authorCol));
        }
        //description
        if (data.getString(descriptionCol).equals("null")) {
            mBinding.newsDescription.setVisibility(View.GONE);
            mBinding.descriptionLabel.setVisibility(View.GONE);
        } else {
            mBinding.newsDescription.setText(data.getString(descriptionCol));
        }
        //content
        if (data.getString(contentCol).equals("null")) {
            mBinding.newsContent.setVisibility(View.GONE);
            mBinding.contentLabel.setVisibility(View.GONE);
        } else {
            mBinding.newsContent.setText(data.getString(contentCol));
        }
        // mBinding.newsDescription.setText(data.getString(descriptionCol));
        mBinding.newsSourceName.setText(data.getString(sourceNameCol));
        news_Link = data.getString(linkCol);
        String mPublishedDate =data.getString(publishedDateCol);
        mPublishedDate=  mPublishedDate.replaceAll("[A-Z]?", " ");
        mBinding.tvPublishedDate.setText(mPublishedDate);
        mBinding.newsContent.setText(data.getString(contentCol));

    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_link, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId= item.getItemId();
        if (itemId == R.id.action_linkTo) {
            Intent linkToGo = new Intent(Intent.ACTION_VIEW);
            linkToGo.setData(Uri.parse(news_Link));
            startActivity(linkToGo);

        }
        return true;
    }
}
