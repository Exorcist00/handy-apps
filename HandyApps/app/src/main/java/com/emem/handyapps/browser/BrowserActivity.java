package com.emem.handyapps.browser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.emem.handyapps.R;

public class BrowserActivity extends AppCompatActivity {
    //todo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        WebView webView= (WebView)findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);//enables JavaScript
        webView.setVerticalScrollBarEnabled(false);//hides vertical scroll bar
        webView.setHorizontalScrollBarEnabled(false);
        webView.loadUrl("https://www.google.com/");
    }
}
