package com.example.android.heychat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class AboutUs extends AppCompatActivity {

    //Initialising the Layout Variable
    private Toolbar mAboutUsToolbar;

    //Initialising the Firebase variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        //Referencing the Layout views
        mAboutUsToolbar = (Toolbar)findViewById(R.id.About_Us_toolbar);
        setSupportActionBar(mAboutUsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ABOUT US");



    }
}
