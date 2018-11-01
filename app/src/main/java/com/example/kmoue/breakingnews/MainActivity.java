package com.example.kmoue.breakingnews;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kmoue.breakingnews.adapters.NewsAdapter;
import com.example.kmoue.breakingnews.utilities.NetworkUtils;
import com.example.kmoue.breakingnews.utilities.OpenNewsJsonUtils;

import java.net.URL;


public class MainActivity extends AppCompatActivity implements NewsAdapter.NewsAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<String[]> {
    private static final int NEWS_LOADER_ID =0;

    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;

    private static final String SAVE_STATE_KEY = "savedState";
    private static String category = "";
    private RecyclerView mRecyclerView;
    private NewsAdapter mNewsAdapter;
    private String[] fetchedNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.progressBar_indicator);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_news);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mNewsAdapter = new NewsAdapter(this);
        mRecyclerView.setAdapter(mNewsAdapter);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVE_STATE_KEY)) {
                mNewsAdapter.setNewsData(savedInstanceState.getStringArray(SAVE_STATE_KEY));

            }
        } else {
         //   loadNewsData();
            int loaderId = NEWS_LOADER_ID;
            LoaderManager.LoaderCallbacks<String[]> callback = MainActivity.this;
            Bundle bundleForLoader = null;
            getSupportLoaderManager().initLoader(loaderId,bundleForLoader,callback);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray(SAVE_STATE_KEY, fetchedNews);
    }




    @Override
    public void onClick(String newsData) {
        Toast.makeText(this, newsData, Toast.LENGTH_SHORT).show();
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentTOStartDetailActivity;
        intentTOStartDetailActivity = new Intent(context, destinationClass);
        intentTOStartDetailActivity.putExtra(Intent.EXTRA_TEXT, newsData);
        startActivity(intentTOStartDetailActivity);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        //   Return true to display your menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        Context context = MainActivity.this;
        String textToShow = "";
        switch (itemThatWasClickedId) {
            case R.id.action_business:
                this.category = "business";
                textToShow = "Business clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_entertainment:
                this.category = "entertainment";
                textToShow = "Entertainment clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_general:
                this.category = "general";
                textToShow = "general clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_health:
                this.category = "health";
                textToShow = "health clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_science:
                this.category = "science";
                textToShow = "science clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_sports:
                this.category = "sports";
                textToShow = "sports clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_technology:
                this.category = "technology";
                textToShow = "technology clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public android.support.v4.content.Loader<String[]> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<String[]>(this) {
            String[] mNewsTitleStringArray=null;

            @Override
            protected void onStartLoading() {
                if(mNewsTitleStringArray !=null){
                    deliverResult(mNewsTitleStringArray);
                }
                else{
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public String[] loadInBackground() {
                URL newsRequestURL = NetworkUtils.buildUrl();
                try {
                    String jsonResponse = NetworkUtils.getResponseFromHttpUrl(newsRequestURL);
                    String[]  NewsJsonData = OpenNewsJsonUtils.getNewsFromJson(MainActivity.this, jsonResponse);
                    return NewsJsonData;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            public void deliverResult(String[] data) {
                mNewsTitleStringArray = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<String[]> loader, String[] data) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if(data == null){
                showErrorMessage();
            }else{
                showDataView();
                mNewsAdapter.setNewsData(data);
            }
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<String[]> loader) {

    }
    private void showDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }
    private void invalidateData() {
        mNewsAdapter.setNewsData(null);
    }
}

