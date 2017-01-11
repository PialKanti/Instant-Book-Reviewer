package com.example.pial_pc.instantbookreview;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;

public class ReviewFragment extends Fragment {

    WebView reviewWebView;
    ArrayList<String> bookDetails;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:{
                    webViewGoBack();
                }break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rooView=inflater.inflate(R.layout.book_online_buy_fragment, container, false);
        reviewWebView=(WebView)rooView.findViewById(R.id.WebGoogle);
        bookDetails = new ArrayList<String>();
        bookDetails=BookDetailsActivity.getRequiredData();


        reviewWebView.getSettings().setJavaScriptEnabled(true);
        reviewWebView.setInitialScale(150);
        reviewWebView.getSettings().setBuiltInZoomControls(true);
        reviewWebView.setWebViewClient(new WebViewClient());
        reviewWebView.getSettings().setLoadWithOverviewMode(true);
        reviewWebView.getSettings().setUseWideViewPort(true);
        reviewWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        reviewWebView.loadUrl(bookDetails.get(6));
        reviewWebView.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && reviewWebView.canGoBack()) {
                    handler.sendEmptyMessage(1);
                    return true;
                }
                return false;
            }

        });

        return  rooView;
    }


    private void webViewGoBack(){
        reviewWebView.goBack();
    }


}
