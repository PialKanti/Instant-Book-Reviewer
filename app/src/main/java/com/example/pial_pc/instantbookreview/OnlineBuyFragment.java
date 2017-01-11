package com.example.pial_pc.instantbookreview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;

public class OnlineBuyFragment extends Fragment {
    WebView googleWebView;
    ArrayList<String> bookDetails;
    String searchUrl="",bookTitle="",bookAuthor="";
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
        googleWebView=(WebView)rooView.findViewById(R.id.WebGoogle);
        bookDetails = new ArrayList<String>();
        bookDetails=BookDetailsActivity.getRequiredData();
        bookTitle=bookDetails.get(1);
        bookTitle=bookTitle.replaceAll(" ", "+");
        bookAuthor=bookDetails.get(2);
        bookAuthor=bookAuthor.replaceAll(" ", "+");
        searchUrl="http://www.google.com/search?q="+bookTitle+bookAuthor+"&as_sitesearch=rokomari.com";

        googleWebView.getSettings().setJavaScriptEnabled(true);
        googleWebView.setWebViewClient(new WebViewClient());
        googleWebView.getSettings().setLoadWithOverviewMode(true);
        googleWebView.getSettings().setUseWideViewPort(true);
        googleWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        googleWebView.loadUrl(searchUrl);
        googleWebView.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && googleWebView.canGoBack()) {
                    handler.sendEmptyMessage(1);
                    return true;
                }
                return false;
            }

        });

        return  rooView;
    }

   /* public class BuyBookOnline extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {

            return null;
        }


    }*/

    private void webViewGoBack(){
        googleWebView.goBack();
    }

}
