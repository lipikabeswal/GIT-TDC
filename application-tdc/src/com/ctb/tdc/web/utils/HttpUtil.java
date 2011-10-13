package com.ctb.tdc.web.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;





public class HttpUtil {

	private static String currentUrl;
	@SuppressWarnings("deprecation")
	public static String getTracker(String file,
			String subtestId) throws Exception {
		URL u;
		InputStream is = null;
		DataInputStream dis = null;
		String s = null;
		StringBuilder trackerXml = new StringBuilder(1000);
		HashMap subtestIdMap = new HashMap();
		MemoryCache memCache = MemoryCache.getInstance();
		subtestIdMap = memCache.getContentDownloadMap();
		System.out.println("Subtest Id map" + subtestIdMap.get(subtestId));
		currentUrl =(String) subtestIdMap.get(subtestId);
		u = new URL(currentUrl + /*"?part=" +*/ file+".xml");
		is = u.openStream(); // throws an IOException

		try {
			dis = new DataInputStream(new BufferedInputStream(is));

			while ((s = dis.readLine()) != null) {
				trackerXml.append(s);
				System.out.println(s);
			}
		} finally {
			FileUtil.closeResource(dis);
		}
		

		if(trackerXml.toString().contains("ERROR")){
			throw new Exception("Exception while retrieving subtest XML");
		}
		return trackerXml.toString();

	}

	public static void downloadContent(String file)
			throws IOException {
		URL u;
		InputStream is = null;
		DataInputStream input = null;
        boolean isDownLoadCompleted = false;
		try {

			u = new URL(currentUrl + file);
			is = u.openStream(); // throws an IOException
			input = new DataInputStream(new BufferedInputStream(is));
			//isDownLoadCompleted = true;
			FileUtil.saveHttpDownloadContentIntoFile(input, ServletUtils.tempPath + file);
		} finally {
			FileUtil.closeResource(input);
			
		}

	}

}
