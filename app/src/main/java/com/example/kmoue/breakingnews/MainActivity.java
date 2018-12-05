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
    private static final String SAVE_STATE_KEY = "savedState";
    private int mPosition = RecyclerView.NO_POSITION;
    private ProgressBar mLoadingIndicator;
    private NewsCategoryPreferences mNewsCategoryPreferences;
    private RecyclerView mRecyclerView;
    private NewsAdapter mNewsAdapter;
    private TextView mTextViewCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextViewCategory = (TextView) findViewById(R.id.textView_category);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.progressBar_indicator);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_news);
       // LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager layoutManager = new GridLayoutManager(this, getSpan());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mNewsAdapter = new NewsAdapter(this, this);
        mRecyclerView.setAdapter(mNewsAdapter);
        showLoading();
        int loaderId = NEWS_LOADER_ID;
        LoaderManager.LoaderCallbacks<Cursor> callback = MainActivity.this;
        Bundle bundleForLoader = null;
        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);
        BreakingNewsSyncUtils.initialize(this,false);

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
            case R.id.action_business:
                NewsCategoryPreferences.updateCategory("business");
                textToShow = "Business clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                mTextViewCategory.setVisibility(View.VISIBLE);
                mTextViewCategory.setText("Business");
                break;
            case R.id.action_entertainment:
                NewsCategoryPreferences.updateCategory("entertainment");
                textToShow = "Entertainment clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                mTextViewCategory.setVisibility(View.VISIBLE);
                mTextViewCategory.setText("Entertainment");
                break;
            case R.id.action_general:
                NewsCategoryPreferences.updateCategory("general");
                textToShow = "general clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                mTextViewCategory.setVisibility(View.VISIBLE);
                mTextViewCategory.setText("General");
                break;
            case R.id.action_health:
                NewsCategoryPreferences.updateCategory("health");
                 textToShow = "health clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                mTextViewCategory.setVisibility(View.VISIBLE);
                mTextViewCategory.setText("General");
                break;
            case R.id.action_science:
                NewsCategoryPreferences.updateCategory("science");
                textToShow = "science clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                mTextViewCategory.setVisibility(View.VISIBLE);
                mTextViewCategory.setText("Science");
                break;
            case R.id.action_sports:
                NewsCategoryPreferences.updateCategory("sports");
                textToShow = "sports clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                mTextViewCategory.setVisibility(View.VISIBLE);
                mTextViewCategory.setText("Sports");
                break;
            case R.id.action_technology:
                NewsCategoryPreferences.updateCategory("technology");
                textToShow = "technology clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                mTextViewCategory.setVisibility(View.VISIBLE);
                mTextViewCategory.setText("Technology");
                break;
        }
        invalidateData();
        getSupportLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
        BreakingNewsSyncUtils.initialize(this,true);
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
       // BreakingNewsSyncUtils.initialize(this,true);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

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

