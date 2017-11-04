package com.example.android.heychat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class FAQs extends AppCompatActivity {

    //Initialising the layout views
    private Toolbar mFAQsToolbar;

    //Initialising the Firebase Elements

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);

        //Referncing gthe layout kvariable
        mFAQsToolbar = (Toolbar)findViewById(R.id.FAQs_toolbar);
        setSupportActionBar(mFAQsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("FAQs");


    }
}
