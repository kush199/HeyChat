package com.example.android.heychat;

import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by CHALLIDO on 7/10/2017.
 */

public class WebViewController extends WebViewClient {
    public WebViewController() {
        super();
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view,  String url) {
        view.loadUrl(url);
        return  true;
    }
}
