package com.plugin.myapp;
import java.io.IOException;
import java.io.InputStream;

import org.apache.cordova.DroidGap;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;

public class MainActivity extends DroidGap  {
	 private static MainActivity instance;


    @Override
    public void onCreate(Bundle savedInstanceState) {            
        super.onCreate(savedInstanceState);   
        super.init();   
        super.loadUrl("file:///android_asset/www/tdc_tutorial.html");
        //super.loadUrl("file:///android_asset/www/web/index.html",10000); 
    }
   

   
   

    public byte[] getData(String fileName){
   	 byte[] buffer=null;
   	try{
      	AssetManager assetManager = getAssets();
		InputStream input = assetManager.open(fileName);
		 int size = input.available();
        buffer = new byte[size];
        input.read(buffer);
        input.close();
        return buffer;
   	}
   	catch(IOException ex)
   	{
   		
   	}
   	 return buffer;
   }
   
}
