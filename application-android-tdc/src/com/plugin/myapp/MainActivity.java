package com.plugin.myapp;
import java.io.IOException;
import java.io.InputStream;

import org.apache.cordova.DroidGap;

import android.content.res.AssetManager;
import android.os.Bundle;

public class MainActivity extends DroidGap {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // ContentAction myobj=new ContentAction(MainActivity.this);
      /* String  hash="FEBA1F02D41BFF3D2624E0E218B9015A";
    	String	   key="n7673nBJ2n27bB4oAfme7Ugl5VV42g8";
        byte[] buffer =this.getData("38682602.ecp");
	      byte[] byteData=ContentFile.decryptFileTest(buffer, hash, key);*/
       super.loadUrl("file:///android_asset/www/index_speechTest.html");
       
      
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
