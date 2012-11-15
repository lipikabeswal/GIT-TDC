package com.ctb.tdc.web.plugin.delegateActivity;

import org.apache.cordova.CordovaWebViewClient;
import org.apache.cordova.DroidGap;

import android.webkit.WebView;

public  class GWTCordovaWebViewClient extends CordovaWebViewClient {

public GWTCordovaWebViewClient(DroidGap ctx) {
super(ctx);
}
@Override
   public boolean shouldOverrideUrlLoading(WebView view, String url) {
      if(url.startsWith("file://"))
      {
      return false;
      }
      
      return super.shouldOverrideUrlLoading(view, url);
      
   }

}