package com.ctb.tdc.web.processingAction;

import java.io.IOException;

import android.util.Log;

import com.ctb.tdc.web.utils.ServletUtils;

public class ContentActionDesc {
	String xmlData = null;
	static ContentAction contentAction=null;
	static ContentActionDesc contentActionDesc=null;
	public static ContentActionDesc getContentActionDescInstance() {
	      if(contentActionDesc == null) {
	    	  contentActionDesc = new ContentActionDesc();
	      }
	      return contentActionDesc;
	   }
	public static ContentAction getInstance() {
	      if(contentAction == null) {
	    	  contentAction = new ContentAction();
	      }
	      return contentAction;
	   }

	public String getXmlData(String method, String xmlRequest)  throws IOException {
		try {
			contentAction=getInstance();
			if (method != null
					&& method.equals(ServletUtils.GET_SUBTEST_METHOD)) {
				xmlData = contentAction.getSubtest(xmlRequest);
			} else if (method.equals(ServletUtils.DOWNLOAD_ITEM_METHOD)) {
				xmlData = contentAction.downloadItem(xmlRequest);
			} else if (method.equals(ServletUtils.GET_TE_ITEM)) {
				System.out.println("method" + method);

				xmlData = ServletUtils.TE_ITEM_FOLDER_PATH;
			} else if (method.equals(ServletUtils.GET_ITEM_METHOD)) {
				xmlData = contentAction.getItem(xmlRequest);// need to check
															// again throughly
			} else if (method.equals(ServletUtils.GET_IMAGE_METHOD)) {
				xmlData = contentAction.getImage(xmlRequest);// need to check
																// again
																// throughly
			} else if (method.equals(ServletUtils.GET_LOCALRESOURCE_METHOD)) {
				xmlData = contentAction.getLocalResource(xmlRequest);

			}

			else if (method.equals(ServletUtils.GET_MUSIC_DATA_METHOD)) {
				xmlData = contentAction.getMusicData(xmlRequest);
			} else if (method.equals(ServletUtils.PLAY_MUSIC_DATA_METHOD)) {
				contentAction.playMusicData(xmlRequest);
			}

			else if (method.equals(ServletUtils.STOP_MUSIC_DATA_METHOD)) {
				contentAction.stopMusicData();
			} else if (method.equals("setVolume")) {
				contentAction.setVolume(xmlRequest);
			} else if (method.equals(ServletUtils.GET_FILE_PARTS)) {
				xmlData = contentAction.downloadFileParts(xmlRequest);
			} else {
				xmlData = ServletUtils.writeResponse(ServletUtils.ERROR);
			}
          
		}
		catch (IOException ix) {
			Log.d("Error", ix.getMessage());

		}
		catch (Exception ex) {
			Log.d("Error", ex.getMessage());

		}
		return xmlData;
	}

}
