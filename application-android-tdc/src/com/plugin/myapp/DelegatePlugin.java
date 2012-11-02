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

import android.util.Log;

import com.ctb.tdc.web.TTSservlet.servlet.SpeechServlet;
import com.ctb.tdc.web.processingAction.ContentAction;
import com.ctb.tdc.web.processingAction.PersistenceAction;
import com.ctb.tdc.web.utils.ServletUtils;


public class DelegatePlugin extends Plugin {
	
    public static final String TAG                = "ExamplePlugin";
    public static final String LOGIN_ACTION    	  = "LOGIN";
    PersistenceAction persistenceAction=null;
    @Override
    public PluginResult execute(String action, JSONArray data, String callbackId) {
        Log.d(TAG, "execute() called. Action: " + action);
        PluginResult result = null;
        JSONObject json_data = null;
        String xmlData=null;
        JSONObject response = new JSONObject();
       
            try {
            	 response.put("OK", "OK");
            	 String method=null,xmlRequest=null,actionName=null;
            	/*ContentAction contentAction=new ContentAction();
            	contentAction.downloadItem("d");*/
            	PersistenceAction persistenceAction=null;
            	
               // systemTime.put("time", "Login Success at time "+System.currentTimeMillis());
              //  method="downloadFileParts";
               // method="getSubtest";
                
                
                for(int i=0;i<data.length();i++) {
                    json_data = data.getJSONObject(i);
                    xmlData=json_data.getString("param1");
                                      
            	}
                
                 method = json_data.getString("param1");
			      xmlRequest = json_data.getString("param2");
				 actionName = json_data.getString("param3");
				System.out.println("method-->>"+method+"xmlRequest-->"+xmlRequest+"actionName-->>"+actionName);
				if (actionName.equals("PersistenceAction")) {
					persistenceAction = new PersistenceAction();
					if ((method != null)
							&& (!method.equals(ServletUtils.NONE_METHOD))) {
						//String xml = ServletUtils.buildPersistenceParameters(	method, xmlRequest);
						String xml=xmlRequest;
						
						try {
							xmlData=persistenceAction.handleEvent(method, xml);
							
						} catch (Exception e) {
							// TODO: handle exception
						}
					} else {
						xmlData=doGetpersisXml(method);
					}
					 response.put("OK", xmlData);
				}
				else if (actionName.equals("ContentAction")) {
					
					ContentAction contentAction=new ContentAction();
					try{
					if (method !=null &&  method.equals(ServletUtils.GET_SUBTEST_METHOD)) {
						xmlData=contentAction.getSubtest(xmlRequest);
					} else if (method.equals(ServletUtils.DOWNLOAD_ITEM_METHOD)) {
						xmlData=contentAction.downloadItem(xmlRequest);
					} else if (method.equals(ServletUtils.GET_ITEM_METHOD)) {
						xmlData=contentAction.getItem(xmlRequest);
					}
					else if (method.equals(ServletUtils.GET_IMAGE_METHOD)) {
						xmlData=contentAction.getImage(xmlRequest);//need to check again throughly
					}
					else if (method.equals(ServletUtils.GET_LOCALRESOURCE_METHOD)) {
						xmlData=contentAction.getLocalResource(xmlRequest);
					}else if (method.equals(ServletUtils.GET_MUSIC_DATA_METHOD)) {
						xmlData=contentAction.getMusicData(xmlRequest);
					}
					else if (method.equals(ServletUtils.PLAY_MUSIC_DATA_METHOD)) {
						contentAction.playMusicData(xmlRequest);
					}
					else if (method.equals(ServletUtils.STOP_MUSIC_DATA_METHOD)) {
						contentAction.stopMusicData();
					}
					else if (method.equals(ServletUtils.GET_FILE_PARTS)){
						xmlData=contentAction.downloadFileParts (xmlRequest);
					}
					else {
						xmlData=ServletUtils.writeResponse(ServletUtils.ERROR);
					}
					 response.put("OK", xmlData);
					}
					
					catch(IOException ioEx){
						
					}
				}
				else if(actionName.equals("SpeechAction")){
					String speedVal=xmlRequest;
					String reqData=method;
					
					SpeechServlet speechServlet=new SpeechServlet();
					xmlData=speechServlet.readPostDataRequest(reqData,speedVal);
					response.put("OK", ServletUtils.OK);
					
				}
				
              else {
            	response.put("OK", ServletUtils.OK);
               // result = new PluginResult(Status.ERROR);
                Log.e(TAG, "Invalid action: " + action);
               
            }
            } catch (JSONException jsonEx) {
                Log.e(TAG, "Got JSON Exception " + jsonEx.getMessage());
                jsonEx.printStackTrace();
                result = new PluginResult(Status.JSON_EXCEPTION);
                return result;
            }
           
            result = new PluginResult(Status.OK,response);
        return result;
}
    public  String doGetpersisXml(String reqMethod)  {

  		String method = ServletUtils.getMethod(reqMethod);
  		long startTime = System.currentTimeMillis();
  		String xml = ServletUtils.getXml(method);
  		try {
  			return persistenceAction.handleEvent(method, xml);
  		} catch (IOException e) {
  			// TODO: handle exception
  		}
  		Log.e(TAG,"PersistenceServlet: " + method + " took "+ (System.currentTimeMillis() - startTime) + "\n");
        return xml;	
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