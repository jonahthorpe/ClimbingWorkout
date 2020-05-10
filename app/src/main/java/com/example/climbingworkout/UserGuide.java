package com.example.climbingworkout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class UserGuide extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);
        WebView myWebView = findViewById(R.id.user_guide);
        //myWebView.getSettings().setLoadWithOverviewMode(true);
        //myWebView.getSettings().setUseWideViewPort(true);
        myWebView.loadUrl("file:///android_asset/userguide.html");
    }
}
