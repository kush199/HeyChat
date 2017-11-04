package com.example.android.heychat.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.android.heychat.MainActivity;
import com.example.android.heychat.WebViewController;

import com.example.android.heychat.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {
    //Initialising the webview
    private WebView mWebview;


    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_request, container, false);

        //Instentiating and reeferncing the weeb view
        mWebview = new WebView(getActivity().getApplicationContext());
        mWebview = (WebView) view.findViewById(R.id.Webview);
        mWebview.getSettings().setJavaScriptEnabled(true); //Enable javascript
        mWebview.loadUrl("http://www.google.com");
        mWebview.setWebViewClient(new WebViewController());
        return view;

    }

}
