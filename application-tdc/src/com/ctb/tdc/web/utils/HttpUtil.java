package com.ctb.tdc.web.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import com.ctb.tdc.web.utils.ServletUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;



public class HttpUtil {

	private static String currentUrl;
	@SuppressWarnings("deprecation")
	public static String getTracker(String file,
			String subtestId) throws Exception {
		//URL u;
		InputStream is = null;
		DataInputStream dis = null;
		String s = null;
		StringBuilder trackerXml = new StringBuilder(1000);
		HashMap subtestIdMap = new HashMap();
		MemoryCache memCache = MemoryCache.getInstance();
		
		DefaultHttpClient client = ServletUtils.client;
		
		subtestIdMap = memCache.getContentDownloadMap();
		System.out.println("Subtest Id map" + subtestIdMap.get(subtestId));
		currentUrl =(String) subtestIdMap.get(subtestId);
		HttpGet httpget = new HttpGet(currentUrl +  file+".xml");
		HttpResponse response = client.execute(httpget);
		int responseCode = HttpStatus.SC_OK;
		
		responseCode = response.getStatusLine().getStatusCode();
		if (responseCode == HttpStatus.SC_OK) {
			try {
				
				is = response.getEntity().getContent();
				
				dis = new DataInputStream(new BufferedInputStream(is));

				while ((s = dis.readLine()) != null) {
					trackerXml.append(s);
					System.out.println(s);
				}
					
			}finally {
					FileUtil.closeResource(dis);
			}
			
			if(trackerXml.toString().contains("ERROR")){
				throw new Exception("Exception while retrieving subtest XML");
			}
			
			return trackerXml.toString();

		}else{
			throw new Exception("Exception while retrieving subtest XML");
		}
		
		
		/*try {
			dis = new DataInputStream(new BufferedInputStream(is));

			while ((s = dis.readLine()) != null) {
				trackerXml.append(s);
				System.out.println(s);
			}
		} finally {
			FileUtil.closeResource(dis);
		}
		*/
}
	

	public static void downloadContent(String file)
			throws IOException {
		URL u;
		InputStream is = null;
		DataInputStream input = null;
        boolean isDownLoadCompleted = false;
        
        DefaultHttpClient client = ServletUtils.client;
        
        HttpGet httpget = new HttpGet(currentUrl +  file);
		HttpResponse response = client.execute(httpget);
		int responseCode = HttpStatus.SC_OK;
        
		responseCode = response.getStatusLine().getStatusCode();
		if (responseCode == HttpStatus.SC_OK) {
			try {
				is =  response.getEntity().getContent();
				input = new DataInputStream(new BufferedInputStream(is));
				//isDownLoadCompleted = true;
				FileUtil.saveHttpDownloadContentIntoFile(input, ServletUtils.tempPath + file);
			}finally {
				FileUtil.closeResource(input);
				
			}
		}				
		/*try {

			u = new URL(currentUrl + file);
			is = u.openStream(); // throws an IOException
			input = new DataInputStream(new BufferedInputStream(is));
			//isDownLoadCompleted = true;
			FileUtil.saveHttpDownloadContentIntoFile(input, ServletUtils.tempPath + file);
		} finally {
			FileUtil.closeResource(input);
			
		}*/

	}

}
