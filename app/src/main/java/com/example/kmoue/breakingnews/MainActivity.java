package com.example.kmoue.breakingnews;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.support.v4.content.Loader;

import com.example.kmoue.breakingnews.adapters.NewsAdapter;
import com.example.kmoue.breakingnews.data.NewsCategoryPreferences;
import com.example.kmoue.breakingnews.data.NewsContract;
import com.example.kmoue.breakingnews.sync.BreakingNewsSyncUtils;


public class MainActivity extends AppCompatActivity
        implements NewsAdapter.NewsAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final int NEWS_LOADER_ID = 22;
    private static final String SAVE_STATE_KEY = "savedState";
    private int mPosition = RecyclerView.NO_POSITION;
    private ProgressBar mLoadingIndicator;
    private NewsCategoryPreferences mNewsCategoryPreferences;
    private RecyclerView mRecyclerView;
    private NewsAdapter mNewsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0f);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.progressBar_indicator);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_news);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mNewsAdapter = new NewsAdapter(this, this);
        mRecyclerView.setAdapter(mNewsAdapter);
        mNewsCategoryPreferences = new NewsCategoryPreferences(this);
        showLoading();
        int loaderId = NEWS_LOADER_ID;
        LoaderManager.LoaderCallbacks<Cursor> callback = MainActivity.this;
        Bundle bundleForLoader = null;
        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);
        BreakingNewsSyncUtils.initialize(this);

    }

    private void showLoading() {
        /* Then, hide the news data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(int idPosition) {
        Intent intentToStartDetailActivity = new Intent(this, DetailActivity.class);
        Uri uriForNewsClicked = NewsContract.NewsEntry.buildNewsUriWithID(idPosition);
        intentToStartDetailActivity.setData(uriForNewsClicked);
        startActivity(intentToStartDetailActivity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        Context context = MainActivity.this;
        String textToShow = "";
        switch (itemThatWasClickedId) {
            case R.id.action_refresh:
                textToShow = "refresh clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_business:
                mNewsCategoryPreferences.setCategory("business");
                textToShow = "Business clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_entertainment:
                mNewsCategoryPreferences.setCategory("entertainment");
                textToShow = "Entertainment clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_general:
                mNewsCategoryPreferences.setCategory("general");
                textToShow = "general clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_health:
                mNewsCategoryPreferences.setCategory("health");
                textToShow = "health clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_science:
                mNewsCategoryPreferences.setCategory("science");
                textToShow = "science clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_sports:
                mNewsCategoryPreferences.setCategory("sports");
                textToShow = "sports clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_technology:
                mNewsCategoryPreferences.setCategory("technology");
                textToShow = "technology clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
        }
        invalidateData();
        getSupportLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
        return super.onOptionsItemSelected(item);
    }


    private void showDataView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }


    private void invalidateData() {
        mNewsAdapter.swapCursor(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle args) {

        switch (loaderId) {
            case NEWS_LOADER_ID:
                Uri QueryUri = NewsContract.NewsEntry.CONTENT_URI;
                return new CursorLoader(this,
                        QueryUri,
                        null,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mNewsAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0) showDataView();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mNewsAdapter.swapCursor(null);
    }

}

