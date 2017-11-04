package com.example.android.heychat;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class NewsFeed extends AppCompatActivity {

    //Initialising the layout vviews

    private Toolbar mNewsFeedtoolbar;

    //Initialising the webview
    private WebView mWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        //Referening the layout views
        mNewsFeedtoolbar = (Toolbar)findViewById(R.id.NewsFeed_toolbar);
        setSupportActionBar(mNewsFeedtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("NEWS FEED");

        //Instentiating and reeferncing the weeb view
        mWebview = new WebView(this);
        mWebview = (WebView)findViewById(R.id.Webview);
        mWebview.getSettings().setJavaScriptEnabled(true); //Enable javascript
        mWebview.loadUrl("http://www.google.com");
        mWebview.setWebViewClient(new WebViewController());



    }
}
