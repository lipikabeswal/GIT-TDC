package com.ctb.tdc.web.plugin.deligateActivity;
import java.io.File;

import org.apache.cordova.CordovaChromeClient;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.DroidGap;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Toast;

public class OasAndroidApp extends DroidGap  {
	 private static OasAndroidApp instance;
	 int windowfocus_count=0;
	 private  boolean pause_falg=false;
  Context con=getContext();
	// private WebView mWebView;
    @Override
    public void onCreate(Bundle savedInstanceState) {            
        super.onCreate(savedInstanceState);   
        super.init(); 
       // requestWindowFeature(Window.FEATURE_NO_TITLE);   
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        //setContentView(R.layout.activity_main_share); 
      //  getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
      
       // getWindow().getDecorView().setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
       // clearApplicationData();

       // super.loadUrl("ok.");
      //Remove title bar9.   
       // this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
        //Remove notification bar12.    
     //   this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
       // con.deleteDatabase("webview.db");
      //  con.deleteDatabase("webviewCache.db");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.loadUrl("file:///android_asset/www/tdc_tutorial.html");
       // super.loadUrl("http://ec2-23-20-104-248.compute-1.amazonaws.com/test.html",10000);
        
      
    }
 // handler for the background updating
    Handler progressHandler = new Handler() 
    {
        public void handleMessage(Message msg) 
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    };
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
 @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 ///  finishing the application whenever user presses home button 
		OasAndroidApp.this.finish(); 
	} 
   /* (non-Javadoc)
	 * @see org.apache.cordova.DroidGap#onConfigurationChanged(android.content.res.Configuration)
	 */
 @Override 
 public void onWindowFocusChanged(boolean hasFocus) {
 	// TODO Auto-generated method stub
 	super.onWindowFocusChanged(hasFocus);
 	    	
 	if(!hasFocus && !pause_falg)
 		finish();

 } 

 public void clearApplicationData() {
		File cache = getCacheDir();
		File appDir = new File(cache.getParent());
		if (appDir.exists()) {
			String[] children = appDir.list();
			for (String s : children) {
				if (!s.equals("lib")) {
					deleteDir(new File(appDir, s));
					Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
				}
			}
		}
	}

 public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		return dir.delete();
	}
}
