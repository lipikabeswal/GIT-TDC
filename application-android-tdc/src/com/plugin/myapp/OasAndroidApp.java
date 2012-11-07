package com.plugin.myapp;
import org.apache.cordova.CordovaChromeClient;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.DroidGap;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;

public class OasAndroidApp extends DroidGap  {
	 private static OasAndroidApp instance;

	// private WebView mWebView;
    @Override
    public void onCreate(Bundle savedInstanceState) {            
        super.onCreate(savedInstanceState);   
        super.init();  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        super.loadUrl("file:///android_asset/www/tdc_tutorial.html");
      
    }
    @Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);

		System.out.println("onConfigurationChanged");
	}
  @Override
    public void init() {
    super.init((CordovaWebView) new WebView(this), new GWTCordovaWebViewClient(this), new CordovaChromeClient(this));
    }
    
   @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
	} 
    
   /* (non-Javadoc)
	 * @see org.apache.cordova.DroidGap#onConfigurationChanged(android.content.res.Configuration)
	 */
	
}
