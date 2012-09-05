package com.plugin.myapp;
import org.apache.cordova.DroidGap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends DroidGap {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       super.loadUrl("file:///android_asset/www/index.html");
        
        
    }
}
