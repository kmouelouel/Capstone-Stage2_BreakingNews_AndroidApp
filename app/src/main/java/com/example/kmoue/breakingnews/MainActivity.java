package com.example.kmoue.breakingnews;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kmoue.breakingnews.utilities.NetworkUtils;

import java.net.URL;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadNewsData();

    }

    private void loadNewsData() {
        Context context=this.getApplicationContext();
        String category= "";
        FetchNewsAsyncTask mFetchNewsAsyncTask= new FetchNewsAsyncTask(context);
         mFetchNewsAsyncTask.execute("");

    }
    public class FetchNewsAsyncTask extends AsyncTask<String, Void,String> {
        Context mContext;
        private ProgressBar mLoadingIndicator;
        private TextView mErrorMessageDisplay;
        private TextView mTextViewResultsJson;
        public FetchNewsAsyncTask(Context context) {
            mContext = context;
            mLoadingIndicator = (ProgressBar) findViewById(R.id.progressBar_indicator);
            mErrorMessageDisplay = (TextView)  findViewById(R.id.tv_error_message_display);
            mTextViewResultsJson = (TextView) findViewById(R.id.tv_search_results_json);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        // protected List<NewsObject> doInBackground(String... params) {
        protected String doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            String sortPath = params[0];
            URL newsRequestURL = NetworkUtils.buildUrl();
            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(newsRequestURL);
                //  List<NewsObject> NewsJsonData = OpenNewsJsonUtils.getNewsFromJson(mContext, jsonResponse);
                return jsonResponse;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(String newsData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (newsData != null && !newsData.equals("")) {
                showDataView();
                mTextViewResultsJson.setText(newsData.toString());
            } else {
                showErrorMessage();
            }
        }

        private void showDataView(){
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
            mTextViewResultsJson.setVisibility(View.VISIBLE);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
        private void showErrorMessage(){
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
            mTextViewResultsJson.setVisibility(View.INVISIBLE);
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
                textToShow ="Business clicked" ;
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_entertainment:
                  textToShow = "Entertainment clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_general:
                textToShow = "general clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_health:
                textToShow = "health clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_science:
                textToShow = "science clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_sports:
                textToShow = "sports clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_technology:
                textToShow = "technology clicked";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
