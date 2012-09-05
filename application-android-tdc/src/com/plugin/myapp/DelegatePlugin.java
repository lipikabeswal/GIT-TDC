package com.plugin.myapp;




import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ctb.tdc.web.processingAction.ContentAction;
import com.ctb.tdc.web.processingAction.processTest;

import android.util.Log;


public class DelegatePlugin extends Plugin {
	
    public static final String TAG                = "ExamplePlugin";
    public static final String LOGIN_ACTION    	  = "LOGIN";
 
    @Override
    public PluginResult execute(String action, JSONArray data, String callbackId) {
        Log.d(TAG, "execute() called. Action: " + action);
        PluginResult result = null;
        JSONObject json_data = null;
        System.out.println("hi.............");
        if (LOGIN_ACTION.equals(action)) {
            try {
            	
            	processTest process=new processTest();
            	String val=process.getValue();
            
            	for(int i=0;i<data.length();i++) {
                    json_data = data.getJSONObject(i);
                    Log.d(TAG, " Value of param1 " + json_data.getString("param1"));
                    Log.d(TAG, " Value of param2 " + json_data.getString("param2"));
            	}
            	//postData("http://192.168.14.172:8080/TMS/TMSServlet", json_data);
            	JSONObject systemTime = new JSONObject();
                systemTime.put("time", "Login Success at time "+System.currentTimeMillis());
                
                result = new PluginResult(Status.OK, systemTime);
                Log.d(TAG, " Value of result " + result);
            } catch (JSONException jsonEx) {
                Log.e(TAG, "Got JSON Exception " + jsonEx.getMessage());
                jsonEx.printStackTrace();
                result = new PluginResult(Status.JSON_EXCEPTION);
            }
         
        } else {
            result = new PluginResult(Status.INVALID_ACTION);
            Log.e(TAG, "Invalid action: " + action);
        }
        return result;
    }
    
    public void  postData(String result,JSONObject json_data) {
        // Create a new HttpClient and Post Header
    
    try {
	        HttpClient httpclient = new DefaultHttpClient();
	        HttpParams myParams = new BasicHttpParams();
	        HttpConnectionParams.setConnectionTimeout(myParams, 10000);
	        HttpConnectionParams.setSoTimeout(myParams, 10000);
	        
	        HttpPost httppost = new HttpPost(result.toString());
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("method", json_data.getString("param1")));
			nameValuePairs.add(new BasicNameValuePair("requestXML", json_data.getString("param2")));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        HttpResponse response = httpclient.execute(httppost);
	        Integer response_code = response.getStatusLine().getStatusCode();
	        Log.i("tag ", response_code.toString());
	        String temp = EntityUtils.toString(response.getEntity());
	       System.out.println(response_code +"sd "+temp);
	        //Log.i("tag ", temp);
        } catch (ClientProtocolException e) {
        	Log.e(TAG, "Error: " + e);
        } catch (JSONException e) {
        	Log.e(TAG, "Error: " + e);
        } catch (IOException e) {
        	Log.e(TAG, "Error: " + e);
        }
    }
   
}