package com.example.kmoue.breakingnews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
private TextView mNewsDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mNewsDisplay =(TextView) findViewById(R.id.tv_display_newsDetails);
        Intent intentThatStartThisActivity = getIntent();
        if(intentThatStartThisActivity !=null && intentThatStartThisActivity.hasExtra((Intent.EXTRA_TEXT))){
            String mNewsData= intentThatStartThisActivity.getStringExtra(Intent.EXTRA_TEXT);
            mNewsDisplay.setText(mNewsData);
            
        }
    }
}
