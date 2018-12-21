package com.example.kmoue.breakingnews;

import android.content.res.Configuration;
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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    private static final String SAVE_CATEGORY_KEY = "savedCategory";
    private static final String SAVE_ADAPTER_POSITION_KEY = "savedPosition";
    private static String mCategory = "";
    private int mPosition = RecyclerView.NO_POSITION;
    private ProgressBar mLoadingIndicator;
    private RecyclerView mRecyclerView;
    private NewsAdapter mNewsAdapter;
    private GridLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.progressBar_indicator);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_news);
        layoutManager = new GridLayoutManager(this, getSpan());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mNewsAdapter = new NewsAdapter(this, this);
        mRecyclerView.setAdapter(mNewsAdapter);

        showLoading();
        int loaderId = NEWS_LOADER_ID;
        LoaderManager.LoaderCallbacks<Cursor> callback = MainActivity.this;
        Bundle bundleForLoader = null;
        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);
        BreakingNewsSyncUtils.initialize(this, false);

    }

    private int getSpan() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return 2;
        }
        return 1;
    }

    private void showLoading() {
        /* Then, hide the news data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(int idPosition) {
        mPosition = idPosition;
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
        mPosition = 0;
        UpdateTitle(itemThatWasClickedId);
        switch (itemThatWasClickedId) {
            case R.id.action_business:
                mCategory = getResources().getString(R.string.business).toLowerCase();
                NewsCategoryPreferences.updateCategory(mCategory);
                break;
            case R.id.action_entertainment:
                mCategory = getResources().getString(R.string.entertainment).toLowerCase();
                NewsCategoryPreferences.updateCategory(mCategory);
                break;
            case R.id.action_general:
                mCategory = getResources().getString(R.string.general).toLowerCase();
                NewsCategoryPreferences.updateCategory(mCategory);
                break;
            case R.id.action_health:
                mCategory = getResources().getString(R.string.health).toLowerCase();
                NewsCategoryPreferences.updateCategory(mCategory);
                break;
            case R.id.action_science:
                mCategory = getResources().getString(R.string.science).toLowerCase();
                NewsCategoryPreferences.updateCategory(mCategory);
                break;
            case R.id.action_sports:
                mCategory = getResources().getString(R.string.sports).toLowerCase();
                NewsCategoryPreferences.updateCategory(mCategory);
                break;
            case R.id.action_technology:
                mCategory = getResources().getString(R.string.technology).toLowerCase();
                NewsCategoryPreferences.updateCategory(mCategory);
                break;


        }
        invalidateData();
        getSupportLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
        BreakingNewsSyncUtils.initialize(this, true);
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


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (layoutManager != null) {
            mPosition = layoutManager.findFirstVisibleItemPosition();
            outState.putInt(SAVE_ADAPTER_POSITION_KEY, mPosition);
        } else {
            mPosition = RecyclerView.NO_POSITION;
        }

        outState.putString(SAVE_CATEGORY_KEY, mCategory);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPosition = savedInstanceState.getInt(SAVE_ADAPTER_POSITION_KEY);
        mCategory = savedInstanceState.getString(SAVE_CATEGORY_KEY);
        mRecyclerView.smoothScrollToPosition(mPosition);
        if (!mCategory.equals("")) {
            this.setTitle(getResources().getString(R.string.app_name) + " - " + mCategory.toUpperCase());
        } else {
            this.setTitle(getResources().getString(R.string.app_name));
        }
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

    private void UpdateTitle(int inSelectedOption) {
        Context context = getApplicationContext();
        switch (inSelectedOption) {
            case R.id.action_business:
                this.setTitle(getResources().getString(R.string.business));
                break;
            case R.id.action_entertainment:
                this.setTitle(getResources().getString(R.string.entertainment));
                break;
            case R.id.action_general:
                this.setTitle(getResources().getString(R.string.general));
                break;
            case R.id.action_health:
                this.setTitle(getResources().getString(R.string.health));
                break;
            case R.id.action_science:
                this.setTitle(getResources().getString(R.string.science));
                break;
            case R.id.action_sports:
                this.setTitle(getResources().getString(R.string.sports));
                break;
            case R.id.action_technology:
                this.setTitle(getResources().getString(R.string.technology));
                break;

            default:
                this.setTitle(getResources().getString(R.string.app_name));
        }

    }
}

