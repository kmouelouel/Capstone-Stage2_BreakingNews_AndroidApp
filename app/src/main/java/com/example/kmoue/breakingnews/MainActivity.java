package com.example.kmoue.breakingnews;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
{
        private RecyclerView mRecyclerView;
        private NewsAdapter mNewsAdapter;
        private static final String SAVE_STATE_KEY ="savedState";
      private static String category="";
      private String[] fetchedNews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView =(RecyclerView) findViewById(R.id.recyclerview_news);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mNewsAdapter= new NewsAdapter(this);
        mRecyclerView.setAdapter(mNewsAdapter);
        if(savedInstanceState!=null){
            if(savedInstanceState.containsKey(SAVE_STATE_KEY)){
                mNewsAdapter.setNewsData(savedInstanceState.getStringArray(SAVE_STATE_KEY));

            }
        }else {
            loadNewsData();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray(SAVE_STATE_KEY,fetchedNews);
    }

    private void loadNewsData() {
        Context context=this.getApplicationContext();
        String category= "";
        FetchNewsAsyncTask mFetchNewsAsyncTask= new FetchNewsAsyncTask(context);
         mFetchNewsAsyncTask.execute(category);

    }

    @Override
    public void onClick(String newsData) {
        Toast.makeText(this, newsData, Toast.LENGTH_SHORT).show();
        Context context= this;
        Class destinationClass = DetailActivity.class;
        Intent intentTOStartDetailActivity;
        intentTOStartDetailActivity = new Intent(context,destinationClass);
        intentTOStartDetailActivity.putExtra(Intent.EXTRA_TEXT,newsData);
        startActivity(intentTOStartDetailActivity);
    }

    public class FetchNewsAsyncTask extends AsyncTask<String, Void,String[]> {
        Context mContext;
        private ProgressBar mLoadingIndicator;
        private TextView mErrorMessageDisplay; ;
        public FetchNewsAsyncTask(Context context) {
            mContext = context;
            mLoadingIndicator = (ProgressBar) findViewById(R.id.progressBar_indicator);
            mErrorMessageDisplay = (TextView)  findViewById(R.id.tv_error_message_display);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        // protected List<NewsObject> doInBackground(String... params) {
        protected String[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            String sortPath = params[0];
            URL newsRequestURL = NetworkUtils.buildUrl();
            String[] NewsJsonData=null;
            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(newsRequestURL);
                 NewsJsonData = OpenNewsJsonUtils.getNewsFromJson(mContext, jsonResponse);
                return NewsJsonData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(String[] newsData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (newsData != null && !newsData.equals("")) {
                showDataView();
                fetchedNews= new String[newsData.length];
                fetchedNews= newsData;
               mNewsAdapter.setNewsData(newsData);
            } else {
                showErrorMessage();
            }
        }

        private void showDataView(){
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
        private void showErrorMessage(){
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
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
        switch(itemThatWasClickedId){
            case R.id.action_business:
                this.category="business";
                textToShow ="Business clicked" ;
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_entertainment:
                this.category="entertainment";
                  textToShow = "Entertainment clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_general:
                this.category="general";
                textToShow = "general clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_health:
                this.category="health";
                textToShow = "health clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_science:
                this.category="science";
                textToShow = "science clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_sports:
                this.category="sports";
                textToShow = "sports clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_technology:
                this.category="technology";
                textToShow = "technology clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
