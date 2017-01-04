package com.busimplified.codingstreet;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    private AdView mAdView;
    private WebView webView;
    private ProgressBar mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isConnected = isNetConnected(this);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webView);
        webView.clearCache(true);
        mAdView = (AdView) findViewById(R.id.adView);
        mProgressView = (ProgressBar) findViewById(R.id.progressBar);
        if (isConnected) {
            MobileAds.initialize(getApplicationContext(), "ca-app-pub-2376950998506083~1750153856");
            webView.loadUrl(String.valueOf(Uri.parse("http://codingstreet.com")));

            AdRequest adRequest = new AdRequest.Builder().build();

            if (adRequest != null) {
                mAdView.loadAd(adRequest);
            }

        } else {
            webView.loadUrl("file:///android_asset/connect_internet.html");
            webView.setVisibility(View.VISIBLE);
            mProgressView.setVisibility(View.GONE);
        }

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        CookieManager.getInstance().setAcceptCookie(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowContentAccess(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.setVisibility(View.VISIBLE);
                mProgressView.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public  boolean isNetConnected(Context context) {
        if (context != null) {
            final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                    .CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
}
